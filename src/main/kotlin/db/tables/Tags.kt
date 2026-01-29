package com.whereto.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object Tags : IntIdTable("tags") {
    val text = varchar("text", 100)
    val taggableId = integer("taggable_id")
    val taggableType = varchar("taggable_type", 255)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
