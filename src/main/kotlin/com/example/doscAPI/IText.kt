import org.apache.poi.xwpf.usermodel.XWPFRun


interface IText {

    fun text(titleRun: XWPFRun, style: Style ,str:()->String){
        text(titleRun,style,str())
    }

    fun text(titleRun: XWPFRun, style: Style ,text:String,position:Int=0){
        titleRun.setText(text)
        titleRun.fontFamily = style.fontFamily
        titleRun.color = style.color
        titleRun.fontSize = style.fontSize
        titleRun.isBold = style.bolt
        titleRun.isItalic = style.italic
        titleRun.characterSpacing = style.spacing
        titleRun.textPosition = position
    }
}