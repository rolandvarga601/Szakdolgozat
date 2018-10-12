package application;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptpHome;

import com.kuka.device.common.JointPosition;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;
import com.kuka.scenegraph.ISceneGraph;
import com.kuka.task.ITaskLogger;
import javax.inject.Inject;

/**
 * Moves a floor mounted robot to its mechanical zero position.
 */
public class MechanicalZeroPosition extends RoboticsAPIApplication {
  @Inject private LBR lbr;
  @Inject private ITaskLogger logger;
  @Inject private IApplicationUI applicationUi;
  @Inject private ISceneGraph sceneGraph;

  private static final JointPosition MECHANICAL_ZERO_POSITION =
      JointPosition.ofDeg(0, 0, 0, 0, 0, 0, 0);

  private static final String INFORMATION_TEXT =
      "This application is intended for floor mounted robots!\n\n"
          + "The robot will move to the mechanical zero position.";

  @Override
  public void initialize() throws Exception {
    // cleans the scene graph by detaching all transient objects
    sceneGraph.clean();
  }

  @Override
  public void run() throws Exception {
    logger.info("Move to HOME-Position.");
    lbr.getFlange().move(ptpHome());

    logger.info("Show modal dialog and wait for user to confirm");
    int selectedButtonIndex = applicationUi.displayModalDialog(
        ApplicationDialogType.QUESTION,
        INFORMATION_TEXT,
        "OK", "Cancel");
    if (selectedButtonIndex == 1) {
      return;
    }

    logger.info("Move to the mechanical zero position");
    lbr.getFlange().move(ptp(MECHANICAL_ZERO_POSITION).setJointVelocityRel(0.25));
  }
}
