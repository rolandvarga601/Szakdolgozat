import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 */

/**
 * @author Varga Roland
 *
 */
public class FindMove {
	
	private BufferedImage img2crop;
	
	private static int HorizontalCorners;
	
	private static int VerticalCorners;
	
	private static double[][] cornersCoords;
	
	private int[][] cellsToLookAt;
	
	
	public FindMove(BufferedImage calibrationImage, int horizontalCorners, int verticalCorners,
			int[][] whitePiecesAt) {
		this.cellsToLookAt = whitePiecesAt;
		this.HorizontalCorners = horizontalCorners;
		this.VerticalCorners = verticalCorners;
		FindCorners cornerFinder = new FindCorners(calibrationImage, horizontalCorners, verticalCorners);
		var CornerPoints = cornerFinder.findPoints();
		cornersCoords = new double[CornerPoints.height()][2];
		for (int i = 0; i < CornerPoints.height(); i++) 
		{
			cornersCoords[i][0] = CornerPoints.get(i, 0)[0];
			cornersCoords[i][1] = CornerPoints.get(i, 0)[1];
		}
	}
	
	private FindMove() {
		
	}
	
//	public int[] FindEndCoords(BufferedImage img) {
//		int[] endCoords = new int[2];
//		BufferedImage[][] images = CropImages(img);
//		
//		return endCoords;
//	}
	
	public int GetHorizontalCorners() {
		return this.HorizontalCorners;
	}
	
	public int GetVerticalCorners() {
		return this.VerticalCorners;
	}
	
	private BufferedImage[][] CropImages(BufferedImage img2crop) {
		
		// Number of rows (it will be always 8, this is just for the test)
		int k = 5;
		
		// Number of columns (it will be always 8, this is just for the test)
		int l = 8;
		
		BufferedImage[][] returnPics = new BufferedImage[k][l];
		int x, y, w, h;
		for (int i = 0; i < k; i++)
		{
			for (int j = 0; j < l; j++)
			{
				x = (int) cornersCoords[i*HorizontalCorners+j][0];
				y = (int) cornersCoords[i*HorizontalCorners+j][1];
				w = (int) Math.ceil(cornersCoords[(i+1)*HorizontalCorners+j+1][0] - 
						cornersCoords[i*HorizontalCorners+j][0]);
				h = (int) Math.ceil(cornersCoords[(i+1)*HorizontalCorners+j+1][1] - 
						cornersCoords[i*HorizontalCorners+j][1]);
				returnPics[i][j] = img2crop.getSubimage(x, y, w, h);
				// System.out.println(returnPics[i][j].getHeight() + "\t" + returnPics[i][j].getWidth());
				writeFile(returnPics[i][j], i, j);
			}
		}
		return returnPics;
	}
	
	private static void writeFile(BufferedImage bufferedImage, int i, int j){
	    // String PATH = "/images-out/";
	    String directoryName = "./images-out/";
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
