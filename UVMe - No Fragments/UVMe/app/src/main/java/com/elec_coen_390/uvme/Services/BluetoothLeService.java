package com.elec_coen_390.uvme.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.elec_coen_390.uvme.BatteryData;
import com.elec_coen_390.uvme.GattAttributes;
import com.elec_coen_390.uvme.UVSensorData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.elec_coen_390.uvme.GattAttributes.UV_INTENSITY_READ;
import static com.elec_coen_390.uvme.GattAttributes.BATTERY_LEVEL_READ;

public class BluetoothLeService extends Service {

    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    static public BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    BluetoothGattCharacteristic UVIntensityCharacteristic;
    BluetoothGattCharacteristic batteryLevelCharacteristic;
    List<BluetoothGattCharacteristic> characteristicsList = new ArrayList<>();

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private BluetoothGattCharacteristic mNotifyCharacteristic;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    public final static String EXTRA_DATA_VALUE =
            "com.example.bluetooth.le.EXTRA_DATA_VALUE";

    public final static String EXTRA_DATA_VALUE_UV_INDEX =
            "com.example.bluetooth.le.EXTRA_DATA_VALUE_UV_INDEX";

    public final static String EXTRA_DATA_VALUE_BATTERY_LEVEL =
            "com.example.bluetooth.le.EXTRA_DATA_VALUE_BATTERY_LEVEL";



    public final static UUID UUID_CLIENT_CHARACTERISTIC_CONFIG =
            UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG);

    public final static UUID UUID_CLIENT_CHARACTERISTIC_CONFIG2 =
            UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG2);

    public final static UUID UUID_UV_INTENSITY_MEASUREMENT_NOTIFY =
            UUID.fromString(GattAttributes.UV_SENSOR_INTENSITY_MEASUREMENT_NOTIFY);

    public final static UUID UUID_BATTERY_LEVEL =
            UUID.fromString(GattAttributes.BATTERY_LEVEL_CHARACTERISTIC);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                List<BluetoothGattService> services = gatt.getServices();

                // Loops through available GATT Services.
                for (BluetoothGattService gattService : services) {
                    List<BluetoothGattCharacteristic> gattCharacteristicsList = gattService.getCharacteristics();

                    // Loops through available Characteristics.
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristicsList) {

                        if (isDataCharacteristic(gattCharacteristic) != 0) {
                            characteristicsList.add(gattCharacteristic);
                            broadcastUpdate(ACTION_DATA_AVAILABLE, gattCharacteristic);
                        }


                        /*
                        if (UUID_UV_INTENSITY_MEASUREMENT_NOTIFY.equals(gattCharacteristic.getUuid())) {
                            UVIntensityCharacteristic = gattCharacteristic;
                            Log.w(TAG, "UV Intensity characteristic found");
                            broadcastUpdate(ACTION_DATA_AVAILABLE, UVIntensityCharacteristic);
                        }
                        if (UUID_BATTERY_LEVEL.equals(gattCharacteristic.getUuid())) {
                            batteryLevelCharacteristic = gattCharacteristic;
                            Log.w(TAG, "Battery Level characteristic found");
                            broadcastUpdate(ACTION_DATA_AVAILABLE, batteryLevelCharacteristic);
                        }
                            */

                    }
                }

                requestCharacteristic(gatt);


            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        public void requestCharacteristic(BluetoothGatt gatt) {
            gatt.readCharacteristic(characteristicsList.get(characteristicsList.size()-1));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }

        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);


        }


    };



    private int isDataCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (UUID_BATTERY_LEVEL.equals(characteristic.getUuid())) {
            return BATTERY_LEVEL_READ;

        } else if (UUID_UV_INTENSITY_MEASUREMENT_NOTIFY.equals(characteristic.getUuid())) {
            return UV_INTENSITY_READ;

        } else {
            return 0;
        }
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {

        final Intent intent = new Intent(action);
        int characteristicIDSet = isDataCharacteristic(characteristic);

        switch (characteristicIDSet) {

            case UV_INTENSITY_READ:
                setCharacteristicNotification(characteristic, true);
                final byte[] dataUV = characteristic.getValue();
                if (dataUV != null && dataUV.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(dataUV.length);
                    for (byte byteChar : dataUV)
                        stringBuilder.append(String.format("%02X", byteChar));
                    //intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
                    intent.putExtra(EXTRA_DATA_VALUE_UV_INDEX, new String(dataUV));
                    intent.putExtra(EXTRA_DATA_VALUE, new String(dataUV));
                    //if (characteristic.getUuid() == UUID_UV_INTENSITY_MEASUREMENT_NOTIFY)
                    UVSensorData.setUVIntensity(Float.parseFloat(new String(dataUV)));

                }
                break;



            case BATTERY_LEVEL_READ:
                //setCharacteristicNotification(characteristic, true);
                final byte[] dataBattery = characteristic.getValue();
                if (dataBattery  != null && dataBattery .length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(dataBattery.length);
                    for (byte byteChar : dataBattery )
                        stringBuilder.append(String.format("%02X", byteChar));
                    intent.putExtra(EXTRA_DATA_VALUE_BATTERY_LEVEL, new String(dataBattery ));
                    intent.putExtra(EXTRA_DATA_VALUE, new String(dataBattery));
                    BatteryData.setBatteryLevel(Float.parseFloat(new String(dataBattery)));

                }
                break;





            default:
                final byte[] dataDefault = characteristic.getValue();

                if (dataDefault != null && dataDefault.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(dataDefault.length);
                    for (byte byteChar : dataDefault)
                        stringBuilder.append(String.format("%02X", byteChar));
                    intent.putExtra(EXTRA_DATA, new String(dataDefault) + "\n" + stringBuilder.toString());
                    intent.putExtra(EXTRA_DATA_VALUE, new String(dataDefault));
                    //BatteryData.setBatteryLevel(Float.parseFloat(new String(dataDefault)));
                }
                break;
        }

        /*
        if (UUID_UV_INTENSITY_MEASUREMENT_NOTIFY.equals(characteristic.getUuid())) {
            setCharacteristicNotification(characteristic, true);
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X", byteChar));
                //intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
                intent.putExtra(EXTRA_DATA_VALUE_UV_INDEX, new String(data));
                //if (characteristic.getUuid() == UUID_UV_INTENSITY_MEASUREMENT_NOTIFY)
                UVSensorData.setUVIntensity(Float.parseFloat(new String(data)));
            }
        }


        if (UUID_BATTERY_LEVEL.equals(characteristic.getUuid())) {
            setCharacteristicNotification(characteristic, true);
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X", byteChar));
                //intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
                intent.putExtra(EXTRA_DATA_VALUE_BATTERY_LEVEL, new String(data));
                //if (characteristic.getUuid() == UUID_UV_INTENSITY_MEASUREMENT_NOTIFY)
                BatteryData.setBatteryLevel(Integer.parseInt(new String(data)));
            }
        }


        else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
                intent.putExtra(EXTRA_DATA_VALUE, new String(data));

            }
        }

         */



        sendBroadcast(intent);

    }



    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device. Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found. Unable to connect.");
            return false;
        }

        // We want to directly connect to the device,
        // so we are setting the autoConnect parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device,the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;

        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }



    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);


        if (UUID_UV_INTENSITY_MEASUREMENT_NOTIFY.equals(characteristic.getUuid())) {

            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID_CLIENT_CHARACTERISTIC_CONFIG);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);

        }



        if (UUID_BATTERY_LEVEL.equals(characteristic.getUuid())) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID_CLIENT_CHARACTERISTIC_CONFIG);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);



            mBluetoothGatt.writeDescriptor(descriptor);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



    }
    
    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    @Override
    public boolean stopService(Intent name) {
        disconnect();
        return super.stopService(name);
    }




}