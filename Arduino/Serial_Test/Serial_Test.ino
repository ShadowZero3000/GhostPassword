#include <SoftwareSerial.h>

//#define HWSERIAL Serial1
SoftwareSerial test(0,1);
void setup() {
  Serial.begin(115200);
        test.begin(115200);
   test.print("$");
   test.print("$");
   test.print("$");
}

void loop() {
        int incomingByte;
        
  if (Serial.available() > 0) {
    incomingByte = (char)Serial.read();
    Serial.print("USB received: ");
    Serial.println((char)incomingByte, DEC);
                test.print("USB received:");
                test.println(incomingByte, DEC);
  }
  if (test.available() > 0) {
    incomingByte = test.read();
    Serial.print("UART received: ");
    Serial.println(incomingByte);
                test.print("UART received:");
                test.println(incomingByte, DEC);
  }
}
