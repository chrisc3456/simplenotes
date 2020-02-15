package com.simplenotes.notes.domain.models

data class Category(
    val id: Int,
    val name: String,
    val colour: Int
) {
    override fun toString(): String {
        return name
    }
}

