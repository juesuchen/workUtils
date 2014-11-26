package workUtils.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.codec.binary.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class MatrixImageUtils {

    private static MultiFormatWriter formatWriter = new MultiFormatWriter();

    public static String getImageData(String content) throws WriterException, IOException {
        String returnStr = "data:image/png;base64,"
                + Base64.encodeBase64String(getImageDataBytes(content)).replaceAll("\r", "").replaceAll("\n", "");
        ;
        return returnStr;
    }

    public static byte[] getImageDataBytes(String content) throws WriterException, IOException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix byteMatrix = formatWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500, hints);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(byteMatrix, "PNG", out);

        return out.toByteArray();
    }
}
