package dev.yovany.todoapp.domain

enum class Category(val color: Int) {
    WORK(0xFF007AFF.toInt()),
    PERSONAL(0xFFFF9500.toInt()),
    SHOPPING(0xFF34C759.toInt()),
    STUDY(0xFFAF52DE.toInt()),
    HEALTH(0xFFFF2D55.toInt()),
    FINANCE(0xFF5856D6.toInt()),
    HOME(0xFFFFCC00.toInt()),
    TRAVEL(0xFF00C7BE.toInt()),
    PROJECTS(0xFF32ADE6.toInt()),
    FITNESS(0xFFFF4981.toInt()),
    READING(0xFF8E8E93.toInt()),
    SOCIAL(0xFF1DA1F2.toInt()),
    HOBBIES(0xFF4CD964.toInt()),
    URGENT(0xFFFF3B30.toInt()),
    EVENTS(0xFF785EF0.toInt()),
    IDEAS(0xFFDC267F.toInt()),
    LEARNING(0xFFFE6100.toInt()),
    APPOINTMENTS(0xFF648FFF.toInt()),
    REMINDERS(0xFFFFB000.toInt()),
    WRITING(0xFF009E73.toInt()),
    FAMILY(0xFFE69F00.toInt()),
    VOLUNTEERING(0xFFCC79A7.toInt()),
    GOALS(0xFF56B4E9.toInt()),
    MUSIC(0xFF32ADE1.toInt()),
    OTHER(0xFF8E8E93.toInt()),
    ;

    companion object {
        fun fromOrdinal(ordinal: Int): Category? {
            return entries.find { it.ordinal == ordinal }
        }
    }
}