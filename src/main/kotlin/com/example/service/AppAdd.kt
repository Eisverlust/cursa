package com.example.service

import com.example.entity.ApplictionUser
import com.example.entity.Urgency
import kotlinx.html.ADDRESS
import org.jetbrains.exposed.sql.javatime.Time
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class AppAdd {
    fun addApp(text:String,fio:String,time: LocalDateTime,address:String,status:String,category:String,urgency:String) {
        transaction {
            ApplictionUser.new {
                this.text = text
                this.fio = fio
                this.time = time
                this.address = address
         /*       this.status = status
                this.category = category
                this.urgency = urgency*/
                // я конченый ... мда это же рили подтягивать из таблиц надо
            }
        }
    }
}