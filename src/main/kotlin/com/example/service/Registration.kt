package com.example.service

import com.example.entity.Acc
import com.example.entity.AccTable
import com.example.entity.Roles
import com.example.entity.RolesTable
import com.example.plugins.ROLE
import org.jetbrains.exposed.sql.transactions.transaction

class Registration {
     fun registration(
         name:String,
         pass:String
     ){
         transaction {
             Acc.new {
                 this.name = name
                 this.password = pass
                 this.roles = Roles.find { RolesTable.role eq ROLE.USER.name }.singleOrNull() ?:Roles.new {
                     role = ROLE.USER.name
                 }
             }
         }
     }
}