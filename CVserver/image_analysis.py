import cv2 as cv2
import numpy as np
import blur_detection as bd
import config
import face_recogize as fr
import socket

NUM_OF_CHUNCKS_SIZE = 20
CHUNKS_BUFFER_SIZE = 2048
reason_dict = {
    0: "Ok",
    400: "Image is blurred",
    500: "Image is selfie"
}


def cv_size(img):
    return tuple(img.shape[1::-1])


def analysis(command, img):
    command = command.lower()
    blur_check, selfie_check = False, False
    if command == "blur":
        blur_check = True
    elif command == "selfie":
        selfie_check = True
    elif command == "all":
        blur_check, selfie_check = True, True
    if blur_check:
        nparr = np.frombuffer(img, np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_LOAD_GDAL)
        is_blur, down_grade = bd.blur_detection(img, cv_size(img), 1000)
        if is_blur:
            return 400
    if selfie_check:
        locations, num_faces = fr.detect(img)
        if num_faces == 1:
            return 500
    return 0


def clean_msg(msg):
    msg = list(msg)
    msg = msg[2:]
    return "".join(msg)

def get_commnad(client_sock):
    command = client_sock.recv(1024).decode()
    command = clean_msg(command)
    print("Got command", command)
    return command


def get_image(client_sock):
    data = bytearray()
    num_of_chuncks = client_sock.recv(1024).decode()
    size = clean_msg(num_of_chuncks)
    for i in range(int(size)):
        d = list(client_sock.recv(CHUNKS_BUFFER_SIZE))
        data.extend(d)
    print('Received {}'.format(len(data)) + " bytes")
    return data


def send_result(client_sock, res):
    print("result is ", res)
    client_sock.send(str(res).encode())
    client_sock.recv(100)
    client_sock.send(reason_dict[res].encode())
    print("Result sent")


def handle(client_sock):
    command = get_commnad(client_sock)
    img = get_image(client_sock)
    arr = np.asarray(img)
    res = analysis(command, img)
    send_result(client_sock, res)
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
            self.server.listen(20)
            client_sock, address = self.server.accept()
            print("server got connection")
            handle(client_sock)
            print("Done!")

    def stop(self):
        if self.server is not None:
            self.server.close()


if __name__ == '__main__':
    l = list("vfndjvd")
    print(l)
