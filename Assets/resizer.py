from PIL import Image
import glob

basewidth = 200

for file in glob.glob("Player/Boy/*"):
    img = Image.open(file)
    wpercent = (basewidth/float(img.size[0]))
    hsize = int((float(img.size[1])*float(wpercent)))
    img = img.resize((basewidth,hsize), Image.ANTIALIAS)
    img.save(file)

print("done")
