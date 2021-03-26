package com.elec_coen_390.uvme;

public class BatteryData {

    private static float batteryLevel = 0;

    public BatteryData(float battery_level) {
        batteryLevel = battery_level;
    }

    public static float getBatteryLevel() {
        return batteryLevel;
    }

    public static void setBatteryLevel(float battery_level) {
        batteryLevel = battery_level;
    }
}
