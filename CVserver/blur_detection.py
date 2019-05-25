import cv2


def laplacian_blur_detection(image):
    return cv2.Laplacian(image, cv2.CV_64F).var()


def blur_detection(image, size, threshold=300):
    res_var = laplacian_blur_detection(image)
    down_grade = abs(res_var - threshold)/threshold
    return res_var < threshold, down_grade




