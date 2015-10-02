#include <SoftwareSerial.h>

//Tx = 0 on LC
//Rx = 1 on LC
int bluetoothTx = 0;  // TX-O pin of bluetooth mate, Arduino D2
int bluetoothRx = 1;  // RX-I pin of bluetooth mate, Arduino D3
int bluetoothInitBaud = 115200;
int bluetoothBaud = 9600;
char BTName[] = "GhostPassword";


SoftwareSerial bluetooth(0,1);
void setup() {
  // put your setup code here, to run once:

  Serial.begin(115200);
        bluetooth.begin(115200);
        //bluetooth.flush();
   delay(200);
   bluetooth.print("$");
   bluetooth.print("$");
   bluetooth.print("$");
   delay(200);
   bluetooth.println("ST,3");
   
   delay(100);
   bluetooth.println("U,9600,N");
   delay(100);
    Serial.print(bluetooth.readString());
  bluetooth.begin(bluetoothBaud);
  Serial.println("Bluetooth Baud set to 9600");
   bluetooth.println("---");
   delay(200);
   bluetooth.print("$");
   bluetooth.print("$");
   bluetooth.print("$");
   delay(300);
   bluetooth.println("SN,GhostPassword");
   delay(300);
   bluetooth.println("---");
   delay(100);
   
}

void loop() {
  // put your main code here, to run repeatedly:

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
