// For storage permanence
#include <EEPROM.h>
// For RTC
#include <Time.h>
#include <DS3232RTC.h>
#include <Wire.h>
// For OTP
#include <TOTP.h>
#include <sha1.h>
// For Bluetooth
#include <SoftwareSerial.h>
// For Pixels
#include <Adafruit_NeoPixel.h>
#include <Arduino.h>  // for type definitions

//Instructions:
// Plug power and ground into all devices
// Additionally, hook the button up to ground
// Pin 0: TX-0 on bluetooth
// Pin 1: RX-0 on bluetooth
// Pin 2: Input pin on led
// Pin 3: Empty
// Pin 4: Empty
// Pin 5: One of the two control pins on the RTC
// Pin 6: The other control pin on the RTC
// Pin 7: One side of the button

#define CONFIG_VERSION 2
#define CONFIG_START 1
#define SETTING_STRING_SIZE 254
#define EEPROM_MIN_ADDR 0
//#define EEPROM_MAX_ADDR 128 // Max size on the Teensy LC
#define EEPROM_MAX_ADDR 1024 // Max size on the Teensy 2


#define BUTTON_PIN 7
#define LED_PIN 2
#define LED_COUNT 1
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(LED_COUNT, LED_PIN, NEO_GRB + NEO_KHZ800);

#define OTP_SETTING_INDEX 0

//Tx = 0 on LC
//Rx = 1 on LC
int bluetoothTx = 0;  // TX-O pin of bluetooth mate, Arduino D2
int bluetoothRx = 1;  // RX-I pin of bluetooth mate, Arduino D3
int bluetoothInitBaud = 115200;
int bluetoothBaud = 9600;
char BTName[] = "GhostPassword";
SoftwareSerial bluetooth(bluetoothTx,bluetoothRx);
String bt_input="";
int receiving_input=0;
int receiving_type=0;
TOTP totp = TOTP({},0);
bool debug_mode = false;

struct SettingsStruct {
  // This detects that they are our stored settings
  uint8_t stringStart;
  byte stringSize;
};
String* settings;
byte settingsCount=0;


void setup() {
  Serial.begin(9600);
  
  //Prep button
  pinMode(BUTTON_PIN, INPUT_PULLUP);
  //pinMode(BUTTON_PWR_PIN, OUTPUT);
  //digitalWrite(BUTTON_PWR_PIN, HIGH);

  if (digitalRead(BUTTON_PIN) == LOW) {
    debug_mode = true;
    Serial.println("Entering debug mode.");
  }
  /*
  settingsCount=1;
  settings = new String[1];
  settings[0]=String("Test");
  saveConfig();
  */
  loadConfig();
  /*
  */
  pixels.begin();
  setColor('o');
  //initialTimeSet();
  // Get your timer detail from the RTC
  setSyncProvider(RTC.get);   // the function to get the time from the RTC
  if(timeStatus() != timeSet) {
      if(debug_mode){
        Serial.println("Unable to sync with the RTC");
      }
      setColor('r');
  }
  else {
      if(debug_mode){
        Serial.println("RTC has set the system time");   
      }
      setColor('g');
  }
  prepareOtp();
  digitalClockDisplay();
  
  // Prep bluetooth
   setColor('b');
   bluetooth.begin(115200);
   delay(200);
   bluetooth.print("$");
   bluetooth.print("$");
   bluetooth.print("$");
   delay(200);
   // This sets the config timeout to 3 seconds, otherwise
   // It takes 60 seconds after bootup
   bluetooth.println("ST,3");
   delay(200);
   bluetooth.println("SN,GhostPassword");
   delay(200);
   bluetooth.println("U,9600,N");
   
   if(debug_mode){
     Serial.print(bluetooth.readString());
   }
   delay(200);
   
   bluetooth.begin(bluetoothBaud);
   
   if(debug_mode){
     Serial.println("Bluetooth Baud set to 9600");
   }
   if(timeStatus() != timeSet) {
      setColor('r');
  } else {
      pixels.setBrightness(16);
      setColor('g'); 
  }
   
}

void loop() {
  // put your main code here, to run repeatedly:
  if(bluetooth.available())  // If the bluetooth sent any characters
  {
    char input = bluetooth.read();
    byte initial_char=0;
    if(receiving_input==0){
      // If we are starting a new string
      if(input==':'){
        receiving_input=1;
        receiving_type=1;
        initial_char=1;
      }
      // If we are starting a new QR code
      if(input=='-'){
        receiving_input=1;
        receiving_type=2;
        initial_char=1;
      }
    } else {
      // If we are in a receiving type
      if(input==':'){
        receiving_input=0;
        processInput(bt_input,receiving_type);
        bt_input="";
        receiving_type=0;
      }
    }
    if(receiving_input==1 && initial_char==0) {
      bt_input = bt_input + input;
    } else {
      initial_char = 0;
    }
    
  }
  
  if (digitalRead(BUTTON_PIN) == LOW) {
    if(debug_mode){
      Serial.println("Button pressed!!!");
      Serial.print("OTP Code: ");
      Serial.println(totp.getCode(now()));
    }
    Keyboard.write(totp.getCode(now()));
    delay(1500);
  } else {
    //Serial.println("Button is not pressed...");
    //delay(100);
  }
}


void processInput(String input_string,int type){
  if(debug_mode){
    Serial.println();
  }
  if(receiving_type==2){
    if(debug_mode){
      Serial.print("QR Code received: ");
      Serial.println(input_string);
      Serial.print("Length: ");
      Serial.println(input_string.length());
    }
    //char otp_seed[] = "R4U3Y7HAL5KYFWNKCOZSKTWQQGSQX6H5TFR7UEKZMMAGGV2YYDB4KZ2SPNB3LZ52"; 
    settings[0] = String(input_string);
    saveConfig();
    // Since we have a new OTP, run the initializer for it
    prepareOtp();
  } 
  if(receiving_type==1){
    if(debug_mode){
      Serial.print("String input received. Typing: ");
      Serial.println(input_string);
    }
    Keyboard.print(input_string);
  }
}


//BEGIN EEPROM stuff
void loadConfig() {
  if(debug_mode){
    Serial.println("Reading from EEPROM...");
  }
  if(EEPROM.read(CONFIG_START + 0) == CONFIG_VERSION) {
    settingsCount=EEPROM.read(CONFIG_START + 1);
    
    unsigned int sSize=sizeof(SettingsStruct);
    //delete[] settings;
    settings = new String[settingsCount];
    for(unsigned int t=0; t<settingsCount; t++){
      SettingsStruct setting;
      unsigned int settingsIndex=CONFIG_START+2+(t*sSize);
      // For each setting stored, read it in
      for(unsigned int j=0; j<sSize; j++){
        // Read in a struct that describes a setting
        *((char*)&setting + j) = EEPROM.read(settingsIndex + j);
      }
      
      settings[t]=String("");
      char input[setting.stringSize];
      for(unsigned int k=0; k<setting.stringSize; k++){
        *((char*)&input + k)  = EEPROM.read(setting.stringStart + k);
      }
      // Use this section to validate reading of settings
      
      if(debug_mode){
        Serial.print("For setting: ");
        Serial.print(t);
        Serial.print(" we read: ");
        Serial.println(input);
      }
      settings[t]=String(input);
    }
  } else {
    if(debug_mode){
      Serial.println("No settings to load");
    }
    settings = new String[0];
    settings[0] = "";
  }
}
void saveConfig() {
  if(debug_mode){
    Serial.println("Writing settings to EEPROM...");
  }
  EEPROM.write(CONFIG_START + 0, CONFIG_VERSION);
  EEPROM.write(CONFIG_START + 1, settingsCount);
  byte sSize=sizeof(SettingsStruct);
  unsigned int stringStartIndex=CONFIG_START+2+settingsCount*sSize;
  for(unsigned int t=0; t<settingsCount; t++){
    unsigned int settingIndex=CONFIG_START+2+(t*sSize);
    byte slen = settings[t].length();
    SettingsStruct thisSetting = {
      stringStartIndex, slen
    };
    char buffer[settings[t].length()+1];
    settings[t].toCharArray(buffer,settings[t].length()+1);
    for(unsigned int j=0; j<sSize; j++){
      EEPROM.write(settingIndex+j, *((char*)&thisSetting + j));
    }
    for(unsigned int m=0; m<slen; m++){
      EEPROM.write(stringStartIndex+m, *((char*)&buffer + m));
    }
    
    stringStartIndex += slen + 1;
  }
}
//END EEPROM stuff

//BEGIN RTC stuff

void initialTimeSet(){
  // This code is only needed for the initial sync
  // Once set, it should be commented out.
  // Remember that your time should be UTC (I think)
  int year = 2015;
  int month = 10;
  int day = 13;
  int hour = 10+6;
  int minute = 8;
  int second = 50;
  setTime(hour, minute, second, day, month, year);   //set the system time to 23h31m30s on 13Feb2009
  RTC.set(now());                     //set the RTC from the system time
}

void digitalClockDisplay(void)
{
  if(debug_mode){
    // digital clock display of the time
    Serial.print(hour());
    printDigits(minute());
    printDigits(second());
    Serial.print(' ');
    Serial.print(day());
    Serial.print(' ');
    Serial.print(month());
    Serial.print(' ');
    Serial.print(year()); 
    Serial.println();
  } 
}
void printDigits(int digits)
{
  if(debug_mode){
    // utility function for digital clock display: prints preceding colon and leading 0
    Serial.print(':');
    if(digits < 10) {
        Serial.print('0');
    }
    Serial.print(digits);
  }
}
///END RTC Stuff


//// BEGIN OTP Stuff
void prepareOtp(){
  //Set setting from eeprom
  unsigned int l=settings[OTP_SETTING_INDEX].length();
  char otp_seed[l];
  settings[OTP_SETTING_INDEX].toCharArray(otp_seed,l+1); // Arraysize + 1 for proper extraction
  int decoded_size=0;
  uint8_t* decoded_seed;
  base32decode(otp_seed, sizeof(otp_seed), decoded_seed, decoded_size);
  totp = TOTP(decoded_seed, decoded_size);
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


//// END OTP Stuff

//// BEGIN Pixel color code:

void setColor(char color){
  byte val=50;
  switch(color){
    case 'o': val=20;
      break;
    case 'r': val=254;
      break;
    case 'b': val=169;
      break;
    case 'g': val=84;
      break;
  }
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
//// END Pixel stuff
