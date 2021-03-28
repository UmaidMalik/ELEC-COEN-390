package com.elec_coen_390.uvme;

import java.util.HashMap;

public class GattAttributes {


    private  static HashMap<String, String> attributes = new HashMap();

    public static String GENERIC_ACCESS = "00001800-0000-1000-8000-00805f9b34fb";
    public static String GENERIC_ATTRIBUTE = "00001801-0000-1000-8000-00805f9b34fb";
    public static String DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb";

    public static String UV_SENSOR_SERVICE = "00000001-a9a6-4e69-87bd-2912357161b3";
    public static String UV_SENSOR_INTENSITY_MEASUREMENT = "00000002-a9a6-4e69-87bd-2912357161b3";
    public static String UV_SENSOR_INTENSITY_MEASUREMENT_NOTIFY = "00000003-a9a6-4e69-87bd-2912357161b3";

    public static String BATTERY_SERVICE = "00000001-b9b6-4d74-87ce-29123591642b";
    public static String BATTERY_LEVEL_CHARACTERISTIC = "00000002-b9b6-4d74-87ce-29123591642b";

    public static String DEVICE_NAME = "00002a00-0000-1000-8000-00805f9b34fb";
    public static String APPEARANCE = "00002a01-0000-1000-8000-00805f9b34fb";
    public static String PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "00002a04-0000-1000-8000-00805f9b34fb";

    public static String MODEL_NUMBER_STRING = "00002a24-0000-1000-8000-00805f9b34fb";
    public static String SERIAL_NUMBER_STRING = "00002a25-0000-1000-8000-00805f9b34fb";
    public static String FIRMWARE_REVISION_STRING = "00002a26-0000-1000-8000-00805f9b34fb";
    public static String HARDWARE_REVISION_STRING = "00002a27-0000-1000-8000-00805f9b34fb";
    public static String SOFTWARE_REVISION_STRING = "00002a28-0000-1000-8000-00805f9b34fb";
    public static String MANUFACTURER_NAME_STRING = "00002a29-0000-1000-8000-00805f9b34fb";

    public static String SERVICE_CHANGED = "00002a05-0000-1000-8000-00805f9b34fb";

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG2 = "00002902-0000-1000-8000-00805f9b34fc";

    public static final int UV_INTENSITY_READ = 1;
    public static final int BATTERY_LEVEL_READ = 2;

    static {
        // Services
        attributes.put(UV_SENSOR_SERVICE, "UV Sensor Service");
        attributes.put(BATTERY_SERVICE, "Battery Service");
        attributes.put(GENERIC_ACCESS, "Generic Access");
        attributes.put(DEVICE_INFORMATION_SERVICE, "Device Information Service");
        attributes.put(GENERIC_ATTRIBUTE, "Generic Attribute");


        // Characteristics
        attributes.put(UV_SENSOR_INTENSITY_MEASUREMENT, "UV Intensity Measurement READ");
        attributes.put(UV_SENSOR_INTENSITY_MEASUREMENT_NOTIFY, "UV Intensity Measurement NOTIFY");
        attributes.put(BATTERY_LEVEL_CHARACTERISTIC, "Battery Level");


        attributes.put(DEVICE_NAME, "Device Name");
        attributes.put(APPEARANCE, "Appearance");
        attributes.put(PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS, "Peripheral Preferred Connection Parameters");

        attributes.put(MANUFACTURER_NAME_STRING, "Manufacturer Name String");
        attributes.put(MODEL_NUMBER_STRING, "Model Number String");
        attributes.put(SERIAL_NUMBER_STRING, "Serial Number String");
        attributes.put(HARDWARE_REVISION_STRING, "Hardware Revision String");
        attributes.put(FIRMWARE_REVISION_STRING, "Firmware Revision String");
        attributes.put(SOFTWARE_REVISION_STRING, "Software Revision String");

        attributes.put(SERVICE_CHANGED, "Service Changed");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;

    }

}
