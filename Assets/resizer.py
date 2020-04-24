from PIL import Image
import glob

basewidth = 1920

for file in glob.glob("C:\\Users\\nickl\\Desktop\\Com Sci\\Pokemon Crossing\\Assets\\Rooms\\*.png"):
    img = Image.open(file)
    wpercent = (basewidth/float(img.size[0]))
    hsize = int((float(img.size[1])*float(wpercent)))
    img = img.resize((basewidth,hsize), Image.ANTIALIAS)
    img.save(file)

print("done")
