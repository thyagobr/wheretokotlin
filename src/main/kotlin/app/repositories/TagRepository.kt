package com.whereto.app.repositories

import com.whereto.app.domain.Tag
import com.whereto.app.domain.TaggableType
import com.whereto.db.tables.Tags
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
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
}