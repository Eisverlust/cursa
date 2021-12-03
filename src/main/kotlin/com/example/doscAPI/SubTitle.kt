import org.apache.poi.xwpf.usermodel.XWPFRun

class SubTitle(private val rows: XWPFRun,private val style: Style):IText {
    var str = ""
    set(v:String){
        field = v
        super.text(rows,style,field,1)
    }

    fun addBreak(rows: XWPFRun = this.rows){
        rows.addBreak()
    }

    fun native(init:XWPFRun.()->Unit) {
        rows.init()
    }

}