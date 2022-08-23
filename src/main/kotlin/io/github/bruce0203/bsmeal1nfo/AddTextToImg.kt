package io.github.bruce0203.bsmeal1nfo

import java.awt.Color
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.io.File
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
        g.color = Color.BLACK
        g.font = getFont().deriveFont(110f)
        //display the text at the coordinates(x=50, y=150)
        txt.split("\n").forEachIndexed { ind, str ->
            g.drawString(str, 200, 350 + ind * 130)
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
}