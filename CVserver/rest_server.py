import base64
import os
import re
from io import BytesIO
from os import path

from PIL import Image
from flask import Flask, json, request

from image_analysis import handle


def saveImg(codec, image_path, ext):
    base64_data = re.sub('^data:image/.+;base64,', '', codec)
    byte_data = base64.b64decode(base64_data)
    image_data = BytesIO(byte_data)
    img = Image.open(image_data)
    ext = ext.upper().replace(".", "")
    img.save(image_path, ext)

def deleteImg(image):
    os.unlink(image)


api = Flask(__name__)

@api.route('/analyze', methods=['POST'])
def analyze():
    data = request.data
    data = json.loads(data)
    cmd = data["command"]
    img = data["img"]
    ctimg = json.loads(img)
    # data_img = ctimg["imageData"]
    # data_img = map(str, data_img)
    # data_img = "".join(data_img)
    # name = ctimg["name"]
    # ext = path.splitext(name)[-1]
    # ext = ".jpg" if ext.strip() == "" else ext.strip()
    # saveImg(data_img, name, ext)
    res, reason = handle(cmd, ctimg)
    return json.dumps({"rc":res, "reason":reason})



if __name__ == '__main__':
    api.run(host="0.0.0.0", port=19090)