#!/usr/bin/env python3
import time

list = [True, False, False, False]
for _ in range(5):
    for i in range(4):
        list[i] = True
        list[i-2] = False
        print(list)
        time.sleep(0.5)
