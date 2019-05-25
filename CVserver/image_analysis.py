import socket
import cv2 as cv2
import numpy as np
import blur_detection as bd
import config
import face_recogize as fr


def cv_size(img):
    return tuple(img.shape[1::-1])


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


def handle(client_sock, address):
    data = bytearray()
    num_of_chuncks = client_sock.recv()
    for i in range(num_of_chuncks):
        data.extend(bytearray(client_sock.recv()))
    print('Received {}'.format(len(data)) + " bytes")
    res = analysis(data)
    print("result is ", res)
    client_sock.send(res)
    client_sock.close()
    print("Done!")


class Server:
    def __init__(self):
        self._port = config.PORT
        self._ip = config.IP_ADDRESS
        self.server = None

    def create_connection(self):
        print("creating connection on ", self._ip,":" ,self._port)
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind((self._ip, self._port))
        print("connection created successfully")

    def start(self):
        while True:
            self.server.listen(1)
            client_sock, address = self.server.accept()
            print("server got connection")
            handle(client_sock, address)
            print("Done handling client")

    def stop(self):
        if self.server is not None:
            self.server.close()


