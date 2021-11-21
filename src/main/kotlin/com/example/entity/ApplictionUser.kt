package com.example.entity
import org.jetbrains.exposed.sql.javatime.*
import com.example.entity.EmpTable.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ApplictionUserTable : IntIdTable("ApplictionUserTable") {
    val text = varchar("text", 50)
    val fio = varchar("fio", 50)
    val time = datetime("time")
    val address =  varchar("address", 100)

}

class ApplictionUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ApplictionUser>(ApplictionUserTable)
    var text    by ApplictionUserTable.text
    val fio by ApplictionUserTable.fio
    val time by ApplictionUserTable.time
    val address by ApplictionUserTable.address
}