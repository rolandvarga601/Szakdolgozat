package application;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptpHome;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.applicationModel.tasks.IRoboticsAPITaskInjectableTypes;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.scenegraph.ISceneGraph;
import javax.inject.Inject;

/**
 * Implementation of a robot application.
 * 
 * <p>The application provides an {@link #initialize()} and a {@link #run()} method, which will be
 * called successively in the application life cycle. The application will terminate automatically
 * after the {@link #run()} method has finished or after stopping the task. The {@link #dispose()}
 * method will be called, even if an exception is thrown during initialization or run.
 * 
 * @see IRoboticsAPITaskInjectableTypes Types and Services available for Dependency Injection
 * @see RoboticsAPIApplication Application specific services available for Dependency Injection
 */
public class RobotApplication extends RoboticsAPIApplication {
  @Inject private LBR lBR_iiwa_14_R820_1;
  @Inject private ISceneGraph sceneGraph;

  @Override
  public void initialize() throws Exception {
    // Cleans the scene graph by removing all transient objects
    sceneGraph.clean();

    // TODO Initialize your application here
  }

  @Override
  public void run() throws Exception {
    // TODO Your application execution starts here
    lBR_iiwa_14_R820_1.getFlange().move(ptpHome());
    
    lBR_iiwa_14_R820_1.getFlange().move(ptp(sceneGraph.getWorld().findFrame("/p1")));
    lBR_iiwa_14_R820_1.getFlange().move(ptp(sceneGraph.getWorld().findFrame("/p2")));
    lBR_iiwa_14_R820_1.getFlange().move(ptpHome());
  }
}
