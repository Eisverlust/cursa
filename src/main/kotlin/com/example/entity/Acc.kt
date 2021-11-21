package com.example.entity

import com.example.entity.AccTable.uniqueIndex
import com.example.entity.RolesTable.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column


object AccTable : IntIdTable("AccTable") {
        val name: Column<String> = varchar("name", 50).uniqueIndex()
        val password: Column<String> = varchar("Pass", 255)
        val roles = reference("role", RolesTable)
}

class Acc(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<Acc>(AccTable)
        var name     by AccTable.name
        var password by AccTable.password
        var roles by Roles referencedOn AccTable.roles // связь один ко многим

}

//------------------------------------------------------------------------------------

object RolesTable : IntIdTable("RolesTable") {
        val role: Column<String> = varchar("role", 50).uniqueIndex()
}

class Roles(id: EntityID<Int>) : IntEntity(id){
        companion object : IntEntityClass<Roles>(RolesTable)
        var role     by RolesTable.role
}
