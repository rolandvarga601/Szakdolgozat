import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class GenerateAndReadQRCode {

	public static void main(String[] args) throws NotFoundException, FileNotFoundException, IOException, WriterException {
//		String qrCodeData = "1";
//	    String filePath = "qr_codes\\test_output\\probax.png";
//	    String charset = "UTF-8"; // or "ISO-8859-1"
//	    Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//	    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//	    QRCodeMethods.createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
//	    System.out.println("QR Code image created successfully!");
	    
//		System.out.println("Result text: " + QRCodeMethods.readQRCode("qr_codes\\test_input\\qr_code.png"));
		
		String filePath = "qr_codes\\test_input\\multiple.png";
		Result[] QRCodeResults = QRCodeMethods.readMultiQRCode(filePath);
		System.out.println(QRCodeResults.length);
		for (int i=0; i<QRCodeResults.length; i++) {
		    String item = QRCodeResults[i].getText();
		    System.out.println(i + ". item: " + item);
		    float[] centralCoords = ChessPieces.CalculateCentral(QRCodeResults[i].getResultPoints());
		    System.out.println("\t" + centralCoords[0] + ", " + centralCoords[1]);
		}
		
//		for (int i = 0; i < 5; i++) {
//			for (int j = 0; j < 8; j++) {
//				String filePath2 = "qr_codes\\test_input\\images-out\\";
//				String fileName = filePath2 + "image" + i + "_" + j + ".jpg";
//				System.out.println(fileName);
//				try {
//					String QRCodeResult = QRCodeMethods.readQRCode(fileName);
//					System.out.println(QRCodeResult);
//				} catch (NotFoundException ex) {
//					System.out.println("QR code was not found in this cell\n");
//				}
//			}
//		}
	}
}