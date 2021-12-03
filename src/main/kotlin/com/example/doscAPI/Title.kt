import com.example.pf.doscAPI.Img
import org.apache.poi.xwpf.usermodel.XWPFParagraph


class Title(private val paragraph: XWPFParagraph, private val style: Style) : IText {

    /**
     * Установить текст строки
     * */
    fun text(style: Style = this.style,str: () -> String) {
        val run = paragraph.createRun()
        super.text(run,style,str)
    }

    /*
    * Создать подзаголовок
    * */
    fun subTitle(subTitleStyle: Style = style, init: SubTitle.() -> Unit): SubTitle {
        val d = SubTitle(paragraph.createRun(), subTitleStyle)
        d.init()
        return d
    }

    fun image(style: Style = this.style, init: Img.() -> Unit): Img {
        val i = Img(paragraph.createRun(),style)
        i.init()
        return i
    }


    init {
        paragraph.spacingBetween = style.spacingLine
        paragraph.alignment = style.alignment
    }
}