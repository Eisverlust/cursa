package com.example.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ApplicationUserTable : IntIdTable("ApplicationUserTable") {
    val text = varchar("text", 50)
    val fio = varchar("fio", 50)
    val phone = varchar("phone", 50)
    val time = datetime("time")
    val address = varchar("address", 100)
    val status = reference("status", StatusAppTable)
    val category = reference("category", CategoryTable)
    val urgency = reference("urgency", UrgencyTable)

}

class ApplicationUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ApplicationUser>(ApplicationUserTable)

    var text by ApplicationUserTable.text
    var fio by ApplicationUserTable.fio
    var phone by ApplicationUserTable.phone
    var time by ApplicationUserTable.time
    var address by ApplicationUserTable.address
    var status by StatusApp referencedOn ApplicationUserTable.status
    var category by Category referencedOn ApplicationUserTable.category
    var urgency by Urgency referencedOn ApplicationUserTable.urgency

    val magazine by Magazine referrersOn MagazineTable.applicationUser

    override fun toString(): String {
        return """
            👉🏻👉🏻👉🏻👉🏻 Заявка № ${id.value} 👈🏻👈🏻👈🏻👈🏻
            Описание = $text
            Фио = $fio
            Номер телефона = $phone          
            Дата = $time           
            Адрес = $address         
            Статус = ${status.status}         
            Категория = ${category.category}            
            Срочно = ${urgency.urgency}          
        """.trimIndent()

    }


}