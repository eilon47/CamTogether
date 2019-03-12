import sys
import face_recogize as fr
import blur_detection as bd
import cv2 as cv2


def main(im):
	grade = []
	# open a new image and show it
	image = cv2.imread(im, 1)
	highet, width = cv_size(image)
	total_size = highet*width
			# i1 = cv2.line(i1, (0,0), (255,255), (0,0,255), 2)
			# i1 = cv2.rectangle(i1,(33,33), (90,90), (255,0,0), 3)
			# i1 = cv2.putText(i1, "we are writing on the image!!", (0,50), cv2.FONT_HERSHEY_TRIPLEX, 1, (0,255,0), 10, cv2.LINE_AA)
			# cv2.imshow('bomba', i1)
			# k = cv2.waitKey(0)
			# if k == 27:
			# 	cv2.destroyAllWindows()
			# elif k == ord('s'):
			# 	cv2.imwrite('1_1.png',i1)
			# 	cv2.destroyAllWindows()
	is_blur, down_grade = bd.blur_detection(image, total_size, 1000)
	if is_blur:
		grade.append(down_grade)
	else:
		grade.append(1)

	locations, number_of_faces = fr.number_of_faces(image)
	if number_of_faces is 0:
		# check if its landscape
		pass
	else:
		fr.draw_squares_on_image(image, locations)
	return grade


def cv_size(img):
	return tuple(img.shape[1::-1])


if __name__ == '__main__':
	main(sys.argv[1])
