import io

from PIL import Image
import socket
import sys

from PIL.JpegImagePlugin import JpegImageFile

import face_recogize as fr
import blur_detection as bd
import cv2 as cv2
import config
import numpy as np


def cv_size(img):
    return tuple(img.shape[1::-1])


def handle(client_sock, address):
    data = bytearray()
    num_of_chuncks = client_sock.recv()
    for i in range(num_of_chuncks):
        data.extend(bytearray(client_sock.recv()))
    print('Received {}'.format(len(data)) + " bytes")
    res = analysis(data)
    client_sock.send(res)
    client_sock.close()


def analysis(img):
    nparr = np.frombuffer(img, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_LOAD_GDAL)
    is_blur, down_grade = bd.blur_detection(img, cv_size(img), 1000)
    if is_blur:
        return -1
    locations, num_faces = fr.detect(img)
    if num_faces == 1:
        return -1
    return 0


class Server:
    def __init__(self):
        self._port = config.PORT
        self._ip = config.IP_ADDRESS
        self.server = None

    def create_connection(self):
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind((self._ip, self._port))

    def start(self):
        while True:
            self.server.listen(1)
            client_sock, address = self.server.accept()
            handle(client_sock, address)


