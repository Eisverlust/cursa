import org.apache.poi.xwpf.usermodel.ParagraphAlignment

open class Style (
   open val color:String="000000",
   open val bolt:Boolean=false,
   open val fontFamily:String="Times New Roman",
   open val fontSize:Int = 10,
   open val alignment: ParagraphAlignment = ParagraphAlignment.CENTER,
   open val italic:Boolean = false,
   open val spacing:Int = 1,
   open val spacingLine: Double = 1.5
)

data class StyleTable(val tableColor:String = "000000"):Style()