package io.github.bruce0203.bsmeal1nfo

import java.awt.Color
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.io.File
import java.util.Properties
import javax.imageio.ImageIO


object AddTextToImg {
    @Throws(Exception::class)
    @JvmStatic
    fun execute(file: File, txt: String, out: File) {
        //read the image
        val image = ImageIO.read(file)
        //get the Graphics object
        val g = image.graphics
        //set font
        val prop = Properties()
        prop.load(File("config/drawing.properties").bufferedReader())
        g.color = hex2Rgb(prop["draw-color"].toString())
        g.font = getFont().deriveFont(prop["draw-font-size"].toString().toFloat())
        txt.split("\n").forEachIndexed { ind, str ->
            g.drawString(str,
                prop["draw-x"].toString().toInt(),
                prop["draw-y"].toString().toInt()
                    + ind * prop["draw-leading"].toString().toInt())
        }
        g.dispose()
        //write the image
        out.mkdirs()
        ImageIO.write(image, "png", out)
    }

    fun getFont(): Font {
        val font: Font = Font.createFont(Font.TRUETYPE_FONT, File("assets/font/font.ttf"))
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        ge.registerFont(font)
        return font
    }

    @JvmStatic
    fun hex2Rgb(colorStr: String): Color {
        var s = colorStr
        if (!s.startsWith("#")) s = "#$colorStr"
        return Color.getHSBColor(
            Integer.valueOf( s.substring( 1, 3 ), 16 ).toFloat(),
            Integer.valueOf( s.substring( 3, 5 ), 16 ).toFloat(),
            Integer.valueOf( s.substring( 5, 7 ), 16 ).toFloat()
        )
    }

}