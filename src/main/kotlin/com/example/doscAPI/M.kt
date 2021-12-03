package com.example.pf.doscAPI

import Style
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import kotlin.io.path.Path

fun createMeetingDocument(attended: List<String>, noAttended:List<String>, date: LocalDateTime?, numberVotes:Int, agenda: List<String?>, solution: List<String?>, access2:List<String>): ByteArray {

   val d = document {
        table(1, 3) {
            get(0, 0) {
                this.body(Style(alignment = ParagraphAlignment.CENTER, spacingLine = 1.0)) {
                    addText("") {
                        addPicture(
                            Files.newInputStream(Paths.get(javaClass.classLoader.getResource("static/image/img.png").toURI())),
                            XWPFDocument.PICTURE_TYPE_PNG,
                            "Эмблема профкома.JPG",
                            Units.toEMU(100.0), Units.toEMU(100.0)
                        )
                        addBreak()
                    }
                    addText("ФЕДЕРАЦИЯ ПРОФСОЮЗОВ ТАЙМЫРА", style = Style(bolt = true)) { addBreak() }
                    addText("Первичная профсоюзная организация ", style = Style(bolt = true)) { addBreak() }
                    addText("студентов ФГБОУ ВО «НГИИ»", style = Style(bolt = true)) { addBreak() }
                    addText("ул. 50 лет Октября, д.7, Норильск, 663310", style = Style(fontSize = 10)) { addBreak() }
                    addText("Телефон: 8 913 036 96 63;", style = Style(fontSize = 10)) { addBreak() }
                }

            }
            get(0, 2) {
                title(Style(alignment = ParagraphAlignment.CENTER, fontSize = 11, spacingLine = 1.0)) {
                    subTitle {
                        str =
                            "Протокол заседания"
                        addBreak()
                    }
                    subTitle { str = "Первичной профсоюзной организации"; addBreak() }
                    subTitle {
                        str =
                            " студентов от «${date?.dayOfMonth}» ${date?.monthValue} ${date?.year} г."; addBreak()
                    }
                    subTitle { str = "Председатель – Костенко Н.В.\n"; addBreak() }
                    subTitle { str = "Секретарь – Яшник И.Л.\n"; addBreak() }

                }
            }
            get(0, 1) {
                addParagraph().createRun().addTab()
            }
        }
        body {
            addText(""){addBreak()}
        }
        table(1, 3) {
            get(0, 0) {
                this@get.body(Style(alignment = ParagraphAlignment.LEFT,fontSize = 12)) {
                    addText("Присутствовали:",style = Style(bolt = true,fontSize = 12)) { addBreak() }
                    attended.forEachIndexed { i, s ->
                        addText("   ${i+1}.${s}"){addBreak()}
                    }
                }
            }
            get(0, 2) {
                this@get.body(Style(alignment = ParagraphAlignment.RIGHT,fontSize = 12)){
                    addText("Отсутствовали:", style = Style(bolt = true,fontSize = 12)) { addBreak(); }
                    noAttended.forEachIndexed { i, s ->
                        addText("   ${i+1}.${s}",){addBreak()}
                    }
                }
            }
            get(0,1){
                this@get.body(Style(alignment = ParagraphAlignment.RIGHT)){
                    addText(""){
                        addTab()
                        addTab()
                        addTab()
                        addTab()
                        addTab()
                    }
                }
            }
        }
        body(style = Style(alignment = ParagraphAlignment.LEFT,fontSize = 12)) {
            addText("Повестка дня:", style = Style(bolt = true, fontSize = 12)) { addBreak() }
            agenda.forEachIndexed { i, s->
                addText("   ${i+1}. $s"){addBreak()}
            }
        }
        body(style = Style(alignment = ParagraphAlignment.LEFT,fontSize = 12)) {
            addText("Решили:", style = Style(bolt = true, fontSize = 12)) { addBreak() }
            solution.forEachIndexed { i, s->
                addText("   ${i+1}. $s"){addBreak()}
            }
        }
        body(style = Style(alignment = ParagraphAlignment.LEFT,fontSize = 12)) {
            addText("Голосовали:", style = Style(bolt = true, fontSize = 12)) { addBreak() }
            addText("За: $numberVotes Против: ${(attended.size+noAttended.size)-numberVotes }")
        }
        table(1, 3) {
            get(0, 0) {
                this@get.body(Style(alignment = ParagraphAlignment.LEFT, fontSize = 12)) {
                    addText("Председатель ППОС ЗГУ", style = Style(bolt = false, fontSize = 12)) { addBreak() }

                }
            }
            get(0, 2) {
                this@get.body(Style(alignment = ParagraphAlignment.RIGHT, fontSize = 12)) {
                    addText("______Костенко Н.В.", style = Style(bolt = false, fontSize = 12)) { addBreak() }

                }
            }
            get(0, 0) {
                this@get.body(Style(alignment = ParagraphAlignment.LEFT, fontSize = 12)) {
                    addText("Секретарь ППОС ЗГУ", style = Style(bolt = false, fontSize = 12)) { addBreak(); }

                }
                get(0, 2) {
                    this@get.body(Style(alignment = ParagraphAlignment.RIGHT, fontSize = 12)) {
                        addText("______Яшник И.Л.", style = Style(bolt = false, fontSize = 12)) { addBreak() }

                    }
                }
                get(0, 1) {
                    this@get.body(Style(alignment = ParagraphAlignment.LEFT)) {
                        addText("") {
                            addTab()
                            addTab()
                            addTab()
                            addTab()
                            addTab()
                        }
                    }
                }
            }
        }
    }
    return  d.getOutput()
}

fun main(){
    createMeetingDocument(listOf("Ivan","Test"), listOf("Lex"), LocalDateTime.now(),4, listOf("Утверждение даты на сбор вещей в благотворительный проект «Сундук»."), listOf("В ходе обсуждения было принято много важных решений"), listOf("ЗА - 15 человек, ПРОТИВ - 0 человек, ВОЗДЕРЖАЛИСЬ - 0 человек"))
}
