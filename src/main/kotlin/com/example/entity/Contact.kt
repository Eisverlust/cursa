package com.example.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

object ContactTable : IntIdTable("ContactTable") {
    val number: Column<String> = varchar("number", 50).uniqueIndex()
    var emp = reference("emp", EmpTable, ReferenceOption.CASCADE)
}

class Contact(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Contact>(ContactTable)

    var number by ContactTable.number
    var emp by Emp referencedOn ContactTable.emp
}

