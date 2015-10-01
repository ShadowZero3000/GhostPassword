#include <DS3232RTC.h>    //http://github.com/JChristensen/DS3232RTC
#include <Time.h>         //http://www.arduino.cc/playground/Code/Time  
#include <Wire.h>         //http://arduino.cc/en/Reference/Wire (included with Arduino IDE)

void setup(void)
{
  //initialTimeSet();
    Serial.begin(9600);
    setSyncProvider(RTC.get);   // the function to get the time from the RTC
    if(timeStatus() != timeSet) 
        Serial.println("Unable to sync with the RTC");
    else
        Serial.println("RTC has set the system time");      
}
void initialTimeSet(){
  int year = 2015;
  int month = 9;
  int day = 30;
  int hour = 23;
  int minute = 11;
  int second = 30;
  setTime(hour, minute, second, day, month, year);   //set the system time to 23h31m30s on 13Feb2009
  RTC.set(now());                     //set the RTC from the system time
}
void loop(void)
{
    digitalClockDisplay();  
    delay(1000);
}

void digitalClockDisplay(void)
{
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

void printDigits(int digits)
{
    // utility function for digital clock display: prints preceding colon and leading 0
    Serial.print(':');
    if(digits < 10)
        Serial.print('0');
    Serial.print(digits);
}
