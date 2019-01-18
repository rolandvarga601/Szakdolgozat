package application;


import imgprocess.CameraHandler;
import imgprocess.FindCorners;
import imgprocess.ScanPiecesPosition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.kuka.common.FileUtil;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ObserverManager;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see UseRoboticsAPIContext
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class ChessOffice extends RoboticsAPIApplication {
	@Inject
	private LBR lBR_iiwa_7_R800_1;
	
	@Inject
	@Named("gripper")
	private Tool gripperTool;
	
	private ObserverManager observerManager;
	
	// Frames for positioning
	private ObjectFrame gripperTCP;
	private Frame[][] chessFrames;
	private Frame[][] chessFramesAbove;
	private Frame chessBase;
	
	// Arrays to hold the current and previous position
	// of the chess pieces
	private boolean[][] whitePiecesAtBefore;
	private boolean[][] whitePiecesAtAfter;
	
	// The path of OpenCV libraries
	private String OpenCVHome;
	
	// The webcam handler
	private CameraHandler cam;
	
	@Override
	public void initialize() {
		
		observerManager = new ObserverManager();
		
		// Creating a new chessbase frame with the same coordinates
		// but with different orientation
		chessBase = getApplicationData().getFrame("/base").copy();
		chessBase.setX(getApplicationData().getFrame("/chessbase").getX());
		chessBase.setY(getApplicationData().getFrame("/chessbase").getY());
		chessBase.setZ(getApplicationData().getFrame("/chessbase").getZ());
		
		// Attaches gripper to the flange
		gripperTool.attachTo(lBR_iiwa_7_R800_1.getFlange());
		
		// Gets the offset of the gripper TCP relative to flange
		gripperTCP = gripperTool.getFrame("/gripperTCPOut");
		double gripperTCPOffsetX = gripperTool.getFrame("/gripperTCPOut").getX();
		double gripperTCPOffsetY = gripperTool.getFrame("/gripperTCPOut").getY();
		
		// Initializes arrays
		whitePiecesAtBefore = new boolean[8][8];
		whitePiecesAtAfter = new boolean[8][8];
		chessFrames = new Frame[8][8];
		chessFramesAbove = new Frame[8][8];
		
		// Generating field coordinates
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Frame chessPoint = chessBase.copy();
				
				// Coordinate of the field
				// (compensating with the offset from centerline)
				chessPoint.setX(chessPoint.getX()-j*40+gripperTCPOffsetX);
				chessPoint.setY(chessPoint.getY()-20-(7-i)*40+gripperTCPOffsetY);
				
				// Sets z coordinate to grabbing height
				chessPoint.setZ(chessPoint.getZ()+35);
				
				// Generates the approaching position
				chessFrames[i][j] = chessPoint.copy();				
				chessPoint.setZ(chessPoint.getZ()+50);
				chessFramesAbove[i][j] = chessPoint.copy();
				
				// Moves to the saved positions for test
//				gripperTCP.move(ptp(chessFramesAbove[i][j]));
//				gripperTCP.move(ptp(chessFrames[i][j]));
			}
		}
		
		// Configuring the OpenCV dll
		ConfigureOpenCV();
		
		// Opening the webcam
		cam = new CameraHandler(0);
	}

	@Override
	public void run() {
		// Moves to home position
//		lBR_iiwa_7_R800_1.move(ptpHome());
		
		BufferedImage img;
		// Take a picture
		img = cam.grabFrame();
		
		// Determining the position of the white pieces
		String libraryPath = "C:\\images\\";
		String fileName = "";
		BufferedImage calibrationImg;
		try {
			fileName = "finaltest.jpg";
			calibrationImg = ImageIO.read(new File(libraryPath + fileName));
			calibrationImg = CameraHandler.rotate90(calibrationImg);
			int verticalCorners = 9;
			int horizontalCorners = 10;
//			FindCorners cornerFinder = new FindCorners(calibrationImg, horizontalCorners, verticalCorners);
//			double[][] pointCoordinates = cornerFinder.GetPointCoordinates();
			ScanPiecesPosition spp = new ScanPiecesPosition(calibrationImg,
					horizontalCorners, verticalCorners);
		} catch (IOException e) {
		}
	}
	
	@Override
	public void dispose() {
		// This part will still run in case of error
		
	}
	
	private void ConfigureOpenCV() {
		this.OpenCVHome = "C://opencv//build//java//x86";
		/*
		File theDir = new File(this.OpenCVHome + "//temporary");

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		    }
		    catch(SecurityException se){
		        System.out.println("Cannot create temporary folder for OpenCV");
		    }
		}
		
		// Deletes temporary OpenCV dll
		File dll2delete = new File(this.OpenCVHome + "//temporary" + Core.NATIVE_LIBRARY_NAME + ".dll");
		if (!dll2delete.exists()) {
			boolean result = FileUtil.deleteDirectory(theDir);
			System.out.println("Result: " + result);
		}
		
		// Copies the dll to the temporary folder
		try {
			File source = new File(this.OpenCVHome + "//" + Core.NATIVE_LIBRARY_NAME + ".dll");
			System.out.println(this.OpenCVHome + "//" + Core.NATIVE_LIBRARY_NAME + ".dll");
			File destination = new File(theDir.getAbsolutePath() + "//" + Core.NATIVE_LIBRARY_NAME + ".dll");
			FileUtil.copyFile(source, destination);
		} catch (IOException e) {
			System.out.println("OpenCV was already loaded");
		}*/
		/*
		// Adding the dll to the library path
		String javaLibPath = System.getProperty("java.library.path");
		String libraryPath = this.OpenCVHome + "//temporary";
		if (!javaLibPath.contains(libraryPath)){
			try{
				// OpenCV temporary library added to
				System.out.println("Adding OpenCV temporary folder to library path");
				
				// set the path
			    System.setProperty("java.library.path", libraryPath);
			    Field sysPath;
				try {
					ClassLoader loader = ClassLoader.class.getClassLoader();
					sysPath = ClassLoader.class.getDeclaredField("sys_paths");
					sysPath.setAccessible(true);
				    try {
						sysPath.set(null, null);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch(UnsatisfiedLinkError e){
				System.out.println(e.toString());
			}
		}
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Loaded OpenCV");*/
		
		// Adding the dll to the library path
		String javaLibPath = System.getProperty("java.library.path");
		String libraryPath = this.OpenCVHome;
		if (!javaLibPath.contains(libraryPath)){
			try{
				// OpenCV temporary library added to
				System.out.println("Adding OpenCV folder to library path");
				
				// set the path
			    System.setProperty("java.library.path", libraryPath);
			    Field sysPath;
				try {
					ClassLoader loader = ClassLoader.class.getClassLoader();
					sysPath = ClassLoader.class.getDeclaredField("sys_paths");
					sysPath.setAccessible(true);
				    try {
						sysPath.set(null, null);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch(UnsatisfiedLinkError e){
				System.out.println(e.toString());
			}
		}
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Loaded OpenCV dll in main..");
	}
}