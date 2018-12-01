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
		try {
		    img = ImageIO.read(new File("./images/calibration.jpg"));
		} catch (IOException e) {
		}
		int verticalCorners = 4;
		int horizontalCorners = 6;
		FindCorners cornerFinder = new FindCorners(img, horizontalCorners, verticalCorners);
		double[][] pointCoordinates = cornerFinder.GetPointCoordinates();
		BufferedImage imgbefore = null;
		try {
		    imgbefore = ImageIO.read(new File("./images/beforemove.jpg"));
		} catch (IOException e) {
		}
		BufferedImage imgafter = null;
		try {
			imgafter = ImageIO.read(new File("./images/aftermove.jpg"));
		} catch (IOException e) {
		}
		ScanPiecesPosition pp = new ScanPiecesPosition(img, horizontalCorners, verticalCorners);
		boolean[][] PiecesAtBefore = pp.ScanPositions(imgbefore);
		boolean[][] PiecesAtAfter = pp.ScanPositions(imgafter);
		for (int i = 0; i < verticalCorners - 1; i++) {
			for (int j = 0; j < horizontalCorners - 1; j++) {
				System.out.print(PiecesAtBefore[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < verticalCorners - 1; i++) {
			for (int j = 0; j < horizontalCorners - 1; j++) {
				System.out.print(PiecesAtAfter[i][j] + "\t");
			}
			System.out.println();
		}
	}

}
