package com.example.service

import com.example.entity.*
import org.jetbrains.exposed.sql.transactions.transaction

class AdminService {

    fun countApplication(): Double {
        return transaction {
            return@transaction ApplicationUser.all().count().toDouble()
        }
    }

    fun countExecuteApp(): Double {
        return transaction {
            return@transaction ApplicationUser.find {
                ApplicationUserTable.status eq StatusApp.find { StatusAppTable.status eq STATUS.Complete.rusName() }
                    .single().id
            }.count().toDouble()
        }
    }

    fun countAcceptApp(): Long {
        return transaction {
            val e = StatusApp.find { StatusAppTable.status eq STATUS.Enable.rusName() }
                .firstOrNull()?.id?.value ?: 0
            return@transaction Magazine.find {
                MagazineTable.applicationUser inList ApplicationUser.find {
                    ApplicationUserTable.status eq e
                }.map { it.id }
            }.count()
        }
    }


    fun listEmployer(): List<Employer> {
        return transaction {
            return@transaction Emp.all().map {
                Employer(it.id.value, it.fio, it.uuid, it.group.group, it.contact.map { it.number })
            }
        }
    }

    fun removeEmployer(employerID: Int) {
        transaction {
            Emp.findById(employerID)?.delete()
        }
    }

    fun empPercent(employerID: Int): Double {
        return transaction {
            val res = Magazine.find { MagazineTable.emp eq employerID }.filter {
                it.applicationUser.status.status == STATUS.Complete.rusName()
            }.count()
            if (res == 0) 1.0
            else res.toDouble()
        }
    }

    fun empPercentifnull(employerID: Int): Double? {
        return transaction {
            val res = Magazine.find { MagazineTable.emp eq employerID }.filter {
                it.applicationUser.status.status == STATUS.Complete.rusName()
            }.count()
            if (res == 0) return@transaction null
            else res.toDouble()
        }
    }
}


data class Employer(val id: Int, val fio: String, val uuid: String, val group: String, val phones: List<String>)