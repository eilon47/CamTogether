import cv2
from CVServer import server as s, client as c

if __name__ == '__main__':
    print(cv2.__version__)
    server = s.Server()
    server.start_server()
