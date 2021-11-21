package com.example.entity

import com.example.entity.GroupTable.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object StatusTable : IntIdTable("StatusTable") {
    val status: Column<String> = varchar("status", 50).uniqueIndex()
}

class Status(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Status>(StatusTable)
    var status   by StatusTable.status
}

