package com.example.entity

import com.example.entity.EmpTable.uniqueIndex
import com.github.kotlintelegrambot.entities.ChatId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object StatusAppTable: IntIdTable("StatusAppTable"){
    val status = varchar("status", 50).uniqueIndex()
}

class StatusApp(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StatusApp>(StatusAppTable)
    var status by StatusAppTable.status
}