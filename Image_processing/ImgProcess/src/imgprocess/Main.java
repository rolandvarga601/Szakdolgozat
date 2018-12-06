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
			fileName = "calibration21.jpg";
		    img = ImageIO.read(new File(libraryPath + fileName));
		} catch (IOException e) {
		}
		int verticalCorners = 9;
		int horizontalCorners = 10;
		FindCorners cornerFinder = new FindCorners(img, horizontalCorners, verticalCorners);
		double[][] pointCoordinates = cornerFinder.GetPointCoordinates();
		System.out.println(pointCoordinates.length);
		BufferedImage imgbefore = null;
		try {
			fileName = "beginning.jpg";
		    imgbefore = ImageIO.read(new File(libraryPath + fileName));
		} catch (IOException e) {
		}
		BufferedImage imgafter = null;
		try {
			fileName = "white1.jpg";
			imgafter = ImageIO.read(new File(libraryPath + fileName));
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
		}
	}

}
