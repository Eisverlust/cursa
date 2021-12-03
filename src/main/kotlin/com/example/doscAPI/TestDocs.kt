package com.example.doscAPI

import Style
import com.example.pf.doscAPI.body
import com.example.pf.doscAPI.document
import com.example.pf.doscAPI.title
import com.example.service.AdminService
import com.example.service.EmpService
import com.example.service.Employer
import io.ktor.application.*
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import kotlin.math.abs

fun test(sizeApplication: Int, acceptApplication: Int, executionApplication:Int,listEmployer: List<Employer>): ByteArray {
    val service = AdminService()
   return document {
        table(1, 3) {
            get(0, 0) {
                this.body(Style(alignment = ParagraphAlignment.CENTER, spacingLine = 1.0)) {
                    addText("ФГБОУ ВО «ЗГУ имени Федоровского»", style = Style(bolt = true)) { addBreak() }
                    addText("ул. 50 лет Октября, д.7, Норильск, 663310", style = Style(fontSize = 10)) { addBreak() }
                    addText("Телефон: 8(123)-000-00-00;", style = Style(fontSize = 10)) { addBreak() }
                }

            }
            get(0, 2) {
                title(Style(alignment = ParagraphAlignment.CENTER, fontSize = 11, spacingLine = 1.0)) {
                    subTitle {
                        str =
                            "Отчет за ${LocalDateTime.now().dayOfMonth} ${LocalDateTime.now().month} ${LocalDateTime.now().year}"
                        addBreak()
                    }
                    subTitle { str = "Информационная Служба Быстрого Реагирования\n"; addBreak() }
                }
            }
            get(0, 1) {
                addParagraph().createRun().addTab()
            }
        }
       body {
           addText("Выполненная работа", style =  Style(fontSize = 14, bolt = true))
       }
       body(style = Style(alignment = ParagraphAlignment.LEFT)) {
           addText("Всего $sizeApplication заявок, из них $acceptApplication на обработке и $executionApplication выполненных ")
           {
               addBreak()
           }
           val x = if(abs(sizeApplication - executionApplication)>0) abs(sizeApplication - executionApplication) else 1
           addText("В обработке ${acceptApplication.toDouble() / x * 100}%") {
               addBreak()
           }
           addText("Завершено заявок ${executionApplication.toDouble() / sizeApplication * 100}%")
       }
       body {
           addText("Отчет по сотрудникам:", style =  Style(fontSize = 14, bolt = true))
       }
       body(style = Style(alignment = ParagraphAlignment.LEFT)) {
           listEmployer.forEachIndexed { index,employer ->
               val res = service.empPercentifnull(employer.id)?.let {
                   it/sizeApplication *100
               } ?: 0
               addText("${index + 1}] ${employer.fio} группы ${employer.group}, выполнено $res %"){addBreak();}
               addText("Контактные данные:"){addBreak()}
               employer.phones.forEach {
                   addText(it){addBreak()}
               }
               addText(""){
                   addBreak()
               }
           }
       }
    }.getOutput()
}