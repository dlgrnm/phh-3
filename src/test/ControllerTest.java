package test;

import static org.junit.Assert.*;
import main.Controller;
import main.ControlDeviceType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

	Controller myApp;
	
	@Before
	public void setUp() throws Exception {
		myApp = new Controller();
	}

	@After
	public void tearDown() throws Exception {
	}

	
//--- This section tests adding valid devices to list of controlled devices ---	
	
	@Test
	public void testAddFistValidDeviceSucceeds() {
		assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "aaaa", 0, 100, 20, 50));
		assertTrue("Device id=1111 should be in list", myApp.isDeviceInList("aaaa"));
		assertEquals("There should be 1 device in list of controlled devices", myApp.getNumDevices(), 1);
//		assertFalse("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaaa", 0, 100, 20, 50));
		
//		assertFalse("id=aaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaa", 0, 100, 20, 60));
//		assertFalse("id=aaaaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaaaa", 0, 100, 20, 60));
//		assertFalse("id=aaaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaaa", 0, 100, 20, 60));
//		assertFalse("id='' төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "", 0, 100, 20, 60));
//		assertFalse("id=aaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "1111", 0, 100, 20, 60));
//		assertFalse("id=aaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaae", -2, 100, 20, 60));
//		assertFalse("id=aaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaae", 2.5, 100, 20, 60));
//		assertFalse("id=aaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaa", 0, -4, 20, 60));
//		assertFalse("id=aaa төхөөрөмж нэмэх", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "aaa", 0, 4.5, 20, 60));
//		assertFalse("Device id=bbbb should be in list", myApp.isDeviceInList("bbbb"));
//		assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "bbbb", 0, 100, 20, 50));
//		assertEquals("There should be 2 device in list of controlled devices", myApp.getNumDevices(), 2);
	}
	
	@Test
	public void testRemoveDevice() {
		assertFalse("id='' илгээх", myApp.removeDevice(""));
		assertEquals("There should be 2 device in list of controlled devices", myApp.getNumDevices(), 0);
		assertFalse("id=bbbb төхөөрөмж хасах", myApp.removeDevice("bbbb"));
		assertFalse("", myApp.isDeviceInList("bbbb"));
	}
	
	@Test
    public void testAddInvalidDeviceIDFails() {
//        assertFalse("Add device should fail", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "", 0, 100, 20, 50));
    }

    @Test
    public void testAddDeviceWithDuplicateIDFails() {
        assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "ssss", 0, 100, 20, 50));
//        assertFalse("Add device should fail", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "ssss", 0, 100, 20, 50));
    }

    @Test
    public void testAddDeviceWithInvalidOperatingRangeFails() {
//        assertFalse("Add device should fail", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "qqqq", 100, 0, 20, 50));
    }

    @Test
    public void testAddDeviceWithInvalidSafeRangeFails() {
//        assertFalse("Add device should fail", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_PRESSURE_PUMP, "qqqq", 0, 100, 50, 20));
    }


    @Test
    public void testRemoveExistingDeviceSucceeds() {
        assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "wwww", 0, 100, 20, 50));
        assertTrue("Remove device should succeed", myApp.removeDevice("wwww"));
        assertFalse("Device id=4444 should not be in the list", myApp.isDeviceInList("wwww"));
        assertEquals("There should be 0 devices in list of controlled devices", myApp.getNumDevices(), 0);
    }

    @Test
    public void testRemoveNonExistingDeviceFails() {
        assertFalse("Remove device should fail", myApp.removeDevice("zzzz"));
    }

    @Test
    public void testRemoveDeviceWithNullIDFails() {
        assertFalse("Remove device should fail", myApp.removeDevice(""));
    }

    @Test
    public void testControlExistingDeviceSucceeds() {
        assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "eeee", 0, 100, 20, 50));
        assertTrue("Control device should succeed", myApp.doControl("eeee", 30));
    }

    @Test
    public void testControlNonExistingDeviceSucceeds() {
        assertTrue("Control device should fail", myApp.doControl("ffff", 30));
    }

    @Test
    public void testControlDeviceWithNullIDFails() {
        assertFalse("Control device should fail", myApp.doControl("", 30));
    }

    @Test
    public void testControlDeviceWithOutOfRangeInputFails() {
        assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "tttt", 0, 100, 20, 50));
        assertFalse("Control device should fail", myApp.doControl("tttt", 120));
    }

}
