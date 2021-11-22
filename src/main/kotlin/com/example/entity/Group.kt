package com.example.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object GroupTable : IntIdTable("GroupTable") {
    val group: Column<String> = varchar("group", 50).uniqueIndex()
}

class Group(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Group>(GroupTable)

    var group by GroupTable.group
}

