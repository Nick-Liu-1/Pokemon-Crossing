from PIL import Image
import glob

basewidth = 5760


img = Image.open("C:\\Users\\nickl\\Desktop\\Com Sci\\Pokemon Crossing\\Assets\\Map\\PC Mask.png")
wpercent = (basewidth/float(img.size[0]))
hsize = int((float(img.size[1])*float(wpercent)))
img = img.resize((basewidth,hsize), Image.ANTIALIAS)
img.save("C:\\Users\\nickl\\Desktop\\Com Sci\\Pokemon Crossing\\Assets\\Map\\PC Mask.png")

print("done")
