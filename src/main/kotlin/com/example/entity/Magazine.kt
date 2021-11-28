package com.example.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object MagazineTable : IntIdTable("MagazineTable") {
    val date = datetime("date")
    val emp = reference("emp", EmpTable,onDelete = ReferenceOption.CASCADE)
    val applicationUser = reference("applicationUser", ApplicationUserTable, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    // добавить --- уже :D
init {
    uniqueIndex(emp,applicationUser)
}

}

class Magazine(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Magazine>(MagazineTable)
    var date by MagazineTable.date
    var emp by Emp referencedOn MagazineTable.emp
    var applicationUser by ApplicationUser referencedOn MagazineTable.applicationUser

    val comment by Comment referrersOn CommentTable.magazine
}