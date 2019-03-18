import io
import socket

import config
from PIL import Image

class Client:
    @staticmethod
    def do():
        clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        clientsocket.connect((config.IP_ADDRESS, config.PORT))
        fd = Image.open("/home/eilon/Desktop/CamTogether-master/CVserver/images/1.jpg")
        imgByteArr = io.BytesIO()
        fd.save(imgByteArr, format='JPEG')
        clientsocket.send(imgByteArr.getvalue())
        ret = clientsocket.recv(1)
        print("client received " + ret)

if __name__ == '__main__':
    Client.do()
