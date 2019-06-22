import cv2 as cv2
import numpy as np
from PIL import Image

import blur_detection as bd
import face_recogize as fr

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
        nparr = np.asarray(img)
        i = Image.fromarray(nparr)
        i.show()
        img = cv2.imdecode(nparr, cv2.IMREAD_LOAD_GDAL)
        is_blur, down_grade = bd.blur_detection(img, cv_size(img), 1000)
        if is_blur:
            return 400
    if selfie_check:
        locations, num_faces = fr.detect(img)
        if num_faces == 1:
            return 500
    return 0


def handle(command, ctimg):
    img_data = ctimg["imageData"]
    img_data = list(img_data)
    res = analysis(command, img_data)
    print("Done!")
    return res, reason_dict[res]


