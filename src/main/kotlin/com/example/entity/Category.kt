package com.example.entity

import com.example.entity.StatusTable.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object CategoryTable : IntIdTable("CategoryTable") {
    val category: Column<String> = varchar("category", 50).uniqueIndex()
}

class Category(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Category>(CategoryTable)
    var category   by CategoryTable.category
}