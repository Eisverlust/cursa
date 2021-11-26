package com.example.entity

import com.example.entity.EmpTable.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object CommentTable : IntIdTable("CommentTable") {
    val date = datetime("date")
    val comment = text("comment").uniqueIndex()
    val magazine = reference("magazine", MagazineTable, onDelete = ReferenceOption.CASCADE)
}

class Comment(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Comment>(CommentTable)
    var date by CommentTable.date
    var comment by CommentTable.comment
    var magazine by Magazine referencedOn CommentTable.magazine

}