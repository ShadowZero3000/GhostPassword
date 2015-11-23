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
// #include <SoftwareSerial.h>
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

#define CONFIG_VERSION 1
#define CONFIG_START 1
// #define SETTING_STRING_SIZE 254
// #define EEPROM_MIN_ADDR 0
// Not used, but here for reference if we chose to.
//#define EEPROM_MAX_ADDR 128 // Max size on the Teensy LC
//#define EEPROM_MAX_ADDR 1024 // Max size on the Teensy 2


#define BUTTON_PIN 7
#define LED_PIN 2
#define LED_COUNT 1
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(LED_COUNT, LED_PIN, NEO_GRB + NEO_KHZ800);

#define OTP_SETTING_INDEX 0
#define MAX_BT_INPUT 256

// LC can only hold one, the others could hold more
#define MAX_SETTINGS 1

//Tx = 0 on LC
//Rx = 1 on LC
#define bluetoothTx 0 //;  // TX-O pin of bluetooth mate, Arduino D2
#define bluetoothRx 1 //;  // RX-I pin of bluetooth mate, Arduino D3
// int bluetoothInitBaud = 115200;
const int bluetoothBaud = 9600;
//char BTName[] = "GhostPassword";
//SoftwareSerial bluetooth(bluetoothTx,bluetoothRx);
#define bluetooth Serial1
char bt_input[256];
int bt_input_length=0;
int receiving_input=0;
int receiving_type=0;
TOTP totp = TOTP({},0);
bool debug_mode = true;

struct SettingsStruct {
  // This detects that they are our stored settings
  uint8_t stringStart;
  byte stringSize;
};
String settings[MAX_SETTINGS];
byte settingsCount=0;


void setup() {
  Serial.begin(9600);
  delay(500);
  //Prep button
  pinMode(BUTTON_PIN, INPUT_PULLUP);

  if (digitalRead(BUTTON_PIN) == LOW) {
    debug_mode = true;
    log_line("Entering debug mode.");
  }
  loadConfig();
  log_line("Settings loaded");

  pixels.begin();
  setColor('o');
  //initialTimeSet();
  // Get your timer detail from the RTC
  setSyncProvider(RTC.get);   // the function to get the time from the RTC  
  if(timeStatus() != timeSet) {
      log_line("Unable to sync with the RTC");
      setColor('r');
  }
  else {
      log_line("RTC has set the system time");   
      setColor('g');
  }
  prepareOtp();
  digitalClockDisplay();
  
  // Prep bluetooth
  prepBluetooth();
  if(timeStatus() != timeSet) {
    setColor('r');
  } else {
    pixels.setBrightness(16);
    setColor('g'); 
  }
}

void prepBluetooth(){
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
  
  log_string(bluetooth.readString());
  
  delay(200);
  
  bluetooth.begin(bluetoothBaud);
  log_line("Bluetooth Baud set to 9600");
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
      // If we are starting a new timestamp
      if(input=='@'){
        receiving_input=1;
        receiving_type=3;
        initial_char=1;
      }
    } else {
      // If we are in a receiving type
      if(input==':'){
        receiving_input=0;
        processInput(receiving_type);
        bt_input_length=0;
        receiving_type=0;
      }
    }
    if(receiving_input==1 && initial_char==0) {
      bt_input[bt_input_length] = input;
      bt_input_length++;
    } else {
      initial_char = 0;
    }
  }
  
  if (digitalRead(BUTTON_PIN) == LOW) {
    log_line("Button pressed!!!");
    digitalClockDisplay();
    log_noline("OTP Code: ");
    log_line(totp.getCode(now()));
    Keyboard.write(totp.getCode(now()));
    delay(1500);
  } else {
    //log_line("Button is not pressed...");
    //delay(100);
  }
}


void processInput(int type){
  char input_string[bt_input_length];
  for(int i=0; i<bt_input_length; i++){
    input_string[i]=bt_input[i];
  }
  log_line();
  log_line("---");
  //If getting time
  if(receiving_type==3){
    log_noline("Time received: ");
    log_line(input_string);
    log_noline("Length: ");
    log_line(bt_input_length);
    //Shave off the millis
    char time_string[bt_input_length - 3];
    for (int i=0; i<bt_input_length - 3; i++){
      time_string[i] = input_string[i];
    }
    //long  l = input_string[0] | (input_string[1] << 8) | (input_string[2] << 16) | (input_string[3] << 24);
    setTime(atol(time_string));
    RTC.set(now());
    log_line("Time set");
    
    digitalClockDisplay();
  } 
  if(receiving_type==2){
    log_noline("QR Code received: ");
    log_line(input_string);
    log_noline("Length: ");
    log_line(bt_input_length);
    //char otp_seed[] = "R4U3Y7HAL5KYFWNKCOZSKTWQQGSQX6H5TFR7UEKZMMAGGV2YYDB4KZ2SPNB3LZ52"; 
    //delete settings[0];
    //settings = new String[1];
    settings[0] = String(input_string);
    settingsCount = 1;
    saveConfig();
    log_line("Otp preparing");
    log_string(settings[0]);
    // Since we have a new OTP, run the initializer for it
    prepareOtp();
    log_line("OTP Prepped");
  } 
  if(receiving_type==1){
    log_noline("String input received. Typing: ");
    log_line(input_string);
    Keyboard.print(input_string);
  }
}


//BEGIN EEPROM stuff
void loadConfig() {
  log_line("Reading from EEPROM...");
  if(EEPROM.read(CONFIG_START + 0) == CONFIG_VERSION) {
    settingsCount=EEPROM.read(CONFIG_START + 1);
    
    unsigned int sSize=sizeof(SettingsStruct);
    for(unsigned int t=0; t<settingsCount; t++){
      SettingsStruct setting;
      unsigned int settingsIndex=CONFIG_START+2+(t*sSize);
      // For each setting stored, read it in
      for(unsigned int j=0; j<sSize; j++){
        // Read in a struct that describes a setting
        *((char*)&setting + j) = EEPROM.read(settingsIndex + j);
      }
      
      settings[t]=String("");
      char* input = new char[setting.stringSize];
      log_noline("String size: ");
      log_line(setting.stringSize);
      for(unsigned int j=0; j<setting.stringSize; j++){
        input[j]=0;
      }
      settings[t]=String("");
      for(unsigned int k=0; k<setting.stringSize; k++){
        //input[k]  = (char)EEPROM.read(setting.stringStart + k);
        settings[t] = settings[t] + (char)EEPROM.read(setting.stringStart + k);
      }
      // Use this section to validate reading of settings
    
      log_noline("For setting: ");
      log_noline(t);
      log_noline(" we read: ");
      log_line(input);
      log_noline("String length: ");
      log_string(settings[t]);
      //settings[t]=String(input, BIN);
      log_noline("String length: ");
      log_line(settings[t].length());
    }
  } else {
    log_line("No settings to load");
    //delete[] settings;
    //settings = new String[1];
    settings[0] = "";
    settingsCount = 1;
  }
}
void saveConfig() {
  log_line("Writing settings to EEPROM...");
  EEPROM.write(CONFIG_START + 0, CONFIG_VERSION);
  EEPROM.write(CONFIG_START + 1, settingsCount);
  byte sSize=sizeof(SettingsStruct);
  uint8_t stringStartIndex=CONFIG_START+2+settingsCount*sSize;
  
  log_line(settingsCount);
  for(unsigned int t=0; t<settingsCount; t++){
    unsigned int settingIndex=CONFIG_START+2+(t*sSize);
    byte slen = settings[t].length();
    log_noline("String saving length: ");
    log_line(settings[t].length());
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
  log_line("Setting initial time");
  setTime(hour, minute, second, day, month, year);   //set the system time to 23h31m30s on 13Feb2009
  RTC.set(now());                     //set the RTC from the system time
}

void digitalClockDisplay(void)
{
  // digital clock display of the time
  log_noline(hour());
  printDigits(minute());
  printDigits(second());
  log_noline(' ');
  log_noline(day());
  log_noline(' ');
  log_noline(month());
  log_noline(' ');
  log_noline(year()); 
  log_line();
}
void printDigits(int digits)
{
  // utility function for digital clock display: prints preceding colon and leading 0
  log_noline(':');
  if(digits < 10) {
      log_noline('0');
  }
  log_noline(digits);
}
///END RTC Stuff


//// BEGIN OTP Stuff
void prepareOtp(){
  //Set setting from eeprom
  unsigned int l=settings[OTP_SETTING_INDEX].length();
  if(l == 0){
    log_line("No valid OTP, defaulting");
    totp = TOTP(0,1);
    return;
  }
  char otp_seed[l];
  Serial.print("Incoming setting: ");
  Serial.println(settings[OTP_SETTING_INDEX]);
  settings[OTP_SETTING_INDEX].toCharArray(otp_seed,l+1); // Arraysize + 1 for proper extraction
  Serial.println(otp_seed);
  int decoded_size=0;
  uint8_t* decoded_seed;
  base32decode(otp_seed, l, decoded_seed, decoded_size);
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
  delete[] temp_array;
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

void log_line(){
  if(debug_mode){Serial.println();}
}
void log_line(const char input){
  if(debug_mode){Serial.println(input);}
}
void log_line(unsigned int input){
  if(debug_mode){Serial.println(input);}
}
void log_line(int input){
  if(debug_mode){Serial.println(input);}
}
void log_line(const char* input){
  if(debug_mode){Serial.println(input);}
}
void log_line(char* input){
  if(debug_mode){Serial.println(input);}
}

void log_noline(const char input){
  if(debug_mode){Serial.print(input);}
}
void log_noline(unsigned int input){
  if(debug_mode){Serial.print(input);}
}
void log_noline(int input){
  if(debug_mode){Serial.print(input);}
}
void log_noline(const char* input){
  if(debug_mode){Serial.print(input);}
}
void log_noline(char* input){
  if(debug_mode){Serial.print(input);}
}

void log_string(String input){
  if(debug_mode){Serial.println(input);}
}

