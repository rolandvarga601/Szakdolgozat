import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// import com.google.zxing.BufferedImageLuminanceSource;

public class FindQRCodeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello, World");
//		ReadingImage ri = new ReadingImage();
		BufferedImage image = getImage("picture.png");
		System.out.println(image.getWidth() + "x" + image.getHeight());
//		BufferedImageLuminanceSource bils = new BufferedImageLuminanceSource(bufferedImage);
//		HybridBinarizer hb = new HybridBinarizer(bils);
	}

	private static BufferedImage getImage(String FileName) {
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("qr_code.png"));
		} catch (IOException e) {
			System.out.println("Can't find/open file");
		}
		return img;
	}

}