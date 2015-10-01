#include <EEPROMex.h>
#include <EEPROMVar.h>

#define CONFIG_VERSION 1
#define CONFIG_START 8
#define SETTING_STRING_SIZE 254
#define EEPROM_MIN_ADDR 0
#define EEPROM_MAX_ADDR 128 // Max size on the Teensy LC
struct SettingsStruct {
  // This detects that they are our stored settings
  byte version;
  char type[4];
  byte stringSize;
} settings = {
  CONFIG_VERSION,
  "blk",
  0
};
String settingValue;
#define PIN_D7 7
#define PIN_A7 7
int count = 0;

void setup() {
  Serial.begin(9600);
  delay(1000);
  // Be warned, your EEPROM size limits your string input to around 90 characters!
  loadConfig();
  String blankTest=String("blk");
  if (blankTest.equals(settings.type)){
    Serial.println("EEPROM is set to default, fixing!");
    strncpy(settings.type,"tst",3);
    settingValue = "This is not a test";
    saveConfig();
  } else {
    Serial.print("Loaded type: ");
    Serial.println(settings.type);
    Serial.print("Loaded value: ");
    Serial.println(settingValue);
  }
  pinMode(PIN_A7, INPUT);
  pinMode(PIN_D7, INPUT);
  pinMode(6, OUTPUT);
  digitalWrite(6, HIGH);
}

  int val = 0;
void loop() {
  // Your computer will receive these characters from a USB keyboard.
  //Keyboard.print(charstring); 
  //Keyboard.println(count);

  // You can also send to the Arduino Serial Monitor
  
  //Serial.println(charstring);
  //Serial.println(count);
  val = digitalRead(PIN_D7);
  Serial.print(" - Digital 7: ");
  Serial.print(val);
  val = digitalRead(8);
  Serial.print(" - Digital 8: ");
  Serial.print(val);
  val = analogRead(PIN_A7);
  Serial.print(" - Analog 7: ");
  Serial.println(val);
  // increment the count
  //count = count + 1;

  delay(500);
  // typing too rapidly can overwhelm a PC
  Serial.println("n");
  delay(500);
}


void loadConfig() {
  if(EEPROM.read(CONFIG_START + 0) == CONFIG_VERSION) {
    for(unsigned int t=0; t<sizeof(settings); t++){
      *((char*)&settings + t) = EEPROM.read(CONFIG_START + t);
    }
    settingValue = read_StringEE(CONFIG_START + sizeof(settings), settings.stringSize);
  } else {
    settingValue = '\n';
  }
}
void saveConfig() {
  settings.stringSize = settingValue.length()+1;
  for(unsigned int t=0; t<sizeof(settings); t++){
    EEPROM.write(CONFIG_START + t, *((char*)&settings + t));
  }
  write_StringEE(CONFIG_START + sizeof(settings), settingValue);
}
String read_StringEE(int Addr,int length)
{
  String stemp="";
  char cbuff[length];
  eeprom_read_string(Addr,cbuff,length);
  for(int i=0;i<length-1;i++)
  {
    stemp.concat(cbuff[i]);//combines characters into a String
    delay(100);
  }
  return stemp;
}
bool write_StringEE(int Addr, String input)
{
    char cbuff[input.length()+1];//Finds length of string to make a buffer
    Serial.println("Size of string: ");
    Serial.println(sizeof(cbuff));
    input.toCharArray(cbuff,input.length()+1);//Converts String into character array
    return eeprom_write_string(Addr,cbuff);//Saves String
}

//http://ediy.com.my/index.php/tutorials/item/68-arduino-reading-and-writing-string-to-eeprom
 
 
 
// Returns true if the address is between the
// minimum and maximum allowed values, false otherwise.
//
// This function is used by the other, higher-level functions
// to prevent bugs and runtime errors due to invalid addresses.
boolean eeprom_is_addr_ok(int addr) {
  return ((addr >= EEPROM_MIN_ADDR) && (addr <= EEPROM_MAX_ADDR));
}
 
 
// Writes a sequence of bytes to eeprom starting at the specified address.
// Returns true if the whole array is successfully written.
// Returns false if the start or end addresses aren't between
// the minimum and maximum allowed values.
// When returning false, nothing gets written to eeprom.
boolean eeprom_write_bytes(int startAddr, const byte* array, int numBytes) {
  // counter
  int i;
 
  // both first byte and last byte addresses must fall within
  // the allowed range
  if (!eeprom_is_addr_ok(startAddr) || !eeprom_is_addr_ok(startAddr + numBytes)) {
    return false;
  }
 
  for (i = 0; i < numBytes; i++) {
    EEPROM.write(startAddr + i, array[i]);
  }
 
  return true;
}
 
 
// Writes a string starting at the specified address.
// Returns true if the whole string is successfully written.
// Returns false if the address of one or more bytes fall outside the allowed range.
// If false is returned, nothing gets written to the eeprom.
boolean eeprom_write_string(int addr, const char* string) {
 
  int numBytes; // actual number of bytes to be written
 
  //write the string contents plus the string terminator byte (0x00)
  numBytes = strlen(string) + 1;
 
  return eeprom_write_bytes(addr, (const byte*)string, numBytes);
}
 
 
// Reads a string starting from the specified address.
// Returns true if at least one byte (even only the string terminator one) is read.
// Returns false if the start address falls outside the allowed range or declare buffer size is zero.
//
// The reading might stop for several reasons:
// - no more space in the provided buffer
// - last eeprom address reached
// - string terminator byte (0x00) encountered.
boolean eeprom_read_string(int addr, char* buffer, int bufSize) {
  byte ch; // byte read from eeprom
  int bytesRead; // number of bytes read so far
 
  if (!eeprom_is_addr_ok(addr)) { // check start address
    return false;
  }
 
  if (bufSize == 0) { // how can we store bytes in an empty buffer ?
    return false;
  }
 
  // is there is room for the string terminator only, no reason to go further
  if (bufSize == 1) {
    buffer[0] = 0;
    return true;
  }
 
  bytesRead = 0; // initialize byte counter
  ch = EEPROM.read(addr + bytesRead); // read next byte from eeprom
  buffer[bytesRead] = ch; // store it into the user buffer
  bytesRead++; // increment byte counter
 
  // stop conditions:
  // - the character just read is the string terminator one (0x00)
  // - we have filled the user buffer
  // - we have reached the last eeprom address
  while ( (ch != 0x00) && (bytesRead < bufSize) && ((addr + bytesRead) <= EEPROM_MAX_ADDR) ) {
    // if no stop condition is met, read the next byte from eeprom
    ch = EEPROM.read(addr + bytesRead);
    buffer[bytesRead] = ch; // store it into the user buffer
    bytesRead++; // increment byte counter
  }
 
  // make sure the user buffer has a string terminator, (0x00) as its last byte
  if ((ch != 0x00) && (bytesRead >= 1)) {
    buffer[bytesRead - 1] = 0;
  }
 
  return true;
}

