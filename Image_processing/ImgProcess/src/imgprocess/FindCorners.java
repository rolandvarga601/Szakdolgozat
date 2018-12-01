package imgprocess;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

// import javafx.scene.image.Image;

public class FindCorners {
	
	private double[][] pointCoordinates;
	private MatOfPoint2f imageCorners;
	private int numCornersHor;
	private int numCornersVer;
	private BufferedImage camStream;
	private Mat frame;
	
	public FindCorners(BufferedImage image, int NumCornersHor, int NumCornersVer) {
		this.camStream = image;
		try 
		{
			this.frame = BufferedImage2Mat(image);
		} 
		catch (IOException e) {
			// TODO: handle exception
		}
		this.numCornersHor = NumCornersHor;
		this.numCornersVer = NumCornersVer;
		this.imageCorners = new MatOfPoint2f();
		this.pointCoordinates = findPoints();
	}
	
	private double[][] findPoints() {

		// init
		Mat grayImage = new Mat();
		
		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);
		
		// the size of the chessboard
		Size boardSize = new Size(this.numCornersHor, this.numCornersVer);
		
		// look for the inner chessboard corners
		boolean found = Calib3d.findChessboardCorners(grayImage, boardSize, imageCorners,
				Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK);
		double[][] pointCoords = new double[imageCorners.height()][2];
		for (int i = 0; i < imageCorners.height(); i++) 
		{
			pointCoords[i][0] = imageCorners.get(i, 0)[0];
			pointCoords[i][1] = imageCorners.get(i, 0)[1];
		}
		return pointCoords;
	}
	
	public double[][] GetPointCoordinates(){
		return this.pointCoordinates;
	}

//	private Image mat2Image(Mat frame)
//	{
//		// create a temporary buffer
//		MatOfByte buffer = new MatOfByte();
//		// encode the frame in the buffer, according to the PNG format
//		Imgcodecs.imencode(".png", frame, buffer);
//		// build and return an Image created from the image encoded in the
//		// buffer
//		return new Image(new ByteArrayInputStream(buffer.toArray()));
//	}
	
	private Mat BufferedImage2Mat(BufferedImage image) throws IOException {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ImageIO.write(image, "jpg", byteArrayOutputStream);
	    byteArrayOutputStream.flush();
	    return Highgui.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Highgui.CV_LOAD_IMAGE_UNCHANGED);
	}
}
