package imgprocess;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;

// import com.google.zxing.NotFoundException;

/**
 * 
 */

/**
 * @author Varga Roland
 *
 */
public class ScanPiecesPosition {
	
	private int HorizontalCorners;
	
	private int VerticalCorners;
	
	private double[][] cornersCoords;
	
	
	public ScanPiecesPosition(BufferedImage calibrationImage, int horizontalCorners, int verticalCorners) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.HorizontalCorners = horizontalCorners;
		this.VerticalCorners = verticalCorners;
		FindCorners cornerFinder = new FindCorners(calibrationImage, horizontalCorners, verticalCorners);
		cornersCoords = cornerFinder.GetPointCoordinates();
	}
	
	private ScanPiecesPosition() {
		
	}
	
	/*public int[] FindEndCoords(BufferedImage img, boolean[][] whiteWasThere) {
		int[] endCoords = new int[2];
		BufferedImage[][] images = CropImages(img);
		boolean findEndPoint = false;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!whiteWasThere[i][j]) {
					try {
						String readString = QRCodeMethods.readQRCode(images[i][j]);
						System.out.println("Found a new QR code, this could be an end point: " + i + "x" + j);
						System.out.println("\t\tChess piece: " + readString);
						findEndPoint = true;
						endCoords[0] = i;
						endCoords[1] = j;
					} catch (NotFoundException e) {
						System.out.println("Did not found QR code at: " + i + "x" + j);
					}
				}
			}
		}
		return endCoords;
	}*/
	
	public boolean[][] ScanPositions(BufferedImage img) {
		return FindGreen(CropImages(img, this.VerticalCorners - 1, this.HorizontalCorners - 1));
	}
	
	private boolean[][] FindGreen(BufferedImage[][] images) {
//		boolean[][] greenMap = new boolean[this.VerticalCorners - 1][this.HorizontalCorners - 1];
//		for (int i = 0; i < VerticalCorners - 1; i++) {
//			for (int j = 0; j < HorizontalCorners - 1; j++) {
		boolean[][] greenMap = new boolean[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BufferedImage img = images[i][j];
				int black = 0;
				int white = 0;
				for (int k = 0; k < img.getHeight(); k++) {
					for (int k2 = 0; k2 < img.getWidth(); k2++) {
						Color pc = new Color(img.getRGB(k2, k));
						int rgb;
						float justGreen = ((float)pc.getGreen() - (float)pc.getRed()/2 - (float)pc.getBlue()/2);
						if (justGreen < 40) {
							rgb = Color.BLACK.getRGB();
							black++;
						} else {
							rgb = Color.WHITE.getRGB();
							white++;
						}
						img.setRGB(k2, k, rgb);
					}
				}
				double average = ((double)white) / ((double)black + (double)white);
//				System.out.println(average);
				if (average > 0.1) {
					greenMap[i][j] = true;
				} else {
					greenMap[i][j] = false;
				}
				writeFile(img, i, j);
			}
		}
		return greenMap;
	}
	
	public int GetHorizontalCorners() {
		return this.HorizontalCorners;
	}
	
	public int GetVerticalCorners() {
		return this.VerticalCorners;
	}
	
	private BufferedImage[][] CropImages(BufferedImage img2crop, int rows, int columns) {
		
		// Number of rows (it will be always 8, this is just for the test)
		//int k = 5;
		
		// Number of columns (it will be always 8, this is just for the test)
		//int l = 8;
		
		BufferedImage[][] returnPics = new BufferedImage[8][8];
		int x, y, w, h;
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				x = (int) cornersCoords[i*HorizontalCorners+j][0];
				y = (int) cornersCoords[i*HorizontalCorners+j][1];
				w = (int) Math.ceil(cornersCoords[(i+1)*HorizontalCorners+j+1][0] - 
						cornersCoords[i*HorizontalCorners+j][0]);
				h = (int) Math.ceil(cornersCoords[(i+1)*HorizontalCorners+j+1][1] - 
						cornersCoords[i*HorizontalCorners+j][1]);
				System.out.println(i + "." + j + ". coordinates: " + x + "x" + y + "\t" + w + ", " + h);
				returnPics[i][j] = img2crop.getSubimage(x, y, w, h);
				// System.out.println(returnPics[i][j].getHeight() + "\t" + returnPics[i][j].getWidth());
				writeFile(returnPics[i][j], i, j);
			}
		}
		return returnPics;
	}
	
	public static void writeFile(BufferedImage bufferedImage, int i, int j){
	    // String PATH = "/images-out/";
	    String directoryName = "C:\\images\\images-out\\";
	    String fileName = "image" + i + "_" + j + ".jpg";

	    File directory = new File(directoryName);
	    if (! directory.exists()){
	        directory.mkdir();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }

	    File outputfile = new File(directoryName + "/" + fileName);
	    try{
	    	ImageIO.write(bufferedImage, "jpg", outputfile);
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.exit(-1);
	    }
	}
}
