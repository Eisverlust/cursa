import org.apache.poi.xwpf.usermodel.XWPFTable
import org.apache.poi.xwpf.usermodel.XWPFTableCell
import java.awt.Color

class Table(private val table: XWPFTable, style: Style,border:Boolean = false) {

    fun get(row: Int, column: Int, init: XWPFTableCell.() -> Unit) {
        val e = table.getRow(row).getCell(column)
        e.init()
    }

    fun addBottomBorder(type:XWPFTable.XWPFBorderType, size:Int, space:Int,color:String){
        table.setBottomBorder(type,size,space,color)
    }

    init {
        if (!border)
            table.removeBorders()
    }
}