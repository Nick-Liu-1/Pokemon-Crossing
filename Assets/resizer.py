from PIL import Image
import glob

def resize(file, basewidth):
    img = Image.open(file)
    wpercent = (basewidth/float(img.size[0]))
    hsize = int((float(img.size[1])*float(wpercent)))
    img = img.resize((basewidth,hsize), Image.ANTIALIAS)
    img.save(file)
    print("done")

def resizeFolder(folder):
    for file in glob.glob(folder):
        resize(file)
    print("done")




