package com.simplenotes.notes.domain.models

import java.time.LocalDateTime

data class Note(
    val id: Int,
    val title: String?,
    val content: String,
    val createdDateTime: LocalDateTime,
    val category: Category?
)