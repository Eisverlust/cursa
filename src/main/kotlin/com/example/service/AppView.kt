package com.example.service

import com.example.entity.*
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

interface IRusName {
    fun rusName(): String
}

enum class STATUS : IRusName {
    Enable {
        override fun rusName(): String {
            return "Открытые"
        }
    },
    Complete {
        override fun rusName(): String {
            return "Завершена"
        }
    };

}

class AppView {
    fun showAllAppDONTMAGAZINA(): List<String> {
        return transaction {
            return@transaction ApplicationUser.find {
                ApplicationUserTable.id notInList (MagazineTable.selectAll().map { it[MagazineTable.applicationUser] })
            }.map {
                it.toString()
            }
        }
    }

    fun backApp(idApp: Int,fioAuthor:String){
        transaction {
            ApplicationUser.find {
                (ApplicationUserTable.id eq idApp) and (ApplicationUserTable.fio eq fioAuthor)
            }.map { it.delete() }
        }
    }

    fun showAcceptedApp(chatId: Long): Pair<String, LocalDateTime> {
        return transaction {
            val empID = Emp.findByUUID(chatId.toString())
            println(empID?.id)
            val app = Magazine.find {
                MagazineTable.emp eq empID!!.id
            }.with(
                Magazine::applicationUser,
                ApplicationUser::status,
                ApplicationUser::category,
                ApplicationUser::urgency
            ).maxByOrNull { it.date }
            println(app)
            println("--------------")
            return@transaction Pair(app?.applicationUser.toString() ?: "-1", app?.date ?: LocalDateTime.now())
        }
    }

    fun acceptApp(id: Int, emp: String) {
        transaction {
            val application = ApplicationUser.findById(id = id)
            Magazine.new {
                date = LocalDateTime.now()
                this.applicationUser = application!!
                this.emp = Emp.findByUUID(emp)!!
            }
        }
    }

    fun isApp(chatId: Long): Boolean {
        return transaction {
            val emp = Emp.findByUUID(chatId.toString())
            val res = Magazine.find { MagazineTable.emp eq emp!!.id }
                .map {
                    it.applicationUser.status.status == STATUS.Enable.rusName()
                }.all { it }
            return@transaction ((res == null) && (res))
        }
    }

    fun completeApp(ID: Int) {
        transaction {
            val app = ApplicationUser.find { ApplicationUserTable.id eq ID }.singleOrNull()
            app?.status = StatusApp.find { StatusAppTable.status eq STATUS.Complete.rusName() }.singleOrNull()
                ?: StatusApp.new {
                    status = STATUS.Complete.rusName()
                }
        }
    }

    fun cancel(ID: Int) {
        transaction {
            val app = ApplicationUser.find { ApplicationUserTable.id eq ID }.singleOrNull()
            app?.status = StatusApp.find { StatusAppTable.status eq STATUS.Enable.rusName() }.singleOrNull()
                ?: StatusApp.new {
                    status = STATUS.Enable.rusName()
                }
            Magazine.find {
                MagazineTable.applicationUser eq app!!.id
            }.map { it.delete() }
        }
    }

    fun commentary(chatId: Long, comment: String) {
        transaction {
            val em = Emp.findByUUID(chatId.toString())

            Comment.new {
                this.magazine = Magazine.find { MagazineTable.emp eq em!!.id }.maxByOrNull { it.date }!!
                this.comment = comment
                this.date = LocalDateTime.now()
            }
        }
    }

    fun muApplicationList(name: String): List<App> {
        return transaction {
            return@transaction ApplicationUser.find {
                ApplicationUserTable.fio eq name
            }.map {
                App(
                    it.id.value, it.text, it.address, it.status.status, it.category.category, it.urgency.urgency,
                    it.magazine.singleOrNull()?.comment?.map {v-> v.comment }.apply {
                        println(this)
                    }
                )
            }
        }
    }
}

data class App(
    val id: Int,
    val text: String,
    val address: String,
    val status: String,
    val category: String,
    val urgency: String,
    val listComment: List<String>? = null
)