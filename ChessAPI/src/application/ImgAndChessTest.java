package application;

import imgprocess.CameraHandler;
import imgprocess.FindCorners;
import imgprocess.ScanPiecesPosition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import chess.core.ChessGame;
import chess.core.Move;

import com.kuka.generated.ioAccess.GripperControlIOGroup;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ObserverManager;

import com.kuka.roboticsAPI.conditionModel.BooleanIOCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.deviceModel.LBRE1Redundancy;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.ioModel.Input;
import com.kuka.roboticsAPI.motionModel.LIN;
import com.kuka.roboticsAPI.motionModel.PTP;
import com.kuka.roboticsAPI.motionModel.Spline;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

public class ImgAndChessTest extends RoboticsAPIApplication {
	@Inject
	private LBR lbr;
	
	private Controller kuka_Sunrise_Cabinet_1;
	private ObserverManager observerManager;
	private GripperControlIOGroup gripperDevice;
	
	@Inject
	@Named("gripper")
	private Tool Gripper;
	private ObjectFrame gripperTCP;
	private Frame[][] chessFrames;
	private Frame[][] chessFramesAbove;
	private Frame chessBase;

	final static double radiusOfCircMove=120;
	final static int nullSpaceAngle = 80;

	final static double offsetAxis2And4=Math.toRadians(20);
	final static double offsetAxis4And6=Math.toRadians(-40);
	double[] loopCenterPosition= new double[]{
			0, offsetAxis2And4, 0, offsetAxis2And4 +offsetAxis4And6 -Math.toRadians(90), 0, offsetAxis4And6,Math.toRadians(90)};


	private final static String informationText=
			"This application is intended for floor mounted robots!"+ "\n" +
			"\n" +
			"The robot moves to the start position and based on this position, a motion that " +
			"describes the symbol of lemniscate (a 'horizontal eight') will be executed." + "\n" +
			"In a next step the robot will move in nullspace by "+nullSpaceAngle+"? in both directions.";
	
	private boolean[][] whitePiecesAtBefore;
	private boolean[][] whitePiecesAtAfter;

	public void initialize() {
		kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
//		gripper = new GripperControlIOGroup(kuka_Sunrise_Cabinet_1);
		observerManager = new ObserverManager();
		
		gripperDevice = new GripperControlIOGroup(kuka_Sunrise_Cabinet_1);
		
		Gripper.attachTo(lbr.getFlange());
		
		chessBase = getApplicationData().getFrame("/base").copy();
		chessBase.setX(getApplicationData().getFrame("/chessbase").getX());
		chessBase.setY(getApplicationData().getFrame("/chessbase").getY());
		chessBase.setZ(getApplicationData().getFrame("/chessbase").getZ());
		gripperTCP = Gripper.getFrame("/gripperTCPOut");
		
		double gripperTCPOffsetX = Gripper.getFrame("/gripperTCPOut").getX();
		double gripperTCPOffsetY = Gripper.getFrame("/gripperTCPOut").getY();
		
		whitePiecesAtBefore = new boolean[8][8];
		whitePiecesAtAfter = new boolean[8][8];
	    
		chessFrames = new Frame[8][8];
		chessFramesAbove = new Frame[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Frame chessPoint = chessBase.copy();
				chessPoint.setX(chessPoint.getX()-j*40+gripperTCPOffsetX);
				chessPoint.setY(chessPoint.getY()-20-(7-i)*40+gripperTCPOffsetY);
				chessPoint.setZ(chessPoint.getZ()+35);
				chessFrames[i][j] = chessPoint.copy();
//				gripperTCP.move(ptp(chessFrames[i][j]));
				chessPoint.setZ(chessPoint.getZ()+50);
				chessFramesAbove[i][j] = chessPoint.copy();
//				gripperTCP.move(ptp(chessFramesAbove[i][j]));
//				gripperTCP.move(ptp(chessFrames[i][j]));
			}
		}
		
		String javaLibPath = System.getProperty("java.library.path");
		String libraryPath = "C://opencv//build//java//x86";
		System.out.println(javaLibPath);
		if (!javaLibPath.contains(libraryPath)){
			try{
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
		
//		String libraryPath = "C:\\images\\";
//		String fileName = "";
//		try {
//			fileName = "finaltest.jpg";
//		    img = ImageIO.read(new File(libraryPath + fileName));
//		} catch (IOException e) {
//		}
//		int verticalCorners = 9;
//		int horizontalCorners = 10;
//		img = CameraHandler.rotateClockwise90(img);
//		FindCorners cornerFinder = new FindCorners(img, horizontalCorners, verticalCorners);
//		double[][] pointCoordinates = cornerFinder.GetPointCoordinates();
	}

	public void run() {		
//		getLogger().info("Show modal dialog and wait for user to confirm");
//        int isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, informationText, "OK", "Cancel");
//        if (isCancel == 1)
//        {
//            return;
//        }
		ChessGame cg = new ChessGame();
        gripperTCP.move(ptp(getApplicationData().getFrame("/chessbase/cameraPosition")));
		CameraHandler cam = new CameraHandler(0);
		BufferedImage img = cam.grabFrame();
		img = CameraHandler.rotate90(img);
		
		// Determining the position of the white pieces
		String libraryPath = "C:\\images\\";
		String fileName = "";
		BufferedImage calibrationImg;
		try {
			fileName = "finaltest.jpg";
		    calibrationImg = ImageIO.read(new File(libraryPath + fileName));
		    calibrationImg = CameraHandler.rotate90(calibrationImg);
		    int horizontalCorners = 10;
			int verticalCorners = 9;
			ScanPiecesPosition pp = new ScanPiecesPosition(calibrationImg, horizontalCorners, verticalCorners);
			boolean[][] PiecesAt = pp.ScanPositions(img);
			System.out.println(PiecesAt.length);
			
			Move move = new Move(4, 1, 4, 3);
			cg.movePiece(move);		
			Move algorithmReply = cg.algorithm.reply(false);
			System.out.println(algorithmReply.toString());
			cg.movePiece(algorithmReply);
			MovePiece(algorithmReply.x1, algorithmReply.y1, algorithmReply.x2, algorithmReply.y2);
			
			// Processing the result of the imgprocess methods
			boolean[][] step = cg.board.WhitePiecesAt();
			
			Move findMove = move.FindMove(step, PiecesAt);
			System.out.println("Found move based on the pictures: " + findMove.toString());
			
//			Move move = new Move(6, 1, 6, 3);
			cg.movePiece(findMove);
			
			algorithmReply = cg.algorithm.reply(false);
			System.out.println(algorithmReply.toString());
			
			cg.movePiece(algorithmReply);
			MovePiece(algorithmReply.x1, algorithmReply.y1, algorithmReply.x2, algorithmReply.y2);
			
//			CloseGripper();
			gripperTCP.move(ptp(getApplicationData().getFrame("/chessbase/cameraPosition")));
//			OpenGripper();
	        // TestImageProcessing();
	        
	        // TestChess();
			
	        // OpenGripper();
		} catch (IOException e) {
		}
		cam.CloseCamera();
	}
	
	private void TestChess() {
		ChessGame cg = new ChessGame();
		Move move = new Move(6, 1, 6, 3);
		
		// Processing the result of the imgprocess methods
		Move findMove = move.FindMove(whitePiecesAtBefore, whitePiecesAtAfter);
		System.out.println("Found move based on the pictures: " + findMove.toString());
		
//		Move foundMove = move.FindMove(move.WhitePiecesAt(cg.board.b), move.WhitePiecesAt(cg.board.b));
		cg.movePiece(move);		
		Move algorithmReply = cg.algorithm.reply(false);
		System.out.println(algorithmReply.toString());
//		int isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, "Chess algorithm reply: " + algorithm.toString(), "OK", "Cancel");
//        if (isCancel == 1)
//        {
//            return;
//        }
        
//        isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, "Check if move was legit: " + cg.checkIfLegalMove(move), "OK", "Cancel");
//        if (isCancel == 1)
//        {
//            return;
//        }
        
		cg.movePiece(algorithmReply);
		move = new Move(5, 1, 5, 2);
		if (cg.checkIfLegalMove(move))
			cg.movePiece(move);
	}
	
	private void TestImageProcessing() {
		// Capturing an image
		CameraHandler cam = new CameraHandler(0);
		BufferedImage img2 = cam.grabFrame();
		System.out.println("Size of the image: " + img2.getWidth() + "x" + img2.getHeight());
		ScanPiecesPosition.writeFile(img2, 10, 10);
		
		// This part uses the methods of the image processing except capturing an image
		BufferedImage img = null;
		String libraryPath = "C:\\images\\";
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
		
		// Reading the before and after step images
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
		
		// Determining the position of the white pieces
		ScanPiecesPosition pp = new ScanPiecesPosition(img, horizontalCorners, verticalCorners);
		boolean[][] PiecesAtBefore = pp.ScanPositions(imgbefore);
		boolean[][] PiecesAtAfter = pp.ScanPositions(imgafter);
		this.whitePiecesAtBefore = PiecesAtBefore;
		this.whitePiecesAtAfter = PiecesAtAfter;
		System.out.println("Just checking: " + PiecesAtBefore.length);
		String beforeText = "";
		String afterText = "";
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				//System.out.println(PiecesAtBefore[i][j] + "\t");
				beforeText += PiecesAtBefore[i][j] + "\t";
			}
			//System.out.println(beforeText);
			beforeText += "\n";
		}
		
		// Displays the result of the ScanPosition function
		int isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, beforeText, "OK", "Cancel");
        if (isCancel == 1)
        {
            return;
        }
		
		// System.out.println();
        afterText.concat("\n");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// System.out.print(PiecesAtAfter[i][j] + "\t");
				afterText += PiecesAtAfter[i][j] + "\t";
			}
			// System.out.println();
			afterText += "\n";
		}
		
		// Displays the result of the ScanPosition function
		isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, afterText, "OK", "Cancel");
        if (isCancel == 1)
        {
            return;
        }
        cam.CloseCamera();
	}
	
	private void MovePiece(int si, int sj, int ei, int ej){
		pickUp(si,sj);
		putDown(ei,ej);
	}
	
	private void pickUp(int i, int j){
		gripperTCP.move(ptp(chessFramesAbove[i][j]));
		OpenGripper();
		gripperTCP.move(ptp(chessFrames[i][j]).setJointVelocityRel(0.2));
		CloseGripper();
		gripperTCP.move(ptp(chessFramesAbove[i][j]));
	}
	
	private void putDown(int i, int j){
		gripperTCP.move(ptp(chessFramesAbove[i][j]));
		gripperTCP.move(ptp(chessFrames[i][j]).setJointVelocityRel(0.2));
		OpenGripper();
		gripperTCP.move(ptp(chessFramesAbove[i][j]));
	}
	
	private void OpenGripper(){
		gripperDevice.setOpenGripper(true);
//		wait(100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Input input = gripperDevice.getInput("Status");
		BooleanIOCondition inputCondition =
		new BooleanIOCondition(input, true);
		boolean result = observerManager.waitFor(inputCondition, 5, TimeUnit.SECONDS);
		gripperDevice.setOpenGripper(false);
		
	}
	
	private void CloseGripper(){
		gripperDevice.setCloseGripper(true);
		Input input = gripperDevice.getInput("Status");
//		wait(100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BooleanIOCondition inputCondition = new BooleanIOCondition(input, true);
		boolean result = observerManager.waitFor(inputCondition, 5, TimeUnit.SECONDS);
		gripperDevice.setCloseGripper(false);
	}
}
