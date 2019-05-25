import os
import cv2
# cascades = [ #cv2.CascadeClassifier('cascades/haarcascade_frontalcatface.xml'),
# #              cv2.CascadeClassifier('cascades/haarcascade_frontalcatface_extended.xml'),
#              cv2.CascadeClassifier('cascades/haarcascade_frontalface_alt.xml'),
#              #cv2.CascadeClassifier('cascades/haarcascade_frontalface_alt2.xml')
#              # cv2.CascadeClassifier('cascades\haarcascade_profileface.xml'),
#              ]
cas = cv2.CascadeClassifier('cascades/haarcascade_frontalface_alt.xml')


def detect(image, scale=1.1, min_neig=5):
    imgtest1 = image.copy()
    imgtest = cv2.cvtColor(imgtest1, cv2.COLOR_BGR2GRAY)
    faces = cas.detectMultiScale(imgtest,scaleFactor=scale, minNeighbors=min_neig)
    return len(faces)

    # for (x, y, w, h) in faces:
    #     face_detect = cv2.rectangle(imgtest, (x, y), (x + w, y + h), (255, 0, 255), 2)
    #     roi_gray = imgtest[y:y + h, x:x + w]
    #     roi_color = imgtest[y:y + h, x:x + w]
    #     plt.imshow(face_detect)
    #     eyes = eye_cascade.detectMultiScale(roi_gray)
    #     for (ex, ey, ew, eh) in eyes:
    #         eye_detect = cv2.rectangle(roi_color, (ex, ey), (ex + ew, ey + eh), (255, 0, 255), 2)
    #         plt.imshow(eye_detect)


