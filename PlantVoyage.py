#!/usr/bin/env python3.4
import socket
import time
import RPi.GPIO as GPIO
import signal
import sys

def signal_handler(sig, frame):
    print("Server shutting down")
    usedPins = [2,3,4,17,27]
    for pin in usedPins:
        GPIO.output(pin, GPIO.LOW)
    if s is None:
        sys.exit(0)
        return
    try:
        s.close()
    except:
        pass
    sys.exit(0)

# Register signal handler
signal.signal(signal.SIGINT, signal_handler)

# Setup GPIO pins
GPIO.setmode(GPIO.BCM)
motorPins = [2,3,4,17]
for pin in motorPins:
    GPIO.setup(pin, GPIO.OUT)
    GPIO.output(pin, GPIO.LOW)
waterPin = 27
GPIO.setup(waterPin, GPIO.OUT)
GPIO.output(waterPin, GPIO.LOW)

HOST = '10.12.12.110'
#HOST = '192.168.0.27'

PORT = 3000
WATER_PLANT = "W"
ROTATE_PLANT = "R"
TEST = "T"


def WaterPlant():
    print("Watering")
    GPIO.output(waterPin, GPIO.HIGH)
    time.sleep(0.4)
    GPIO.output(waterPin, GPIO.LOW)
    print("Watering stopped")

def RotatePlant():
    print("Rotating plant")
    GPIO.output(motorPins[0], GPIO.HIGH)
    for _ in range(10):
        for i in range(len(motorPins)):
            GPIO.output(motorPins[i], GPIO.HIGH)
            GPIO.output(motorPins[i-2], GPIO.LOW)
            time.sleep(0.025)
    print("Rotation stopped")

while True:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        print("Listening to connection")
        try:
            s.bind((HOST, PORT))
        except:
            continue
        s.listen(3)
        conn, addr = s.accept()
        with conn:
            print("Connected")
            print('Client: ', addr)
            while True:
                data = conn.recv(1024)
                line = data.decode('utf-8').strip()
                if not data:
                    break
                else:
                    print(line)
                    if line == WATER_PLANT:
                        WaterPlant()
                    elif line == ROTATE_PLANT:
                        RotatePlant()
                    elif line == TEST:
                        pass
                    else:
                        pass
