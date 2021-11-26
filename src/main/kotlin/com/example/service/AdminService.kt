package com.example.service

import com.example.entity.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

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

    fun listEmployer(): List<Employer> {
       return transaction {
           return@transaction Emp.all().map {
                Employer(it.id.value,it.fio,it.uuid,it.group.group,it.contact.map { it.number })
            }
        }
    }

    fun removeEmployer(employerID:Int){
        transaction {
            Emp.findById(employerID)?.delete()
        }
    }

    fun empPercent(employerID: Int): Double {
       return transaction {
           val res = Magazine.find { MagazineTable.emp eq employerID }.filter {
                it.applicationUser.status.status != STATUS.Complete.rusName()
            }.count()
           if (res == 0 ) 1.0
           else res.toDouble()
        }
    }
}



data class Employer(val id:Int,val fio:String,val uuid: String,val group: String, val phones:List<String>)