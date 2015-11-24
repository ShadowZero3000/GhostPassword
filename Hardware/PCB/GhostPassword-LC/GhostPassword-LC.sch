EESchema Schematic File Version 2
LIBS:power
LIBS:device
LIBS:transistors
LIBS:conn
LIBS:linear
LIBS:regul
LIBS:74xx
LIBS:cmos4000
LIBS:adc-dac
LIBS:memory
LIBS:xilinx
LIBS:microcontrollers
LIBS:dsp
LIBS:microchip
LIBS:analog_switches
LIBS:motorola
LIBS:texas
LIBS:intel
LIBS:audio
LIBS:interface
LIBS:digital-audio
LIBS:philips
LIBS:display
LIBS:cypress
LIBS:siliconi
LIBS:opto
LIBS:atmel
LIBS:contrib
LIBS:valves
LIBS:teensy_3.1
LIBS:GhostPassword-LC-cache
EELAYER 25 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L DS3231 RTC1
U 1 1 562C11B5
P 8400 1450
F 0 "RTC1" H 9390 1150 60  0000 C CNN
F 1 "DS3231" H 9380 1010 60  0000 C CNN
F 2 "GhostPassword:DS3231" H 8400 1450 60  0001 C CNN
F 3 "" H 8400 1450 60  0000 C CNN
	1    8400 1450
	1    0    0    -1  
$EndComp
$Comp
L NeoPixel_Jewel LED1
U 1 1 562C13AF
P 8475 4675
F 0 "LED1" H 8755 4735 60  0000 C CNN
F 1 "NeoPixel_Jewel" H 8775 4295 60  0000 C CNN
F 2 "GhostPassword:NeoPixel_Jewel" H 8475 4675 60  0001 C CNN
F 3 "" H 8475 4675 60  0000 C CNN
	1    8475 4675
	1    0    0    -1  
$EndComp
$Comp
L SW_PUSH SW1
U 1 1 562C1496
P 8800 2700
F 0 "SW1" H 8950 2810 50  0000 C CNN
F 1 "SW_PUSH" H 8800 2620 50  0000 C CNN
F 2 "Buttons_Switches_SMD:SW_SPST_PTS645" H 8800 2700 60  0001 C CNN
F 3 "" H 8800 2700 60  0000 C CNN
	1    8800 2700
	1    0    0    -1  
$EndComp
NoConn ~ 8500 2025
NoConn ~ 8500 2125
Text Label 7700 1925 0    60   ~ 0
RTC-SCL
Text Label 6325 5650 0    60   ~ 0
BLE-RX-1
Text Label 7500 3800 0    60   ~ 0
BTN-IN
Text Label 6775 1125 0    60   ~ 0
VCC
Text Label 5225 6075 0    60   ~ 0
GND
$Comp
L Teensy_3.1 U1
U 1 1 563DA0BC
P 6225 2625
F 0 "U1" H 6275 3675 60  0000 C CNN
F 1 "Teensy_3.1" H 6325 4125 60  0000 C CNN
F 2 "Teensy-3:Teensy-3.1" H 6325 2625 60  0001 C CNN
F 3 "" H 6325 2625 60  0000 C CNN
	1    6225 2625
	0    -1   -1   0   
$EndComp
$Comp
L BlueSmirfHID BLE1
U 1 1 563DA39B
P 8450 5200
F 0 "BLE1" H 9570 5280 60  0000 C CNN
F 1 "BlueSmirfHID" H 9530 4520 60  0000 C CNN
F 2 "GhostPassword:BlueSmirfHID" H 8450 5200 60  0001 C CNN
F 3 "" H 8450 5200 60  0000 C CNN
	1    8450 5200
	1    0    0    -1  
$EndComp
Text Label 6350 4925 0    60   ~ 0
LED-DIN
NoConn ~ 5525 3625
NoConn ~ 5675 3625
NoConn ~ 6275 3625
NoConn ~ 6425 3625
NoConn ~ 6575 3625
NoConn ~ 6725 3625
NoConn ~ 6875 3625
NoConn ~ 7425 2875
NoConn ~ 7425 2725
NoConn ~ 7425 2575
NoConn ~ 7425 2425
NoConn ~ 7425 2275
NoConn ~ 6875 1625
NoConn ~ 6725 1625
NoConn ~ 6575 1625
NoConn ~ 6425 1625
NoConn ~ 6275 1625
NoConn ~ 5825 1625
NoConn ~ 5675 1625
NoConn ~ 5525 1625
NoConn ~ 5375 1625
NoConn ~ 5225 1625
NoConn ~ 5075 1625
NoConn ~ 8500 5750
NoConn ~ 8500 5250
$Comp
L PWR_FLAG #FLG01
U 1 1 563DC349
P 4925 1625
F 0 "#FLG01" H 4925 1720 50  0001 C CNN
F 1 "PWR_FLAG" H 4925 1805 50  0000 C CNN
F 2 "" H 4925 1625 60  0000 C CNN
F 3 "" H 4925 1625 60  0000 C CNN
	1    4925 1625
	1    0    0    -1  
$EndComp
$Comp
L PWR_FLAG #FLG02
U 1 1 563DC525
P 4925 3625
F 0 "#FLG02" H 4925 3720 50  0001 C CNN
F 1 "PWR_FLAG" H 4925 3805 50  0000 C CNN
F 2 "" H 4925 3625 60  0000 C CNN
F 3 "" H 4925 3625 60  0000 C CNN
	1    4925 3625
	0    -1   -1   0   
$EndComp
NoConn ~ 5975 3625
NoConn ~ 5825 3625
$Comp
L R_Small 70k1
U 1 1 563DD290
P 6600 1275
F 0 "70k1" H 6630 1295 50  0000 L CNN
F 1 "R_Small" H 6630 1235 50  0000 L CNN
F 2 "Resistors_ThroughHole:Resistor_Horizontal_RM10mm" H 6600 1275 60  0001 C CNN
F 3 "" H 6600 1275 60  0000 C CNN
	1    6600 1275
	0    -1   -1   0   
$EndComp
$Comp
L R_Small R70k1
U 1 1 563DD39B
P 6900 1400
F 0 "R70k1" H 6930 1420 50  0000 L CNN
F 1 "R_Small" H 6930 1360 50  0000 L CNN
F 2 "Resistors_ThroughHole:Resistor_Horizontal_RM10mm" H 6900 1400 60  0001 C CNN
F 3 "" H 6900 1400 60  0000 C CNN
	1    6900 1400
	0    -1   -1   0   
$EndComp
Text Label 7700 1825 0    60   ~ 0
RTC-SDA
Connection ~ 8125 4725
Wire Wire Line
	8125 5350 8500 5350
Connection ~ 8275 5450
Wire Wire Line
	8275 5450 8500 5450
Wire Wire Line
	5375 4925 8525 4925
Wire Wire Line
	8125 4725 8525 4725
Connection ~ 8275 4825
Wire Wire Line
	8275 4825 8525 4825
Wire Wire Line
	7600 1925 8500 1925
Wire Wire Line
	7525 1825 8500 1825
Wire Wire Line
	8500 1625 8275 1625
Wire Wire Line
	6125 3800 6125 3625
Wire Wire Line
	4925 1125 8125 1125
Wire Wire Line
	8275 6075 4925 6075
Wire Wire Line
	7950 3800 6125 3800
Wire Wire Line
	7950 2700 7950 3800
Wire Wire Line
	8500 2700 7950 2700
Wire Wire Line
	8125 1125 8125 5350
Wire Wire Line
	9100 2950 8275 2950
Wire Wire Line
	9100 2700 9100 2950
Wire Wire Line
	8275 1625 8275 6075
Wire Wire Line
	5225 5650 8500 5650
Wire Wire Line
	5375 3625 5375 4925
Wire Wire Line
	5225 3625 5225 5650
Wire Wire Line
	4925 6075 4925 3625
Wire Wire Line
	4925 1125 4925 1625
Wire Wire Line
	7525 1400 7525 1825
Wire Wire Line
	6125 1400 6125 1625
Wire Wire Line
	7600 1275 7600 1925
Wire Wire Line
	5975 1275 5975 1625
Connection ~ 8275 2950
Wire Wire Line
	8500 1725 8125 1725
Connection ~ 8125 1725
Wire Wire Line
	6125 1400 6800 1400
Wire Wire Line
	7000 1400 7525 1400
Wire Wire Line
	5975 1275 6500 1275
Wire Wire Line
	6700 1275 7600 1275
Wire Wire Line
	8500 5550 5075 5550
Wire Wire Line
	5075 5550 5075 3625
$EndSCHEMATC
