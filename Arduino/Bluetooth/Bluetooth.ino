#include <Adafruit_NeoPixel.h>

//#include <AltSoftSerial.h>

#include <SoftwareSerial.h>
#define LED_PIN 10
#define LED_COUNT 1
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(LED_COUNT, LED_PIN, NEO_GRB + NEO_KHZ800);

//Tx = 0 on LC
//Rx = 1 on LC
int bluetoothTx = 0;  // TX-O pin of bluetooth mate, Arduino D2
int bluetoothRx = 1;  // RX-I pin of bluetooth mate, Arduino D3
int bluetoothInitBaud = 115200;
int bluetoothBaud = 115200; //9600;
//#define bluetooth Serial1
SoftwareSerial bluetooth(bluetoothTx,bluetoothRx);
//AltSoftSerial bluetooth; //(bluetoothTx, bluetoothRx);
//HardwareSerial bluetooth = HardwareSerial();
void setup() {
  // put your setup code here, to run once:
  //pixels.setPixelColor(0, pixels.Color(0,150,0));
  //pixels.show();
  //pixels.show();
Serial.begin(bluetoothInitBaud);
//Serial.begin(bluetoothInitBaud);
while(!Serial){;}
  pixels.begin();
  setColor('r');
  //pixels.setPixelColor(0, pixels.Color(50,150,0));
  //pixels.show();
Serial.println("Goodnight moon!");
bluetooth.begin(bluetoothInitBaud);
//delay(200);
bluetooth.print("$"); // Print three times individually
bluetooth.print("$");
bluetooth.print("$"); // Enter command mode
delay(200);
//bluetooth.println("U,9600,N");
delay(200);
//bluetooth.begin(bluetoothBaud);
  //Serial.println("Bluetooth Baud set to 9600");
bluetooth.print("$"); // Print three times individually
bluetooth.print("$");
bluetooth.print("$"); // Enter command mode
delay(100);
bluetooth.println();
bluetooth.println("SN,Josh's Marbles");
delay(100);
bluetooth.println("---");

}
void loop() {
  /*setColor('r');
  delay(1000);
  setColor('g');
  delay(1000);
  setColor('b');
  delay(1000);
  */
  if(bluetooth.available())  // If the bluetooth sent any characters
  {
    // Send any characters the bluetooth prints to the serial monitor
    Serial.print((char)bluetooth.read());  
  }
  if(Serial.available())  // If stuff was typed in the serial monitor
  {
    // Send any characters the Serial monitor prints to the bluetooth
    bluetooth.print((char)Serial.read());
    ///Serial.println("Got input");
  }
}
void setColor(char color){
  byte val=50;
  switch(color){
    case 'r': val=254;
      break;
    case 'b': val=169;
      break;
    case 'g': val=84;
      break;
  }
  //Serial.print("Color: ");
  //Serial.println(val);
  pixels.setPixelColor(0, Wheel(val));
  pixels.show();
}
uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if(WheelPos < 85) {
    return pixels.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  }
  if(WheelPos < 170) {
    WheelPos -= 85;
    return pixels.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  }
  WheelPos -= 170;
  return pixels.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
}
