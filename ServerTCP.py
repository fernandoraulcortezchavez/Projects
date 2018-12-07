#!/usr/bin/env python3.4
import socket
#from contextlib import closing


# HOST = '192.168.0.15'
HOST = '192.168.0.27'
# (localhost)
PORT = 3000

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    print("Listening to connection")
    s.bind((HOST, PORT))
    s.listen(3)
    conn, addr = s.accept()
    with conn:
        print("Connected")
        print('Client: ', addr)
        while True:
            data = conn.recv(1024)
            if not data:
                break
            else:
                print(data)
