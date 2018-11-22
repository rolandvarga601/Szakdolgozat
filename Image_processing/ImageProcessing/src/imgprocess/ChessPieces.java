package imgprocess;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class ChessPieces {
	
	/**
	 * @throws IOException
	 * @throws WriterException
	 */
	public static void GeneratePiecesQRCode() throws IOException, WriterException{
		for (int i=0; i<Pieces.values().length; i++) {
			String qrCodeData = Pieces.values()[i].toString();
		    String filePath = "qr_codes\\test_output\\" + qrCodeData + ".png";
		    String charset = "UTF-8"; // or "ISO-8859-1"
		    Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		    QRCodeMethods.createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
		}
	}
	
	public static float[] CalculateCentral(ResultPoint[] points) {
		float xValue = (points[0].getX() + points[2].getX())/2;
		float yValue = (points[0].getY() + points[2].getY())/2;
		return new float[] {xValue, yValue};
	}
	
	public static enum Pieces{
		w_king, w_queen, w_rook, w_knight, w_bishop, w_pawn,
		b_king, b_queen, b_rook, b_knight, b_bishop, b_pawn;
	}
}
