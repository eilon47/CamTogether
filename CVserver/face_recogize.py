import cv2

cas = cv2.CascadeClassifier('cascades/haarcascade_frontalface_alt.xml')

# def detect(image, scale=1.1, min_neig=5):
#     imgtest1 = image.copy()
#     imgtest = cv2.cvtColor(imgtest1, cv2.COLOR_BGR2GRAY)
#     faces = cas.detectMultiScale(imgtest,scaleFactor=scale, minNeighbors=min_neig)
#     print("found {} faces".format(len(faces)))
#     return faces, len(faces)
#

import face_recognition as face_recognition
from PIL import Image, ImageDraw

def detect(image):
    loc, num = number_of_faces(image, True)
    return loc, num


def number_of_faces(img, show=False):
    model = "hog"
    locations = face_recognition.face_locations(img,2, model)
    draw_squares_on_image(img, locations, show)
    return locations, len(locations)


def draw_squares_on_image(img, locations, show=False):
    im = Image.fromarray(img, 'RGB')
    draw = ImageDraw.Draw(im)
    if show:
        im.show()
    print(locations)
    for loc in locations:
        loc = rotate_loc(loc)
        draw.rectangle(xy=loc, outline="red")
    del draw
    return im
    # write to stdout

def rotate_loc(location):
    top, right, bottom, left = location
    return int(right), int(bottom),int(left), int(top)


