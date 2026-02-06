package com.whereto.app.repositories

import com.whereto.app.domain.Event
import com.whereto.app.domain.Place
import com.whereto.app.domain.Tag
import com.whereto.app.domain.TaggableType
import com.whereto.app.dtos.tags.TagRequest
import com.whereto.db.tables.Tags
import com.whereto.db.tables.Tags.taggableType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class TagRepository {
    fun findTagsForPlace(placeId: Int): List<Tag> = transaction {
        Tags.selectAll()
            .where {
                (Tags.taggableId eq placeId) and (Tags.taggableType eq TaggableType.Place.name)
            }
            .map {
                Tag(
                    id = it[Tags.id].value,
                    text = it[Tags.text],
                    taggableId = it[Tags.taggableId],
                    taggableType = it[Tags.taggableType],
                    createdAt = it[Tags.createdAt],
                    updatedAt = it[Tags.updatedAt]
                )
            }
    }

    fun findTagForPlaces(placeIds: List<Int>): List<Tag> = transaction {
        Tags.selectAll()
            .where { Tags.taggableId.inList(placeIds) and (Tags.taggableType eq TaggableType.Place.name) }
            .map {
                Tag(
                    id = it[Tags.id].value,
                    text = it[Tags.text],
                    taggableId = it[Tags.taggableId],
                    taggableType = it[Tags.taggableType],
                    createdAt = it[Tags.createdAt],
                    updatedAt = it[Tags.updatedAt]
                )
            }
    }

    fun create(tagParams: TagRequest, taggable: Any, newTaggableId: Int): Tag? = transaction {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val newTaggableType = when(taggable) {
            is Place, "Place" -> TaggableType.Place.name
            is Event, "Event" -> TaggableType.Event.name
            else -> throw IllegalArgumentException("Unknown taggable type: $taggableType")
        }
        Tags.insert {
            it[taggableId] = newTaggableId
            it[taggableType] = newTaggableType
            it[text] = tagParams.text
            it[createdAt] = now
            it[updatedAt] = now
        }

        findByIdAndType(taggableId = newTaggableId, taggableType = newTaggableType)
    }

    fun findByIdAndType(taggableId: Int, taggableType: String): Tag? = transaction {
        Tags.selectAll().where {
            (Tags.taggableId eq taggableId) and (Tags.taggableType eq taggableType)
        }.map { buildTagForRow(it) }.singleOrNull()
    }

    fun deleteByTaggable(taggableId: Int, taggableType: String) = transaction {
        Tags.deleteWhere { (Tags.taggableId eq id) and (Tags.taggableType eq taggableType) }
    }

    fun buildTagForRow(row: ResultRow): Tag {
        return Tag(
            id = row[Tags.id].value,
            text = row[Tags.text],
            taggableId = row[Tags.taggableId],
            taggableType = row[Tags.taggableType],
            createdAt = row[Tags.createdAt],
            updatedAt = row[Tags.updatedAt]
        )
    }
}