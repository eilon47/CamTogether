import cv2

cas = cv2.CascadeClassifier('cascades/haarcascade_frontalface_alt.xml')

def detect(image, scale=1.1, min_neig=5):
    imgtest1 = image.copy()
    imgtest = cv2.cvtColor(imgtest1, cv2.COLOR_BGR2GRAY)
    faces = cas.detectMultiScale(imgtest,scaleFactor=scale, minNeighbors=min_neig)
    return len(faces)



