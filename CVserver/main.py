from image_analysis import Server

if __name__ == '__main__':
    server = Server()
    server.create_connection()
    server.start()
    server.stop()
