#include <sha1.h>

#include <TOTP.h>

#include <Time.h>



#include <EEPROMex.h>
#include <EEPROMVar.h>
/* Pushbutton, Teensyduino Tutorial #3
   http://www.pjrc.com/teensy/tutorial3.html

   This example code is in the public domain.
*/
uint8_t my_var;
void setup() {                
  Serial.begin(38400);
  pinMode(7, INPUT);
  delay(1000);
  Serial.println("Beginning");
  //my_var=EEPROM.readByte(0);
  //setTime(17+6,34,28,30,9,2015);
  setTime(48121948);
  
}
void printHash(uint8_t* hash) {
  int i;
  for (i=0; i<20; i++) {
    Serial.print("0123456789abcdef"[hash[i]>>4]);
    Serial.print("0123456789abcdef"[hash[i]&0xf]);
  }
  Serial.println();
}

void loop()  {
  Serial.println("Looping");
  
  /*Serial.println(my_var);
  if (digitalRead(7) == HIGH) {
    Serial.println("Button is not pressed...");
  } else {
    Serial.println("Button pressed!!!");
  }*/
  uint8_t hmacKey[] = {0x48, 0x60, 0xC9, 0x09, 0x22, 0x95, 0x38, 0xC1, 0xFB, 0xEC, 0x56, 0x3A, 0x14, 0x00, 0x17, 0x50, 0x33, 0xCC, 0x10, 0x05, 0x56, 0x54, 0x91, 0x53, 0x1C, 0x94, 0x80, 0x58, 0x8D, 0xF6, 0x55, 0x61, 0xC0, 0xE8, 0x02, 0x51, 0x11, 0x43, 0x93, 0x28, 0x03, 0xFB, 0xDF, 0x7E, 0x0B, 0x44, 0xD6, 0x79};
  /*uint8_t str[] = "SGDJCSKVOMH77FY6FAAXUDPMEAVWVJFTHJSAWI32VWHA6AJREUOTKAP7334LRNZ5";
  //uint8_t str[] = "test";
  uint8_t hmacKey[sizeof(str)-1];
  for(int i=0; i<sizeof(hmacKey); i++){
    hmacKey[i]=str[i];
  }
  */
  //uint8_t* hmacKey = str;
 /*Serial.println("Test: FIPS 198a A.1");
  Serial.println("Expect:52bdf679ea68e5043d6fab011c0c92dc1a76bc4a");
  Serial.print("Result:");
  Sha1.initHmac(hmacKey,64);
  Sha1.print("test");
  Serial.print("Result: ");
  Serial.println(sizeof(Sha1.resultHmac()));
  printHash(Sha1.resultHmac());
  Serial.println();
  */
  //Serial.write(hmacKey);
  
  //long unsigned int hmacKey = strtoul(str,0,6);
  //Serial.println(hmacKey);
//uint8_t hmacKey[] = {0x4d, 0x79, 0x4c, 0x65, 0x67, 0x6f, 0x44, 0x6f, 0x6f, 0x72};
  TOTP totp = TOTP(hmacKey, sizeof(hmacKey));
  long timer = now();
  Serial.println(totp.getCode(timer));
  long a = now();
  Serial.println(now());
  delay(1000);
}

uint8_t x2i(char *s) 
{
 uint8_t x = 0;
 for(;;) {
   char c = *s;
   if (c >= '0' && c <= '9') {
      x *= 16;
      x += c - '0'; 
   }
   else if (c >= 'A' && c <= 'F') {
      x *= 16;
      x += (c - 'A') + 10; 
   }
   else break;
   s++;
 }
 return x;
}

