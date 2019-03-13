import face_recognition as face_recognition
from PIL import Image, ImageDraw


def number_of_faces(img):
	model = "hog"
	locations = face_recognition.face_locations(img,2, model)
	return locations, len(locations)


def draw_squares_on_image(img, locations):
	im = Image.open(img)
	draw = ImageDraw.Draw(im)
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

