import socket, select
import cv2
from configurations import config

TCP_IP = config.IP_ADDRESS
TCP_PORT = config.PORT


class Client:

    def __init__(self):
        self.socket = self.connect()

    def connect(self):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_address = (TCP_IP, TCP_PORT)
        sock.connect(server_address)
        return sock

    def terminate_connection(self):
        self.socket.close()

    def send_image(self, image):

        try:
            # open image
            img = open('/home/dandan/PycharmProjects/untitled/images/2.jpg', 'rb')
            bytes = img.read()
            size = len(bytes)

            # send image size to server
            self.socket.sendall(b"SIZE %s")
            answer = self.socket.recv(4096)

            print('answer = %s' % answer)

            # send image to server
            if answer == 'GOT SIZE':
                self.socket.sendall(bytes)

                # check what server send
                answer = self.socket.recv(4096)
                print('answer = %s' % answer)

                if answer == 'GOT IMAGE' :
                    self.socket.sendall("BYE BYE ")
                    print('Image successfully send to server')

        finally:
            img.close()

def main():
    image = cv2.imread('/home/dandan/PycharmProjects/untitled/images/2.jpg', 1)
    client = Client()
    client.send_image(image)
    client.terminate_connection()

if __name__ == '__main__':
    main()

