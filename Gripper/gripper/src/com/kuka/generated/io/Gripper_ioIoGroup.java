package com.kuka.generated.io;

import com.kuka.io.IIoDefinition;
import com.kuka.io.IIoDefinitionProvider;
import com.kuka.io.IIoValueProvider;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Automatically generated class to access to IOs of the sunrise IO group <b>Gripper_io</b>.
 * <i>Please, do not modify!</i>
 *
 * <p><b>IO group user description:</b>
 * ./.
 *
 * <p><b>Hint:</b>
 * To work directly with IOs the interfaces {@link IIoDefinitionProvider} and
 * {@link IIoValueProvider} can be used. Both of them can be obtained via Dependency Injection.
 * The {@link IIoDefinitionProvider} allows to access the {@link IIoDefinition} which provides
 * all information of about an IO, e.g. its name, its related group name or the user description.
 * Via the {@link IIoValueProvider} it is possible to request the current value of an IO or set
 * a new value for connected outputs.
 */
@Singleton
public class Gripper_ioIoGroup {
  /** The name of the IO group as it is configured in WorkVisual. */
  public static final String NAME = "Gripper_io";

  private IIoDefinitionProvider definitionProvider;
  private IIoValueProvider valueProvider;

  /**
   * Creates a new instance.
   * 
   * @param ioDefinitionProvider
   *        the provider for accessing IO definitions
   * @param ioValueProvider
   *        the provider for accessing IO values
   */
  @Inject
  public Gripper_ioIoGroup(IIoDefinitionProvider ioDefinitionProvider,
      IIoValueProvider ioValueProvider) {
    definitionProvider = ioDefinitionProvider;
    valueProvider = ioValueProvider;
  }

  /**
  * Gets the value of the <b>output '<i>Close</i>'</b>.
  * <i>This method is automatically generated. Please, do not modify!</i>
  *
  * <p><b>IO type and direction:</b>
  * digital output
  *
  * <p><b>User description of the IO:</b>
  * ./.
  *
  * <p><b>Range of the IO value:</b>
  * [false; true]
  *
  * @return current value of the output 'Close'
  */
  public boolean getClose() {
    IIoDefinition definition = definitionProvider.getIoDefinition(NAME, "Close");
    return valueProvider.getIoValue(definition).getValueAsBoolean();
  }

  /**
  * Sets the value of the <b>output '<i>Close</i>'</b>.
  * <i>This method is automatically generated. Please, do not modify!</i>
  *
  * <p><b>IO type and direction:</b>
  * digital output
  *
  * <p><b>User description of the IO:</b>
  * ./.
  *
  * <p><b>Range of the IO value:</b>
  * [false; true]
  *
  * @param value
  *        the value, which has to be written to the output 'Close'
  */
  public void setClose(boolean value) {
    IIoDefinition definition = definitionProvider.getIoDefinition(NAME, "Close");
    valueProvider.setOutputValue(definition, value);
  }

  /**
  * Gets the value of the <b>output '<i>Open</i>'</b>.
  * <i>This method is automatically generated. Please, do not modify!</i>
  *
  * <p><b>IO type and direction:</b>
  * digital output
  *
  * <p><b>User description of the IO:</b>
  * ./.
  *
  * <p><b>Range of the IO value:</b>
  * [false; true]
  *
  * @return current value of the output 'Open'
  */
  public boolean getOpen() {
    IIoDefinition definition = definitionProvider.getIoDefinition(NAME, "Open");
    return valueProvider.getIoValue(definition).getValueAsBoolean();
  }

  /**
  * Sets the value of the <b>output '<i>Open</i>'</b>.
  * <i>This method is automatically generated. Please, do not modify!</i>
  *
  * <p><b>IO type and direction:</b>
  * digital output
  *
  * <p><b>User description of the IO:</b>
  * ./.
  *
  * <p><b>Range of the IO value:</b>
  * [false; true]
  *
  * @param value
  *        the value, which has to be written to the output 'Open'
  */
  public void setOpen(boolean value) {
    IIoDefinition definition = definitionProvider.getIoDefinition(NAME, "Open");
    valueProvider.setOutputValue(definition, value);
  }

  /**
  * Gets the value of the <b>input '<i>Status</i>'</b>.
  * <i>This method is automatically generated. Please, do not modify!</i>
  *
  * <p><b>IO type and direction:</b>
  * digital input
  *
  * <p><b>User description of the IO:</b>
  * ./.
  *
  * <p><b>Range of the IO value:</b>
  * [false; true]
  *
  * @return current value of the input 'Status'
  */
  public boolean getStatus() {
    IIoDefinition definition = definitionProvider.getIoDefinition(NAME, "Status");
    return valueProvider.getIoValue(definition).getValueAsBoolean();
  }

}
