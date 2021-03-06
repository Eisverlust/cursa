package com.example.entity

import com.example.entity.EmpTable.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UrgencyTable : IntIdTable("UrgencyTable") {
    val urgency = varchar("urgency", 50).uniqueIndex()
}

class Urgency(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Urgency>(UrgencyTable)

    var urgency by UrgencyTable.urgency
}