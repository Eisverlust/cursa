package com.example.service

import com.example.entity.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class EmpService() {
    fun addEmp(
        number: String,
        group: String,
        fio: String,
        uuid: String
    ) {
        transaction {
            val em = Emp.new {
                this.fio = fio
                this.uuid = uuid
                this.group = Group.find { GroupTable.group eq group }.singleOrNull() ?: Group.new {
                    this.group = group
                }
            }
            Contact.new {
                this.emp = em
                this.number = number
            }
        }
    }
    fun findTelegramID(id:String): String? {
        return transaction {
            return@transaction Emp.findByUUID(id)?.uuid
        }
    }
    fun allEmpLoad(): MutableSet<String> {
       return transaction {
           return@transaction Emp.all().map {it.uuid }.toMutableSet()
        }
    }
}

fun Emp.Companion.findByUUID(id: String) = Emp.find{ EmpTable.uuid eq id}.singleOrNull()