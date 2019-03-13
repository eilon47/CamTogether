import select
import socket
import cv2
import numpy
import configurations.config as config
import image_analysis as ia

TCP_IP = config.IP_ADDRESS
TCP_PORT = config.PORT

basename = "image%s.png"
connected_clients_sockets = []


class Server:
    buffer_size = 4096

    @staticmethod
    def start_server():
        imgcounter = 1
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        server_socket.bind((TCP_IP, TCP_PORT))
        print('finished binding')
        server_socket.listen(10)
        connected_clients_sockets.append(server_socket)
        while True:
            read_sockets, write_sockets, error_sockets = select.select(connected_clients_sockets, [], [])
            print("New client connection")

            for sock in read_sockets:

                if sock == server_socket:

                    sockfd, client_address = server_socket.accept()
                    connected_clients_sockets.append(sockfd)

                else:
                    try:
                        print(' Buffer size is %s' % buffer_size)
                        data = sock.recv(buffer_size)
                        txt = str(data)

                        if txt.startswith('SIZE'):
                            tmp = txt.split()
                            size = int(tmp[1])

                            print('got size')
                            print('size is %s' % size)

                            sock.send("GOT SIZE")
                            # Now set the buffer size for the image
                            buffer_size = 40960000

                        elif txt.startswith('BYE'):
                            sock.shutdown()

                        elif data:

                            #myfile = open(basename % imgcounter, 'wb')
                            grade = ia.main(data)
                            # data = sock.recv(buffer_size)

                            sock.send("GOT IMAGE - The grade is: " + str(grade))
                            buffer_size = 4096
                            sock.shutdown()
                    except:
                        sock.close()
                        connected_clients_sockets.remove(sock)
                        continue
                imgcounter += 1