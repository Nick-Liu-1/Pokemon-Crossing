import cv2
import numpy as np
from pygame import *

def compare(p1, p2):
    original = cv2.imread(p1)
    duplicate = cv2.imread(p2)

    if original.shape == duplicate.shape:
        difference = cv2.subtract(original, duplicate)
        b, g, r = cv2.split(difference)

        if cv2.countNonZero(b) == 0 and cv2.countNonZero(g) == 0 and cv2.countNonZero(r) == 0:
            return True

    else:
        return False



# Pygame Window
size = width, height = 750, 750
screen = display.set_mode(size)
init()

# Constants
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (0, 0, 255)
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)

myClock = time.Clock()
pic = image.load("AC Map.png")

running = True
while running:
    for evt in event.get():
        if evt.type == QUIT:
            running = False

    screen.blit(pic, (0,0))
    for i in range(0, 5460, 60):
        for j in range(0, 3840, 60):
            pass
    
    display.flip()
    myClock.tick(60)

quit()
