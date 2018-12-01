package imgprocess;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.VideoCapture;

// import javafx.scene.image.Image;

public class CameraHandler {
	
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	
	public CameraHandler(int CameraNumber) {
		
		this.capture = new VideoCapture();
		
		// start the video capture
		this.capture.open(CameraNumber);
		
		// sets the resolution to HD
		boolean wset = capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1280);
		boolean hset = capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 720);
	}
	
	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link BufferedImage} to show
	 */
	private BufferedImage grabFrame()
	{
		// init everything
		BufferedImage imageToShow = null;
		Mat frame = new Mat();
		
		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);
				
				// if the frame is not empty, process it
				if (!frame.empty())
				{	
					// convert the Mat object (OpenCV) to BufferedImage (Java.awt)
					imageToShow = mat2BufferedImage(frame);
				}
				
			}
			catch (Exception e)
			{
				// log the (full) error
				System.err.print("ERROR");
				e.printStackTrace();
			}
		}
		
		return imageToShow;
	}
	
	/**
	 * Convert a Mat object (OpenCV) in the corresponding BufferedImage
	 * 
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link BufferedImage} to process
	 */
	private BufferedImage mat2BufferedImage(Mat frame)
	{
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Highgui.imencode(".png", frame, buffer);
		// build and return an BufferedImage created from the image encoded in the
		// buffer
		try {
			return ImageIO.read(new ByteArrayInputStream(buffer.toArray()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void CloseCamera() {
		// release the camera
		this.capture.release();
	}
}
