package application;

import javax.inject.Inject;
import javax.inject.Named;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.math.ITransformation;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;
import com.kuka.roboticsAPI.motionModel.IMotion;
import com.kuka.roboticsAPI.motionModel.PTP;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

public class MechanicalZeroPosition extends RoboticsAPIApplication {
	@Inject
	private LBR lbr;
	
	@Inject
	@Named("gripper")
	private Tool Gripper;
	
	private final static String informationText=
			"This application is intended for floor mounted robots!"+ "\n" +
			"\n" +
			"The robot will move to the mechanical zero position.";

	public void initialize() {
		Gripper.attachTo(lbr.getFlange());
	}

	public void run() {
//		getLogger().info("Show modal dialog and wait for user to confirm");
//        int isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, informationText, "OK", "Cancel");
//        if (isCancel == 1)
//       {
//           return;
//        }

		getLogger().info("Move to the mechanical zero position");
		PTP ptpToMechanicalZeroPosition = ptp(0,0,0,0,0,0,0);
		
//		ptpToMechanicalZeroPosition.setJointVelocityRel(0.80);
//		lbr.moveAsync(ptpToMechanicalZeroPosition);
//		lbr.move(ptp(getApplicationData().getFrame("/chessbase/cameraPosition")));
//		lbr.move(linRel(Transformation.ofTranslation(0,0,20), getApplicationData().getFrame("/chessbase")));
//		Frame chessPoint = getApplicationData().getFrame("/chessbase/cameraPosition").copy();
//		chessPoint.setX(getApplicationData().getFrame("/chessbase/cameraPosition").getX()-20);
//		Gripper.move(ptp(chessPoint));
		
		Frame chessBase = getApplicationData().getFrame("/base").copy();
		chessBase.setX(getApplicationData().getFrame("/chessbase").getX());
		chessBase.setY(getApplicationData().getFrame("/chessbase").getY());
		chessBase.setZ(getApplicationData().getFrame("/chessbase").getZ());
//		Gripper.move(ptp(chessBaseCopy));
		
//		chessBaseCopy.setX(getApplicationData().getFrame("/chessbase").getX()+10);
//		Gripper.move(ptp(chessBaseCopy));
		
		ObjectFrame gripperTCP = Gripper.getFrame("/gripperTCPOut");
		gripperTCP.move(ptp(chessBase));
//		chessBaseCopy.setZ(0);
//		Gripper.getFrame("/gripperTCPOut").move(ptp(chessBaseCopy));
		Frame[][] chessFrames = new Frame[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Frame chessPoint = chessBase.copy();
				chessPoint.setX(chessPoint.getX()-20-j*30);
				chessPoint.setY(chessPoint.getY()-20-i*40);
				chessFrames[i][j] = chessPoint.copy();
				gripperTCP.move(ptp(chessFrames[i][j]));
			}
		}
		
//		lbr.setHomePosition(chessBase);
//		Gripper.move(ptpHome());
//		Gripper.move(ptp(getApplicationData().getFrame("/chessbase/cameraPosition")));
		}
}
