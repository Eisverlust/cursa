import com.example.pf.doscAPI.Img
import org.apache.poi.xwpf.usermodel.VerticalAlign
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun

class Body(private val b: XWPFParagraph, private val style: Style) : IText {

    fun addText(text: String, position:Int = 0 ,style: Style = this.style,init: XWPFRun.()->Unit = {}): XWPFRun {
        val rows = b.createRun()
        super.text(rows, style, text,position)
        rows.init()
        return rows
    }

    fun image(style: Style = this.style, init: Img.() -> Unit): Img {
        val i = Img(b.createRun(),style)
        i.init()
        return i
    }

    init {
        b.alignment = style.alignment
        b.spacingBetween = style.spacingLine
    }

}