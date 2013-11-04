#include <Arduino.h>
#include <HardwareSerial.h>
#include <IRremote.h>
const int buttonPin = 4;
int buttonState = 0;

IRsend irsend;

void setup()
{
    Serial.begin(9600);
}

void loop()
{
    int d;
    unsigned long code = 0;
    if ((d = Serial.read()) != -1) {
        switch (d) {
            case '1': code = 0x1CE3807F; break;
            case '2': code = 0x1CE340BF; break;
            case '3': code = 0x1CE3C03F; break;
            case '4': code = 0x1CE320DF; break;
            case '5': code = 0x1CE3A05F; break;
            case '6': code = 0x1CE3609F; break;
            case '7': code = 0x1CE3E01F; break;
            case '8': code = 0x1CE310EF; break;
            case '9': code = 0x1CE3906F; break;
            case '0': code = 0x1CE300FF; break;
            case 'q': code = 0x1CE3708F; break;  // volume up
            case 'a': code = 0x1CE3F00F; break;  // volume down
            case 'w': code = 0x1CE350AF; break;  // channel up
            case 's': code = 0x1CE3D02F; break;  // channel down
            case 'e': code = 0x1CE3C837; break;  // source
            case 'd': code = 0x1CE3A25D; break;  // enter
            case 'p': code = 0x1CE348B7; break;  // power
            default:  code = 0;
        }
        if (code != 0) {
            irsend.sendNEC(code, 32);
        }
    }
}

