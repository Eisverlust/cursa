package com.example.service

import com.example.entity.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class AppAdd {
    fun addApp(
        text: String,
        fio: String,
        time: LocalDateTime,
        address: String,
        status: String,
        category: String,
        urgency: String
    ) {
        transaction {
            ApplictionUser.new {
                this.text = text
                this.fio = fio
                this.time = time
                this.address = address
                // я конченый ... мда это же рили подтягивать из таблиц надо
                this.status = Status.find { StatusTable.status eq status }.singleOrNull() ?: Status.new {
                    this.status = status
                }
                this.category = Category.find { CategoryTable.category eq category }.singleOrNull() ?: Category.new {
                    this.category = category
                }
                this.urgency = Urgency.find { UrgencyTable.urgency eq urgency }.singleOrNull() ?: Urgency.new {
                    this.urgency = urgency
                }

            }
        }
    }
}