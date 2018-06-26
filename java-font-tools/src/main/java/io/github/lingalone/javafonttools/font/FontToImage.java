package io.github.lingalone.javafonttools.font;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/26
 */

public class FontToImage {


    public static void saveFontImageSingle(FontImage fontImage) throws IOException, FontFormatException {

        File fontFile = Paths.get(fontImage.getFontFile()).toFile();
        if(fontFile.isFile()){
            Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontFile));
            font = font.deriveFont(Font.PLAIN, fontImage.getFontSize()!=null ? 80:fontImage.getFontSize());

            BufferedImage image = new BufferedImage(fontImage.getImgSize(), fontImage.getImgSize(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = image.createGraphics();
            graphics.setFont(font);

            AffineTransform affineTransform = new AffineTransform();
            FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (String fontKey : fontImage.getFontKeys()) {
                Rectangle2D rect = font.getStringBounds(fontKey, fontRenderContext);
                double fw = rect.getWidth();
                double fh = rect.getHeight();

                graphics.setComposite(AlphaComposite.Clear);
                graphics.fillRect(0, 0, fontImage.getImgSize(), fontImage.getImgSize());
                graphics.setComposite(AlphaComposite.Src);
                graphics.setColor(Color.BLACK);
                graphics.drawString(fontKey, (int) (fontImage.getImgSize() - fw) / 2, (int) (fh - (fontImage.getImgSize() - fh) / 2));

                File imageFile = Paths.get(fontImage.getSaveDir(), fontFile.getName(), fontKey+".png").toFile();
                if(imageFile.getParentFile().exists())
                    ImageIO.write(image, "PNG", imageFile);
                else{
                    if(imageFile.getParentFile().mkdir()){
                        ImageIO.write(image, "PNG", imageFile);
                    }
                }
            }
        }
    }


}
