#include <Adafruit_ATParser.h>
#include <Adafruit_BLE.h>
#include <Adafruit_BLEBattery.h>
#include <Adafruit_BLEEddystone.h>
#include <Adafruit_BLEGatt.h>
#include <Adafruit_BLEMIDI.h>
#include <Adafruit_BluefruitLE_SPI.h>
#include <Adafruit_BluefruitLE_UART.h>

    /* 
 ML8511 UV Sensor Read Example
 By: Nathan Seidle
 SparkFun Electronics
 Date: January 15th, 2014
 License: This code is public domain but you buy me a beer if you use this and we meet someday (Beerware license).

 The ML8511 UV Sensor outputs an analog signal in relation to the amount of UV light it detects.

 Connect the following ML8511 breakout board to Arduino:
 3.3V = 3.3V
 OUT = A0
 GND = GND
 EN = 3.3V
 3.3V = A1
 These last two connections are a little different. Connect the EN pin on the breakout to 3.3V on the breakout.
 This will enable the output. Also connect the 3.3V pin of the breakout to Arduino pin 1.

 This example uses a neat trick. Analog to digital conversions rely completely on VCC. We assume
 this is 5V but if the board is powered from USB this may be as high as 5.25V or as low as 4.75V:
 http://en.wikipedia.org/wiki/USB#Power Because of this unknown window it makes the ADC fairly inaccurate
 in most cases. To fix this, we use the very accurate onboard 3.3V reference (accurate within 1%). So by doing an
 ADC on the 3.3V pin (A1) and then comparing this against the reading from the sensor we can extrapolate
 a true-to-life reading no matter what VIN is (as long as it's above 3.4V).

 Test your sensor by shining daylight or a UV LED: https://www.sparkfun.com/products/8662

 This sensor detects 280-390nm light most effectively. This is categorized as part of the UVB (burning rays)
 spectrum and most of the UVA (tanning rays) spectrum.

 There's lots of good UV radiation reading out there:
 http://www.ccohs.ca/oshanswers/phys_agents/ultravioletradiation.html
 https://www.iuva.org/uv-faqs

*/



//Hardware pin definitions

#define BLUEFRUIT_SPI_CS               8
#define BLUEFRUIT_SPI_IRQ              7
#define BLUEFRUIT_SPI_RST              4

#define BLUEFRUIT_SPI_SCK              15
#define BLUEFRUIT_SPI_MISO             14
#define BLUEFRUIT_SPI_MOSI             16


int UVOUT = A0; //Output from the sensor
int REF_3V3 = A1; //3.3V power on the Arduino board

#define VBATPIN A9
   
// SETUP ADAFRUIT BLUEFRUITLE SPI FRIEND
Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);


// Global variables to keep track of time
unsigned long StartTime;
unsigned long TimeReference;

int count = 0;

/* UV SENSOR SERIVCE ITEMS
 * 
 * ----------------------- */

 int32_t uvServiceId;
 int32_t uvIntensityCharId;
 //int32_t uvTimeCharId;


 /* Function: error
  * 
  * Helper function 
  * 
  * Returns: n/a - void. Error message is printed serially
  * 
  */
void error(const __FlashStringHelper * err) {

  Serial.println(err);
  while(1);
}

void updateIntCharacteristic(String nameOfChar, float characteristic, int32_t charId) {

  Serial.print("Byte size of ");
  Serial.print(nameOfChar);
  Serial.print(" : ");
  Serial.println(sizeof(characteristic));

  String msg = String(characteristic);

  ble.print( F("AT+GATTCHAR=") );
  ble.print( charId );
  ble.print( F(",") );
  ble.println(msg);

  Serial.print("Actual value of ");
  Serial.print(nameOfChar);
  Serial.print(" : ");
  Serial.println(characteristic);
  if ( !ble.waitForOK() ) Serial.println(F("Failed to get response!"));
  Serial.println("");
  Serial.println("");
}

/* Function: emitUVSensorData
 * 
 * uvIntensity = UV index reading
 * 
 * returns: n/a - void
 * */
void emitUVSensorData(float uvIntensity) {

  updateIntCharacteristic("UV Intensity", uvIntensity, uvIntensityCharId);
  //updateIntCharacteristic("UV Sensor Time", counter, int(uvTimeReference), uvTimeCharId);
  

}



void setup()
{
  // SETUP BLE
  delay(500);
  boolean success;
  Serial.begin(115200);

  pinMode(UVOUT, INPUT);
  pinMode(REF_3V3, INPUT);

  if ( !ble.begin(false) ) error(F ("Couldn't find Bluefruit, makey sure it is in Command mode"));

  if ( !ble.factoryReset() ) error(F("Couldn't factory reset"));
  ble.echo(false);
  ble.info();

  
  
  if (! ble.sendCommandCheckOK(F("AT+GAPDEVNAME=UVMe UV Sensor Adafruit Feather BLE Arduino Hardware")) ) error(F("Could not set device name?"));


  // SETUP UV SENSOR SERVICE & CHARACTERISTICS
  success = ble.sendCommandWithIntReply( F("AT+GATTADDSERVICE=UUID128=00-00-00-01-A9-A6-4E-69-87-BD-29-12-35-71-61-B3"), &uvServiceId);
  if (!success) error(F("Could not add UV Sensor service"));

  //success = ble.sendCommandWithIntReply( F("AT+GATTADDCHAR=UUID=0x0010,PROPERTIES=0x02,MIN_LEN=1,MAX_LEN=20,VALUE=0"), &uvIntensityCharId);
  //if (! success) error(F("Could not add UV Intensity characteristic"));

  //success = ble.sendCommandWithIntReply( F("AT+GATTADDCHAR=UUID=0x0003,PROPERTIES=0x2,MIN_LEN=1,MAX_LEN=20,VALUE=0"), &uvTimeCharId);
  //if (! success) error(F("Could not add UV Sensor Time characteristic"));

  success = ble.sendCommandWithIntReply( F("AT+GATTADDCHAR=UUID128=00-00-00-02-A9-A6-4E-69-87-BD-29-12-35-71-61-B3, PROPERTIES=0x10, MIN_LEN=1, MAX_LEN=20,VALUE=0,DATATYPE= 1"), &uvIntensityCharId);
  if (! success) error(F("Could not add UV Intensity characteristic"));

  //success = ble.sendCommandWithIntReply( F("AT+GATTADDCHAR=UUID128=00-00-00-03-A9-A6-4E-69-87-BD-29-12-35-71-61-B3, PROPERTIES=0x2, MIN_LEN=1, MAX_LEN=20,VALUE=0,DATATYPE=1"), &uvTimeCharId);
  //if (! success) error(F("Could not add UV Sensor Time characteristic"));

  
  ble.reset();
  

  Serial.println("UVMe - UV Sensor: ML8511 ");
}

void loop()
{
  //StartTime = millis();
  //TimeReference = millis();

  //count = 0;

  
  int uvLevel = averageAnalogRead(UVOUT);
  int refLevel = averageAnalogRead(REF_3V3);

  //Use the 3.3V power pin as a reference to get a very accurate output value from sensor
  float outputVoltage = 3.3 / refLevel * uvLevel;

  float uvIntensity = mapfloat(outputVoltage, 0.99, 2.8, 0.0, 15.0); //Convert the voltage to a UV intensity level

  float measuredvbat = analogRead(VBATPIN);
  measuredvbat *= 2;    // we divided by 2, so multiply back
  measuredvbat *= 3.3;  // Multiply by 3.3V, our reference voltage
  measuredvbat /= 1024; // convert to voltage


  emitUVSensorData(uvIntensity);

  //count++;

  /*
  Serial.print("VBat: " ); Serial.println(measuredvbat);

  Serial.print("output: ");
  Serial.print(refLevel);

  Serial.print(" ML8511 output: ");
  Serial.print(uvLevel);

  Serial.print(" / ML8511 voltage: ");
  Serial.print(outputVoltage);

  Serial.print(" / UV Intensity (mW/cm^2): ");
  Serial.print(uvIntensity);

  Serial.println();
  */
  

  delay(100);
}

//Takes an average of readings on a given pin
//Returns the average
int averageAnalogRead(int pinToRead)
{
  byte numberOfReadings = 8;
  unsigned int runningValue = 0; 

  for(int x = 0 ; x < numberOfReadings ; x++)
    runningValue += analogRead(pinToRead);
  runningValue /= numberOfReadings;

  return(runningValue);  
}

//The Arduino Map function but for floats
//From: http://forum.arduino.cc/index.php?topic=3922.0
float mapfloat(float x, float in_min, float in_max, float out_min, float out_max)
{
  //outputVoltage, 0.99, 2.8, 0.0, 15.0
  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}
