package dev.yovany.todoapp.domain

enum class Category(val color: Int) {
    WORK(0xFF5677FC.toInt()),        // Azul índigo más claro y vibrante
    PERSONAL(0xFF00BCD4.toInt()),    // Cian brillante
    SHOPPING(0xFF2196F3.toInt()),    // Azul primario saturado
    STUDY(0xFF4CAF50.toInt()),       // Verde vivo
    OTHER(0xFF9E9E9E.toInt()),       // Gris equilibrado
    ;

    companion object {
        fun fromOrdinal(ordinal: Int): Category? {
            return entries.find { it.ordinal == ordinal }
        }
    }
}