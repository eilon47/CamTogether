import socket

import cv2 as cv2
import numpy as np

import blur_detection as bd
import config
import face_recogize as fr


def cv_size(img):
    return tuple(img.shape[1::-1])


def handle(client_sock, address):
    request = client_sock.recv(100000)
    print('Received {}'.format(len(request)) + " bytes")
    res = analysis(request)
    client_sock.send(res.to_bytes())
    client_sock.close()


def analysis(img):
    nparr = np.fromstring(img, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_LOAD_GDAL)
    is_blur, down_grade = bd.blur_detection(img, cv_size(img), 1000)
    if is_blur:
        return -1
    locations, num_faces = fr.number_of_faces(img)
    if num_faces == 1:
        return -1
    return 0


class Server:
    def __init__(self):
        self._port = config.PORT
        self._ip = config.IP_ADDRESS
        self.server = None

    def create_connection(self):
        print("creating connection")
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind((self._ip, self._port))

    def start(self):
        while True:
            print("listening")
            self.server.listen(1)
            client_sock, address = self.server.accept()
            #print("connected : " + client_sock)
            print(address)
            print("handling")
            handle(client_sock, address)


if __name__ == '__main__':
    server = Server()
    server.create_connection()
    server.start()
