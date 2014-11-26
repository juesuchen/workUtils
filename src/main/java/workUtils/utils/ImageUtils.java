package workUtils.utils;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * @ClassName: ImageUtils
 * @Description: TODO(ImageUtils.)
 * @author juesu.chen
 * @date Jul 22, 2014 3:47:59 PM
 * @version 1.0
 */
public class ImageUtils {
    /*
     * cut Center Image
     */
    public static void cutCenterImage(String src, String dest, int w, int h) throws IOException {
        String ext = src.substring(src.lastIndexOf(".") + 1);
        Iterator iterator = ImageIO.getImageReadersByFormatName(ext);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        int imageIndex = 0;
        Rectangle rect = new Rectangle((reader.getWidth(imageIndex) - w) / 2, (reader.getHeight(imageIndex) - h) / 2,
                w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, ext, new File(dest));

    }

    /**
     * 
     * @Title: getImageSize
     * @Description: TODO(simple description this method what to do.)
     * @author juesu.chen
     * @date Aug 13, 2014 2:44:04 PM
     * @param src
     * @return [width,height]
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int[] getImageSize(String src) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(src);
        BufferedImage sourceImg = ImageIO.read(in);
        int[] result = new int[] { sourceImg.getWidth(), sourceImg.getHeight() };
        in.close();
        return result;
    }

    /*
     * cut Half Image
     */
    public static void cutHalfImage(String src, String dest) throws IOException {
        String ext = src.substring(src.lastIndexOf(".") + 1);
        Iterator iterator = ImageIO.getImageReadersByFormatName(ext);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        int imageIndex = 0;
        int width = reader.getWidth(imageIndex) / 2;
        int height = reader.getHeight(imageIndex) / 2;
        Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, ext, new File(dest));
    }

    /*
     * cut Image
     */

    public static void cutImage(String src, String dest, int x, int y, int w, int h) throws IOException {
        String ext = src.substring(src.lastIndexOf(".") + 1);
        Iterator iterator = ImageIO.getImageReadersByFormatName(ext);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, ext, new File(dest));

    }

    /*
     * zoom Image
     */
    public static void zoomImage(String src, String dest, int w, int h) throws Exception {
        double wr = 0, hr = 0;
        File srcFile = new File(src);
        File destFile = new File(dest);
        BufferedImage bufImg = ImageIO.read(srcFile);
        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);
        wr = w * 1.0 / bufImg.getWidth();
        hr = h * 1.0 / bufImg.getHeight();
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
        try {
            ImageIO.write((BufferedImage) Itemp, dest.substring(dest.lastIndexOf(".") + 1), destFile);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}
