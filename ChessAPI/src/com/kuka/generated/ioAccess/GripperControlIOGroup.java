package com.kuka.generated.ioAccess;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.ioModel.AbstractIOGroup;
import com.kuka.roboticsAPI.ioModel.IOTypes;

/**
 * Automatically generated class to abstract I/O access to I/O group <b>GripperControl</b>.<br>
 * <i>Please, do not modify!</i>
 * <p>
 * <b>I/O group description:</b><br>
 * ./.
 */
@Singleton
public class GripperControlIOGroup extends AbstractIOGroup
{
	/**
	 * Constructor to create an instance of class 'GripperControl'.<br>
	 * <i>This constructor is automatically generated. Please, do not modify!</i>
	 *
	 * @param controller
	 *            the controller, which has access to the I/O group 'GripperControl'
	 */
	@Inject
	public GripperControlIOGroup(Controller controller)
	{
		super(controller, "GripperControl");

		addDigitalOutput("CloseGripper", IOTypes.BOOLEAN, 1);
		addDigitalOutput("OpenGripper", IOTypes.BOOLEAN, 1);
		addInput("Status", IOTypes.BOOLEAN, 1);
	}

	/**
	 * Gets the value of the <b>digital output '<i>CloseGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'CloseGripper'
	 */
	public boolean getCloseGripper()
	{
		return getBooleanIOValue("CloseGripper", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>CloseGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'CloseGripper'
	 */
	public void setCloseGripper(java.lang.Boolean value)
	{
		setDigitalOutput("CloseGripper", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>OpenGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'OpenGripper'
	 */
	public boolean getOpenGripper()
	{
		return getBooleanIOValue("OpenGripper", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>OpenGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'OpenGripper'
	 */
	public void setOpenGripper(java.lang.Boolean value)
	{
		setDigitalOutput("OpenGripper", value);
	}

	/**
	 * Gets the value of the <b>digital input '<i>Status</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'Status'
	 */
	public boolean getStatus()
	{
		return getBooleanIOValue("Status", false);
	}

}
