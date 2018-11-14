import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;


public class HelloCV {
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("pic2calibrate.jpg"));
		} catch (IOException e) {
		}
		int verticalCorners = 6;
		int horizontalCorners = 9;
		FindCorners cornerFinder = new FindCorners(img, horizontalCorners, verticalCorners);
		var CornerPoints = cornerFinder.findPoints();
		double[][] pointCoordinates = new double[CornerPoints.height()][2];
		for (int i = 0; i < CornerPoints.height(); i++) 
		{
			pointCoordinates[i][0] = CornerPoints.get(i, 0)[0];
			pointCoordinates[i][1] = CornerPoints.get(i, 0)[1];
		}
		BufferedImage img2crop = null;
		try {
		    img2crop = ImageIO.read(new File("pic2divide.jpg"));
		} catch (IOException e) {
		}
		BufferedImage[][] pics = CropImages(img2crop, pointCoordinates, horizontalCorners, verticalCorners);
	}

	private static BufferedImage[][] CropImages(BufferedImage img2crop, double[][] pointCoordinates, 
			int horizontalCorners, int verticalCorners) {
		
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
				x = (int) pointCoordinates[i*horizontalCorners+j][0];
				y = (int) pointCoordinates[i*horizontalCorners+j][1];
				w = (int) Math.ceil(pointCoordinates[(i+1)*horizontalCorners+j+1][0] - 
						pointCoordinates[i*horizontalCorners+j][0]);
				h = (int) Math.ceil(pointCoordinates[(i+1)*horizontalCorners+j+1][1] - 
						pointCoordinates[i*horizontalCorners+j][1]);
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
