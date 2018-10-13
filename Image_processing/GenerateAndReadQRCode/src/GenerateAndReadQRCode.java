import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class GenerateAndReadQRCode {

	public static void main(String[] args) throws NotFoundException, FileNotFoundException, IOException, WriterException {
		String qrCodeData = "b_pawn";
	    String filePath = "qr_codes\\test_output\\proba1.png";
	    String charset = "UTF-8"; // or "ISO-8859-1"
	    Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
	    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

	    QRCodeMethods.createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
	    System.out.println("QR Code image created successfully!");
		System.out.println("Result text: " + QRCodeMethods.readQRCode("qr_codes\\test_input\\qr_code.png"));
	}
}