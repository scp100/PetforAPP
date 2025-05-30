import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// ===== Task.kt =====

data class Task(
    val id: Int = nextId(),
    var title: String,
    var description: String = "",
    var priority: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var completedAt: LocalDateTime? = null
) {
    companion object {
        private var seq = 0
        private fun nextId() = ++seq
    }

    val rewardExp: Int = (priority.coerceIn(1, 5) * 10)
    val isCompleted: Boolean get() = completedAt != null

    fun complete() {
        if (completedAt == null) {
            completedAt = LocalDateTime.now()
        }
    }

    override fun toString(): String {
        val status = if (isCompleted) "✅" else "⏳"
        val stars = "★".repeat(priority)
        return "\$status [\$id] \$title (\$stars) - \$rewardExp 经验值"
    }
}

// ===== Pet.kt =====

enum class PetState { WALK, EAT, STARVE, DEAD }

data class Pet(
    val name: String,
    val type: String,
    var level: Int = 1,
    var exp: Int = 0,
    var happiness: Int = 80,
    var health: Int = 100,
    var hunger: Int = 20,
    var lastFed: LocalDateTime = LocalDateTime.now()
) {
    fun gainExp(amount: Int) {
        exp += amount
        happiness = (happiness + amount / 5).coerceAtMost(100)
        checkLevelUp()
    }

    private fun checkLevelUp() {
        val needed = level * 100
        if (exp >= needed) {
            level++
            exp -= needed
            happiness = (happiness + 20).coerceAtMost(100)
            println("🎉 \$name 升级到 Level \$level !")
        }
    }

    fun feed() {
        hunger = (hunger - 30).coerceAtLeast(0)
        happiness = (happiness + 15).coerceAtMost(100)
        health = (health + 10).coerceAtMost(100)
        lastFed = LocalDateTime.now()
        println("🍖 \$name 很开心地吃了食物！")
    }

    fun timePass() {
        hunger = (hunger + 5).coerceAtMost(100)
        if (hunger > 80) {
            happiness = (happiness - 10).coerceAtLeast(0)
            health = (health - 5).coerceAtLeast(0)
        }
    }

    fun getStatus(): String {
        val mood = when {
            happiness >= 80 -> "😄"
            happiness >= 60 -> "😊"
            happiness >= 40 -> "😐"
            happiness >= 20 -> "😞"
            else -> "😢"
        }
        fun bar(v: Int) = "█".repeat(v / 10) + "░".repeat(10 - v / 10)
        return """
            🐾 宠物状态 🐾
            名字: \$name (\$type) \$mood
            等级: \$level (EXP: \$exp/\${level*100})
            生命值: \$health/100 ${bar(health)}
            快乐度: \$happiness/100 ${bar(happiness)}
            饥饿度: \$hunger/100 ${bar(hunger)}
        """.trimIndent()
    }
}

// ===== PetTodoSystem.kt =====

class PetTodoSystem {
    private val tasks = mutableListOf<Task>()
    private lateinit var pet: Pet
    private val scanner = Scanner(System.`in`)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun start() {
        println("🌟 欢迎来到宠物养成TodoList！🌟")
        setupPet()
        while (true) {
            pet.timePass()
            showMenu()
            when (getChoice()) {
                1 -> addTask()
                2 -> viewTasks()
                3 -> completeTask()
                4 -> println(pet.getStatus())
                5 -> feedPet()
                6 -> showStatistics()
                0 -> { println("再见，记得照顾好你的 \${pet.name}！"); return }
                else -> println("无效选择，请重试！")
            }
        }
    }

    private fun setupPet() {
        print("给你的宠物起个名字: ")
        val name = readLine().orEmpty()
        println("选择宠物类型：1.🐱 2.🐶 3.🐰 4.🐸")
        val types = arrayOf("","小猫","小狗","小兔","小青蛙")
        val type = types.getOrNull(getChoice()) ?: "小猫"
        pet = Pet(name, type)
        println("🎉 \$name 诞生了！开始你的养成旅程！")
        println(pet.getStatus())
    }

    private fun showMenu() {
        println("\n${"=".repeat(30)}")
        println("1. 添加新任务  2. 查看任务  3. 完成任务")
        println("4. 查看宠物状态 5. 喂食宠物 6. 查看统计 0. 退出")
        println("${"=".repeat(30)}")
        print("请选择: ")
    }

    private fun addTask() {
        print("任务标题: ")
        val title = readLine().orEmpty()
        print("任务描述: ")
        val desc = readLine().orEmpty()
        print("优先级(1-5): ")
        val prio = getChoice().coerceIn(1,5)
        val now = LocalDateTime.now()
        tasks += Task(title = title, description = desc, priority = prio, dueDate = now)
        println("✨ 任务添加成功，完成可获得 \${prio*10} 经验值！")
    }

    private fun viewTasks() {
        if (tasks.isEmpty()) { println("暂无任务"); return }
        println("\n⏳ 待办任务:")
        tasks.filterNot(Task::isCompleted).forEach { println(it) }
        println("\n✅ 已完成任务:")
        tasks.filter(Task::isCompleted).forEach { println(it) }
    }

    private fun completeTask() {
        val pending = tasks.filterNot(Task::isCompleted)
        if (pending.isEmpty()) { println("全部完成！"); return }
        pending.forEachIndexed { i, t -> println("${i+1}. \$t") }
        print("编号: ")
        val idx = getChoice() -1
        if (idx in pending.indices) {
            val task = pending[idx]
            task.complete()
            println("完成 \${task.title}，获得 \${task.rewardExp} 经验值！")
            pet.gainExp(task.rewardExp)
            if (task.priority>=4) pet.feed()
        } else println("编号无效！")
    }

    private fun feedPet() {
        val doneToday = tasks.count { it.isCompleted && it.completedAt?.toLocalDate() == LocalDateTime.now().toLocalDate() }
        if (doneToday>0) pet.feed() else println("先完成任务再喂食哦！")
    }

    private fun showStatistics() {
        val total = tasks.size
        val done = tasks.count(Task::isCompleted)
        val exp = tasks.filter(Task::isCompleted).sumOf(Task::rewardExp)
        println("总任务: \$total，已完成: \$done，经验: \$exp")
    }

    private fun getChoice(): Int = try { readLine()?.toInt() ?: -1 } catch (_:Exception) { -1 }
}

fun main() {
    PetTodoSystem().start()
}
