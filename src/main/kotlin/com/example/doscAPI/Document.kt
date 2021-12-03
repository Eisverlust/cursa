package com.example.pf.doscAPI

import Body
import Style
import Table
import Title
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFTableCell
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.OutputStream


class Document(private var style: Style) {
    private val file = XWPFDocument()

    fun save(path: String = "C:/Users/nikit/Desktop/test.docx") {
        val out = FileOutputStream(path)
        file.write(out)
        out.close()
        file.close()
    }

    fun getOutput(): ByteArray {
        val out = ByteArrayOutputStream()
        file.write(out)
        file.close()
        val byte = out.toByteArray()
        out.close()
        return byte
    }

    fun title(styleTitle: Style = style, init: Title.() -> Unit): Title {
        val d = Title(file.createParagraph(), styleTitle)
        d.init()
        return d
    }

    fun body(style: Style = this.style, init: Body.() -> Unit): Body {
        val d = Body(file.createParagraph(), style)
        d.init()
        return d
    }

    fun table(rows:Int, cols:Int, style: Style = this.style, init: Table.()->Unit): Table {
        val t = Table(file.createTable(rows, cols), style)
        t.init()
        return t
    }


}

fun XWPFTableCell.title(styleTitle: Style, init: Title.() -> Unit): Title {
    val d = Title(this.addParagraph(), styleTitle)
    d.init()
    return d
}

fun  XWPFTableCell.body(style: Style, init: Body.() -> Unit): Body {
    val d = Body(this.addParagraph(), style)
    d.init()
    return d
}



fun document(style: Style = Style(), init: Document.() -> Unit): Document {
    val d = Document(style)
    d.init()
    return d
}