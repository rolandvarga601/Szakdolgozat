import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;


public class QRCodeMethods {
	/***
	 * 
	 * @param qrCodeData
	 * @param filePath
	 * @param charset
	 * @param hintMap
	 * @param qrCodeheight
	 * @param qrCodewidth
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void createQRCode(String qrCodeData, String filePath,
	    String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
	    throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(
	        new String(qrCodeData.getBytes(charset), charset),
	        BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight);
		ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix), "png", new File(filePath));
	}
	
	/**
	 * @param FileName
	 * 
	 * @return QR Code value 
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static String readQRCode(String FileName) throws FileNotFoundException, IOException, NotFoundException {
		BufferedImage image = getImage(FileName);
		BufferedImageLuminanceSource bils = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(bils));
		Result qrCodeResult = new MultiFormatReader().decode(bitmap);
	    return qrCodeResult.getText();
	}
	
	private static BufferedImage getImage(String FileName) {
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(FileName));
		} catch (IOException e) {
			System.out.println("Can't find/open file");
			return null;
		}
		return img;
	}
}
