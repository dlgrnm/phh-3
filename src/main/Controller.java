package main;


/**
 * Enable management of a range of controlled devices. Functionality:
 * 	- Add remove a device to/from the list of devices to be controlled.  
 *  - Send a new input reading to a controlled device.
 * 
 * @author Diana Kirk
 * 
 * @version 1.1 : December 18th, 2023
 * Өөрчөлт хийсэн: Э.Дэлгэрням
 * Алдаа олж засвар хийсэн.
 *		
 *
 */

public class Controller {
	
	// --- Constants ---
	static final int MAX_DEVICES = 6;
	
	// --- Fields ---
	// List of devices to be controlled
	private ControlledDevice [] controlledDevices; 
	private int nextIndex;  // equivalent to the number of devices in 'controlledDevics' array

	// --- Constructor ---
	public Controller() {
		controlledDevices = new ControlledDevice[MAX_DEVICES];
	}
	

	// --- Public methods ---
	
	/**
	 * Add a device to the list of devices to be controlled by this controller. 
	 * 
	 * A controller can control up to six physical devices. The method succeeds if the device is successfully 
	 * added to the list of controlled devices. The method fails if the list is full or if the device is not 
	 * correctly specified. 
	 * 
	 * @param deviceType	The type of device to be controlled (enumerated in 'ControlledDeviceTypes') 
	 * @param deviceID		Unique identifier for the physical device. This is a 4-digit string.
	 * @param operatingMin	Minimum value accepted by the device i.e. operating range floor. Non-negative integer value expected.
	 * @param operatingMax	Maximum value accepted by the device i.e. operating range ceiling. Non-negative integer value expected.
	 * @param safeMin		Minimum 'safe' operating value for the device. Expected >= 'operatingMin'.
	 * @param safeMax		Maximum 'safe' operating value accepted by the device. Expected <= operatingMax'.
	 * 
	 * @return 				'true' if the device is successfully added to the list
	 *						'false' if already 6 devices in the array 
	 *			
	 * @throws 				IllegalArgumentException if 'deviceID' is invalid or is already in the list
	 * @throws				NullPointerException if 'deviceID' is null
	 * @throws				IllegalArgumentException if 'operatingMin' or 'operatingMax' is negative
	 * @throws				IllegalArgumentException if 'operatingMin' is greater than 'operatingMax'
	 * @throws				IllegalArgumentException if 'safeMin' is greater than 'safeMax'
	 * @throws				IllegalArgumentException if 'safeMin' or 'safeMax' are outside 'operatingMin' and 'operatingMax'
	 */ 
	public boolean addDevice(ControlDeviceType deviceType, String deviceID, int operatingMin, int operatingMax, int safeMin, int safeMax) 
			throws IllegalArgumentException, NullPointerException {
	
		final String DIGITS_MASK = "[0-9]+"; 

		boolean inputsOk = true;  // assume device will be successfully added

		if (deviceID == null) {
			throw new NullPointerException();
		}
		
		if (deviceID.equals("") || deviceID.length()!=4  ||  deviceID.matches(DIGITS_MASK)) {
			throw new IllegalArgumentException();
		}

		if (findDeviceIDInList(deviceID)) {
			throw new IllegalArgumentException();
		}

		
		if ((operatingMin<0) || (operatingMin>operatingMax) || (safeMin<operatingMin) || (safeMax>operatingMax) || (safeMin>safeMax)) {
			throw new IllegalArgumentException();
		}
		
		// Provided data is valid, create a device object and add to the 'controlledDevices' list.
		if (inputsOk) {		
		
			switch (deviceType) {
				case CONTROLDEVICE_FAN_DRIVER: controlledDevices[nextIndex] = new FanDriver(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
				case CONTROLDEVICE_PRESSURE_PUMP: controlledDevices[nextIndex] = new PressurePump(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
				case CONTROLDEVICE_VOLTAGE_REGULATOR: controlledDevices[nextIndex] = new VoltageRegulator(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
				case CONTROLDEVICE_WATER_VALVE: controlledDevices[nextIndex] = new WaterValve(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
			}
			nextIndex++;
		}
		
		return (inputsOk);
	}


	/**
	 * Remove a device from the list of devices to be controlled by this controller. 
	 * 
	 * The method succeeds if the device is successfully removed from the list of controlled devices. The method fails if the list is empty, 
	 * the device is not in the list or "deviceID" is null. 
	 * 
	 * @param deviceID		Unique identifier for the physical device. This is a 4-digit string.
	 * @return 				'true' if the device is successfully removed from the list
	 *						'false' if  the device is not in the list OR the list is empty
	 * @throws 				NullPointerException if "deviceID" is null
	 */ 
	public boolean removeDevice(String deviceID) 
			throws NullPointerException {
	
		boolean inputsOk = true;  // assume device will be successfully added

		if (deviceID == null) {
			throw new NullPointerException();
		}
		//not checking list is empty
		if (!findDeviceIDInList(deviceID)  || controlledDevices == null) { 
			inputsOk = false; 		
		}
	
		inputsOk = !(nextIndex == 0);		

		
		// Remove 'deviceID', moving later objects down and decrement the 'next' pointer.
		if (inputsOk) {	
			for (int i=getDeviceIndex(deviceID); i<controlledDevices.length-1; ++i) 
				controlledDevices[i] = controlledDevices[i+1];
			nextIndex--;
		}
		
		return (inputsOk);
	}
	
	/**
	 * Activate control of a device by sending it a new input value.
	 * 
	 * The method succeeds if the device is successfully controlled. The method fails if the device is not 
	 * in the list, "deviceID" is null, the input value is not within the operating range of the device or
	 * the control call fails. 
	 * 
	 * @param deviceID		Unique identifier for the physical device. This is a 4-digit string.
	 * @param inputVal		The value to be passed to the device.
	 * @return 				'true' if the device successfully activates control
	 *						'false' if  'deviceId' is not in the list OR 
	 *									'deviceID' is null OR
	 *									'inputVal' is not inside the operating range for 'deviceID' OR
	 *									 control call fails
	 */ 

	public boolean doControl(String deviceID, int inputVal) {
		
		boolean inputsOk = true;
		
		final String DIGITS_MASK = "[0-9]+";
		
		if (deviceID.equals("") || deviceID.length()!=4  ||  deviceID.matches(DIGITS_MASK)) {
			throw new IllegalArgumentException();
		}
		
		if (!findDeviceIDInList(deviceID) || deviceID == null) { 
			inputsOk = false; 		
		}
		
		return (inputsOk);
	}

	
	
	//--- Helper methods ---
	public boolean isDeviceInList(String deviceID) {
		return findDeviceIDInList(deviceID);
	}
	
	public int getNumDevices() {
		return (nextIndex);
	}	
	
	private boolean isInputValueInRange(ControlledDevice device, int inputVal) {
	    return inputVal >= device.getOperatingMin() && inputVal <= device.getOperatingMax();
	}

	
	
	//--- Private methods ---
			private boolean findDeviceIDInList(String id) {
				
				int i=0;
				boolean found = false;
				while  (!found && (i<controlledDevices.length)) {
					// if (controlledDevices[i].getID() == id)  -> Error
					if (controlledDevices[i] != null && controlledDevices[i].getID().equals(id)) {
						found = true;
					}
					i++;
				}
				return found;
			}


			private int getDeviceIndex(String id) {
			// Assumes the device with ID is in the list. Invalid return value if it is not.	
				int i = 0;
				for (i=0; (i<nextIndex) && (!controlledDevices[i].getID().equals(id)); ++i) {}
				return i;
				
			}
			
}
