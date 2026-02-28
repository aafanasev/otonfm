package net.afanasev.otonfm.util

object ProfanityFilter {
    private val bannedWords: Set<String> = setOf("абас", "хуй", "пизда", "нахуй", "блэт", "блядь")

    fun containsProfanity(text: String): Boolean {
        val words = text.lowercase().split(Regex("[^\\p{L}]+")).filter { it.isNotEmpty() }
        return words.any { it in bannedWords }
    }
}
