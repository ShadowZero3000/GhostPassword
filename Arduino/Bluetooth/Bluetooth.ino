#include <SoftwareSerial.h>

SoftwareSerial mySerial(0,1);

void setup() {
  // put your setup code here, to run once:
Serial.begin(38400);
while(!Serial){;}
Serial.println("Goodnight moon!");
mySerial.begin(9600);
  Serial.println("A");
mySerial.print("AT+ROLE1");
mySerial.print("AT+START");
  Serial.println("A");
delay(1000);
}
void at(char* cmd){
  Serial.print(cmd);
  mySerial.write(cmd);
  int count=0;
  while(!mySerial.find("OK")) {
    Serial.print(".");
    count++;
    if(count>5){
      break;
    }
  }
}
void loop() {
at("AT");
  Serial.println("Alive");
  // put your main code here, to run repeatedly:
mySerial.print("test I am slavetest I am slavetest I am slavetest I am slavetest I am slavetest I am slavetest I am slavetest I am slave ");
delay(2000);
mySerial.print("AT");
mySerial.listen();
if(mySerial.available()){
  Serial.println("Data received");
  Serial.write(mySerial.read());
}
if(Serial.available()){
  mySerial.write(Serial.read());
  Serial.println("Written");
}
}
