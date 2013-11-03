BOARD_TAG = uno
ARDUINO_PORT = /dev/ttyACM0
ARDUINO_LIBS = IRremote
ARDUINO_DIR = /usr/share/arduino
include /usr/share/arduino/Arduino.mk

.PHONY: minicom

minicom:
	sudo minicom -D ${ARDUINO_PORT} -b 9600 -o
