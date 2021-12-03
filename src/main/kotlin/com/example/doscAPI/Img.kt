package com.example.pf.doscAPI

import Style
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFRun
import java.nio.file.Files
import kotlin.io.path.Path

class Img(private val rows: XWPFRun, private val style: Style) {

    fun setImage(
        path: String, width: Int = Units.toEMU(50.0),
        height: Int = Units.toEMU(100.0),
        type: Int = XWPFDocument.PICTURE_TYPE_PNG
    ) {
        rows.addPicture(
            Files.newInputStream(Path(path)),
            type,
            path,
            width,
            height
        )
    }
}