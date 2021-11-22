package com.example.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ApplictionUserTable : IntIdTable("ApplictionUserTable") {
    val text = varchar("text", 50)
    val fio = varchar("fio", 50)
    val time = datetime("time")
    val address = varchar("address", 100)
    val status = reference("status", StatusTable)
    val category = reference("category", CategoryTable)
    val urgency = reference("urgency", UrgencyTable)

}

class ApplictionUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ApplictionUser>(ApplictionUserTable)

    var text by ApplictionUserTable.text
    var fio by ApplictionUserTable.fio
    var time by ApplictionUserTable.time
    var address by ApplictionUserTable.address
    var status by Status referencedOn ApplictionUserTable.status
    var category by Category referencedOn ApplictionUserTable.category
    var urgency by Urgency referencedOn ApplictionUserTable.urgency
}