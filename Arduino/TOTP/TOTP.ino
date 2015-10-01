#include <TOTP.h>

#include <Time.h>

#include <sha1.h>

void setup() {
  // put your setup code here, to run once:
      
  Serial.begin(9600);
  Serial.println("Beginning");
  setTime(1443716056);
}

void loop() {
  // put your main code here, to run repeatedly:
  //char google_code[]="SGDJCSKVOMH77FY6FAAXUDPMEAVWVJFTHJSAWI32VWHA6AJREUOTKAP7334LRNZ5";
  char google_code[]="OW5A3VP7R7FUMO66DSJDN3V2R7XOIX2RPBPDICB7R5MJRT4S5S4OBT65XGQOCC64";
  /*uint8_t actual_key[] = { 0x91, 0x86, 0x91, 0x49, 0x55, 0x73, 0x0f, 0xff, 0x97, 0x1e, 0x28, 0x01, 0x7a, 0x0d, 0xec, 0x20, 0x2b, 0x6a, 0xa4, 0xb3, 0x3a, 0x64, 0x0b, 0x23, 0x7a, 0xad, 0x8e, 0x0f, 0x01, 0x31, 0x25, 0x1d, 0x35, 0x01, 0xff, 0xde, 0xf8, 0xb8, 0xb7, 0x3d};
  
  Serial.print("Actual: ");
  for(int i=0; i<sizeof(actual_key); i++){
    Serial.print("0x");
    Serial.print(actual_key[i], HEX);
    Serial.print(" ");
  }
  Serial.println();
  */
  int decoded_size=0;
  uint8_t* decoded;
  base32decode(google_code, sizeof(google_code), decoded, decoded_size);
  /*Serial.println();
  Serial.print("Decoded: ");
  for(int i=0; i<decoded_size; i++){
    Serial.print("0x");
    Serial.print(decoded[i], HEX);
    Serial.print(" ");
  }
  Serial.println();*/
  Serial.print("TOTP: ");
  TOTP totp = TOTP(decoded, decoded_size);
  Serial.println(totp.getCode(now()));
  delay(10000);
}


char keyString[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
int getIndex(char val){
  for (int ind = 0; ind < 32; ind++){
    if (val == keyString[ind]){
      return ind;
    }
  } 
  return 0;
}
void base32decode(char input[], int inputLen, uint8_t* &output, int &output_size){
  int buffer = 0;
  int bitsLeft = 0;
  int i = 0;
  int count = 0;
  uint8_t* temp_array = new uint8_t[inputLen];
  while (i < inputLen){
    int val = getIndex(input[i++]);
    if (val >= 0 && val < 32) {
      buffer <<= 5;
      buffer |= val;
      bitsLeft += 5;
      if (bitsLeft >= 8) {
        temp_array[count]=buffer >> (bitsLeft - 8) & 0xFF;
        bitsLeft -= 8;
        count++;
      }
     }
  }
  
  output = new uint8_t[count];
  for(int i=0; i<count; i++){
    output[i] = temp_array[i];
  }
  output_size = count;
}
