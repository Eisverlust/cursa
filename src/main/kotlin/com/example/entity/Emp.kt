package com.example.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object EmpTable : IntIdTable("EmpTable") {
    val fio = varchar("fio", 50)
    val uuid = varchar("uuid", 25).uniqueIndex()
    val group = reference("group", GroupTable, onDelete = ReferenceOption.CASCADE)
}

class Emp(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Emp>(EmpTable)

    var fio by EmpTable.fio
    var uuid by EmpTable.uuid
    var group by Group referencedOn EmpTable.group

    val contact by Contact referrersOn ContactTable.emp
}