package com.example.entity

import com.example.entity.ContactTable.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object EmpTable : IntIdTable("EmpTable") {
    val fio = varchar("fio", 50)
    val uuid = uuid("uuid",).uniqueIndex()
    val group = reference("group",GroupTable)
}

class Emp(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Emp>(EmpTable)
    var fio     by EmpTable.fio
    var uuid    by EmpTable.uuid
    var group   by Group referencedOn EmpTable.group
}