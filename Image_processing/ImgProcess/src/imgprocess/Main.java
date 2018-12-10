package imgprocess;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.opencv.core.Core;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BufferedImage img = null;
		String libraryPath = "C:\\Users\\vargaro\\Documents\\general\\szakdolgozat\\Szakdolgozat\\TestImages\\";
		String fileName = "";
		try {
			fileName = "finaltest-original.jpg";
		    img = ImageIO.read(new File(libraryPath + fileName));
		} catch (IOException e) {
		}
		int verticalCorners = 9;
		int horizontalCorners = 10;
		img = CameraHandler.rotateClockwise90(img);
		FindCorners cornerFinder = new FindCorners(img, horizontalCorners, verticalCorners);
		double[][] pointCoordinates = cornerFinder.GetPointCoordinates();
		
/*		try {
			fileName = "final0.jpg";
		    img = ImageIO.read(new File(libraryPath + fileName));
		} catch (IOException e) {
		}
		verticalCorners = 9;
		horizontalCorners = 10;
		img = CameraHandler.rotateClockwise90(img);
		FindCorners cornerFinder2 = new FindCorners(img, horizontalCorners, verticalCorners);
		double[][] pointCoordinates2 = cornerFinder2.GetPointCoordinates();
		System.out.println("Number of points: " + pointCoordinates2.length);*/
		
		System.out.println(pointCoordinates.length);
/*		BufferedImage imgbefore = null;
		try {
			fileName = "finaltest.jpg";
		    imgbefore = ImageIO.read(new File(libraryPath + fileName));
		    imgbefore = CameraHandler.rotateClockwise90(imgbefore);
		} catch (IOException e) {
		}
		BufferedImage imgafter = null;
		try {
			fileName = "final2.jpg";
			imgafter = ImageIO.read(new File(libraryPath + fileName));
			imgafter = CameraHandler.rotateClockwise90(imgafter);
		} catch (IOException e) {
		}
		ScanPiecesPosition pp = new ScanPiecesPosition(pointCoordinates, horizontalCorners, verticalCorners);
		boolean[][] PiecesAtBefore = pp.ScanPositions(imgbefore);
		boolean[][] PiecesAtAfter = pp.ScanPositions(imgafter);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(PiecesAtBefore[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(PiecesAtAfter[i][j] + "\t");
			}
			System.out.println();
		}*/
	}

}
