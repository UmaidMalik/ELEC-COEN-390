package com.elec_coen_390.uvme;

public class BatteryData {

    private static int batteryLevel = 0;

    public BatteryData(int battery_level) {
        batteryLevel = battery_level;
    }

    public static int getBatteryLevel() {
        return batteryLevel;
    }

    public static void setBatteryLevel(int battery_level) {
        batteryLevel = battery_level;
    }
}
