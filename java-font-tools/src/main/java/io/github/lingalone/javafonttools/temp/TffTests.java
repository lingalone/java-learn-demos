package io.github.lingalone.javafonttools.temp;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/13
 */

public class TffTests {

    public void doConvert(Map<String, String> params, Map<String, String> list) {
        String ttffile = params.get("font");
        String outputdir = params.get("output");

        outputdir = (outputdir.endsWith("/") == true) ? outputdir : outputdir + "/";

        int iconsize = 100;
        int fontsize = 80;

        File tfile = null;
        Font font = null;

        FileInputStream fi = null;

        BufferedImage image = null;
        Graphics2D graphics = null;

        Color fontcolor = Color.BLACK;

        if (params.containsKey("color") == true)
        {
            fontcolor = TffTests.hex2Rgb(params.get("color"));
        }

        if (params.containsKey("iconsize") == true)
        {
            iconsize = Integer.parseInt(params.get("iconsize"));
        }

        if (params.containsKey("fontsize") == true)
        {
            iconsize = Integer.parseInt(params.get("fontsize"));
        }

        try
        {
            tfile = new File(ttffile);
            fi = new FileInputStream(tfile);
            font = Font.createFont(Font.TRUETYPE_FONT, fi);
            font = font.deriveFont(Font.PLAIN, fontsize);

            image = new BufferedImage(iconsize, iconsize, BufferedImage.TYPE_4BYTE_ABGR);
            graphics = image.createGraphics();

            graphics.setFont(font);

            AffineTransform af = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(af, true, true);

//            Map<String, String> fontmap = loadFontMap(params.get("list"));
            Map<String, String> fontmap = list;
            Iterator<String> fontkey = fontmap.keySet().iterator();

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            while(fontkey.hasNext())
            {
                String fname = fontkey.next();
                String fvalue = fontmap.get(fname);

                System.out.println(">> exporting : " + fname);

                Rectangle2D rect = font.getStringBounds(fvalue, frc);
                System.out.println( rect.contains(255,255));
                System.out.println(rect.toString());
                System.out.println();
                System.out.println(rect.getMaxX());


                double fw = rect.getWidth();
                double fh = rect.getHeight();

                graphics.setComposite(AlphaComposite.Clear);
                graphics.fillRect(0, 0, iconsize, iconsize);

                graphics.setComposite(AlphaComposite.Src);
                graphics.setColor(fontcolor);
                graphics.drawString(fvalue, (int) (iconsize - fw) / 2, (int) (fh - (iconsize - fh) / 2));
                ImageIO.write(image, "PNG", new File(outputdir + fname + ".png"));
            }

            System.out.println(">> finish exporting");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (graphics != null)
                {
                    graphics.dispose();
                }
                graphics = null;

                image = null;

                if (fi != null)
                {
                    fi.close();
                }
                fi = null;
            }
            catch (Exception e)
            {

            }
        }
    }

    public static Color hex2Rgb(String colorStr){
        Color c = new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
        return c;
    }

    public Map<String, String> loadFontMap(String listfilename) {
        Map<String, String> fontmap = new HashMap<String, String>();

        File f = null;
        FileReader fr = null;
        BufferedReader br = null;

        String line = null;

        try
        {
            f = new File(listfilename);

            fr = new FileReader(f);
            br = new BufferedReader(fr);

            while( (line = br.readLine()) != null )
            {
                if (line.startsWith(";") == false && line.startsWith("//") == false) // comment
                {
                    int n = line.indexOf(":");
                    if (n > -1)
                    {
                        String name = line.substring(0, n).trim();
                        String value = line.substring(n+1).trim();

                        if (value.endsWith(",") == true)
                        {
                            value = value.substring(0, value.length()-1).trim();
                        }

                        if (name.startsWith("\"") == true && name.endsWith("\"") == true && name.length() > 2)
                        {
                            name = name.substring(1, name.length() - 1);
                        }

                        if (value.startsWith("\"") == true && value.endsWith("\"") == true && value.length() > 2)
                        {
                            value = value.substring(1, value.length() - 1);

                            if (value.startsWith("\\") == true && value.charAt(1) == 'u')
                            {
                                Integer code = Integer.parseInt(value.substring(2), 16);
                                char ch = Character.toChars(code)[0];
                                value = Character.toString(ch);
                            }
                        }

                        fontmap.put(name, value);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.err.println(">> Error while reading list file : " + listfilename);
        }
        finally
        {
            try
            {
                if (br != null)
                    br.close();
                br = null;

                if (fr != null)
                    fr.close();
                fr = null;
            }
            catch (Exception e)
            {

            }
        }

        return fontmap;
    }
    public static void main(String[] a) throws Exception {
        String wName = "C:\\Users\\owner\\Downloads\\tyc-num.woff";
        String tName = "C:\\Users\\owner\\Downloads\\tyc-num-self.ttf";
//        InputStream is = new FileInputStream(new File(fName));//TffTests.class.getResourceAsStream(fName);
//        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
//        System.out.println(font.getSize2D());
//        char[] array = "4".toCharArray();
//        System.out.println(array.length);
//        System.out.println(FontUtilities.isComplexText(array, 0, array.length));
        WoffConverter converter =new WoffConverter();
        byte[] bytes = converter.convertToTTFByteArray(new FileInputStream(new File(wName)));
        FileOutputStream stream = new FileOutputStream(new File(tName));
        stream.write(bytes);
        stream.close();
        Map<String, String> list = converter.getGlyphCode();
        Map<String, String> params = new HashMap<String, String>();
        params.put("font",tName);
        params.put("output","C:\\Users\\owner\\Desktop\\111");
//        params.put("list","C:\\Users\\owner\\Desktop\\num.txt");
        TffTests font = new TffTests();
        font.doConvert(params, list);


    }




//    @Test
    public void dddd() throws IOException, DataFormatException {
        String wName = "C:\\Users\\owner\\Downloads\\tyc-num.woff";
        String tName = "C:\\Users\\owner\\Downloads\\tyc-num-self.ttf";

        WoffConverter converter =new WoffConverter();
        byte[] bytes = converter.convertToTTFByteArray(new FileInputStream(new File(wName)));
        FileOutputStream stream = new FileOutputStream(new File(tName));
        stream.write(bytes);
        stream.close();
        Map<String, String> list = converter.getGlyphCode();
        Map<String, String> params = new HashMap<String, String>();
        params.put("font",tName);
        params.put("output","C:\\Users\\owner\\Desktop\\111");
//        params.put("list","C:\\Users\\owner\\Desktop\\num.txt");
        TffTests font = new TffTests();
        font.doConvert(params, list);
    }

}
