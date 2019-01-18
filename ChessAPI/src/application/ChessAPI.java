package application;


import imgprocess.CameraHandler;
import imgprocess.ScanPiecesPosition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.opencv.core.Core;

import chess.core.ChessGame;
import chess.core.Move;

import com.kuka.generated.ioAccess.GripperControlIOGroup;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.BooleanIOCondition;
import com.kuka.roboticsAPI.conditionModel.ObserverManager;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.ioModel.Input;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

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
public class ChessAPI extends RoboticsAPIApplication {
	@Inject
	private LBR lbr;
	
	@Inject
	@Named("gripper")
	private Tool gripperTool;
	
	private Controller kuka_Sunrise_Cabinet_1;
	
	private ObserverManager observerManager;
	
	private GripperControlIOGroup gripperController;
	
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
		kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
		observerManager = new ObserverManager();
		
		gripperController = new GripperControlIOGroup(kuka_Sunrise_Cabinet_1);

		// Creating a new chessbase frame with the same coordinates
		// but with different orientation
//		chessBase = getApplicationData().getFrame("/base").copy();
		chessBase = getApplicationData().getFrame("/chessbase").copy();
//		chessBase.setX(getApplicationData().getFrame("/chessbase").getX());
//		chessBase.setY(getApplicationData().getFrame("/chessbase").getY());
//		chessBase.setZ(getApplicationData().getFrame("/chessbase").getZ());
		
		// Attaches the gripper to the flange 
		// (for absolute coordinates)
		gripperTool.attachTo(lbr.getFlange());
		
		// Gets the offset of the gripper TCP relative to flange
		gripperTCP = gripperTool.getFrame("/gripperTCPOut");
		Frame chessCorner = getApplicationData().getFrame("/chessbase/chessCorner").copy();
		Frame chessCorner0 = getApplicationData().getFrame("/chessbase/chessCorner0").copy();
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
//				chessPoint.setX(chessPoint.getX() - j * 40 + gripperTCPOffsetX);
//				chessPoint.setY(chessPoint.getY() - 20 - (7 - i) * 40
//						+ gripperTCPOffsetY);
				/*chessPoint.setX(chessPoint.getX() - j * (chessPoint.getX()-chessCorner.getX())/8
						+ gripperTCPOffsetX);
				chessPoint.setY(chessPoint.getY()-(7 - i + 0.5)*(chessPoint.getY()-chessCorner.getY())/8
						+ gripperTCPOffsetY);*/
				
				// Not tested
				chessPoint.setX(20 + i*40 + gripperTCPOffsetY);
				chessPoint.setY(20 + j*40 + gripperTCPOffsetX);
				

				// Sets z coordinate to grabbing height
//				chessPoint.setZ(chessPoint.getZ() + 35);
				/*chessPoint.setZ(chessPoint.getZ() + 35 + 
						(7-i) * (chessPoint.getZ()-chessCorner0.getZ())/8 +
						j * (chessCorner.getZ()-chessCorner0.getZ())/8);*/
				
				// Not tested
				chessPoint.setZ(35);
				chessPoint.setParent(getApplicationData().getFrame("/chessbase"));

				// Generates the approaching position
				chessFrames[i][j] = chessPoint.copy();
				chessPoint.setZ(chessPoint.getZ() + 50);
				chessFramesAbove[i][j] = chessPoint.copy();

				// Moves to the saved positions for test
//				 gripperTCP.move(ptp(chessFramesAbove[i][j]));
//			 gripperTCP.move(ptp(chessFrames[i][j]));
			}
		}

		// Configuring the OpenCV dll
		ConfigureOpenCV();
		
		// Opening the webcam
		cam = new CameraHandler(0);
	}

	@Override
	public void run() {
//		lbr.move(ptpHome());
		
		ChessGame cg = new ChessGame();
		Move moveFinder = new Move(0,1,0,2);
		Move algorithmReply = new Move(0,0,0,0);
		Move foundMove = moveFinder;
		BufferedImage img;
		
		BufferedImage calibrationImg = null;
		String libraryPath = "C:\\images\\";
		String fileName = "";
		try {
			fileName = "calib.jpg";
		    calibrationImg = ImageIO.read(new File(libraryPath + fileName));
		    calibrationImg = CameraHandler.rotate90(calibrationImg);
		} catch (IOException e) {
		}
		int horizontalCorners = 10;
		int verticalCorners = 9;
		ScanPiecesPosition pp = new ScanPiecesPosition(calibrationImg,
			horizontalCorners, verticalCorners);
		
		// Move to camera position
		gripperTCP.move(ptp(getApplicationData().getFrame("/chessbase/cameraPosition")));
		
		for (int i = 0; i < 6; i++) {
			// Ask if the move was made
			getLogger().info("Show modal dialog and wait for user to confirm");
	        int isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, "Moved?", "Yes", "Cancel");
	        if (isCancel == 1)
	        {
	           break;
	        }
			
			// Take a picture
			img = cam.grabFrame();
			
			// Process the picture
			img = CameraHandler.rotate90(img);
			boolean[][] PiecesAtAfter = pp.ScanPositions(img);
			boolean[][] PiecesAtBefore = cg.board.WhitePiecesAt();
//			for (int n = 0; n < 8; n++){
//				for (int m = 0; m < 8; m++) {
//					if (PiecesAtBefore[n][m] != PiecesAtAfter[n][m])
//						System.out.println(n + "." + m + ": " + PiecesAtBefore[n][m] + " -> " + PiecesAtAfter[n][m]);
//				}
//			}
			foundMove = moveFinder.FindMove(PiecesAtBefore, PiecesAtAfter);
			foundMove.type = cg.board.b[foundMove.x1][foundMove.y1].type;
			
//			foundMove.x1++;
//			foundMove.x2++;
			System.out.println("Found move based on the pictures: " + foundMove.toString());
			
			// Generating reply
			if (cg.checkIfLegalMove(foundMove)) {
				cg.movePiece(foundMove);
				System.out.println("Move was correct");
				
				// Reply
				algorithmReply = cg.algorithm.reply(false);
				System.out.println("Reply move: " + algorithmReply.toString());
				cg.movePiece(algorithmReply);
				MovePiece(algorithmReply.x1, algorithmReply.y1, algorithmReply.x2, algorithmReply.y2);
				
				// Move to camera position
				gripperTCP.move(ptp(getApplicationData().getFrame("/chessbase/cameraPosition")));
				
			} else {
				System.out.println("Move was not correct");
				i--;
			}
		}
		
	}
	
	@Override
	public void dispose() {
		// This part will still run in case of error
		cam.CloseCamera();
	}
	
	private void ConfigureOpenCV() {
		this.OpenCVHome = "C://opencv//build//java//x86";
		
		// Adding the dll to the library path
		String javaLibPath = System.getProperty("java.library.path");
		String libraryPath = this.OpenCVHome;
		if (!javaLibPath.contains(libraryPath)){
			try{
				// OpenCV temporary library added to path
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
		System.out.println("Loaded OpenCV dll in main");
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
		gripperController.setOpenGripper(true);
//		wait(100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Input input = gripperController.getInput("Status");
		BooleanIOCondition inputCondition =
		new BooleanIOCondition(input, true);
		boolean result = observerManager.waitFor(inputCondition, 5, TimeUnit.SECONDS);
		gripperController.setOpenGripper(false);
		
	}
	
	private void CloseGripper(){
		gripperController.setCloseGripper(true);
		Input input = gripperController.getInput("Status");
//		wait(100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BooleanIOCondition inputCondition = new BooleanIOCondition(input, true);
		boolean result = observerManager.waitFor(inputCondition, 5, TimeUnit.SECONDS);
		gripperController.setCloseGripper(false);
	}
}