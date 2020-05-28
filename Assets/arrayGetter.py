from pygame import *

# Pygame Window
size = width, height = 5760, 5760
screen = display.set_mode(size)
init()

# Constants
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (0, 0, 255)
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)

myClock = time.Clock()
pic = image.load("C:\\Users\\nickl\\Desktop\\Com Sci\\Pokemon Crossing\\Assets\\Map\\Minigame Island.png")

grid = [[None for i in range(49)] for j in range(46)]

running = True
while running:
    for evt in event.get():
        if evt.type == QUIT:
            running = False

    screen.blit(pic, (0,0))
    
    for i in range(49):
        for j in range(46):
            if screen.get_at((i*60+30, j*60+30)) == screen.get_at((1350, 1470)):
                grid[j][i] = "1"
            else:
                grid[j][i] = "0"
    


   
    
    display.flip()
    myClock.tick(60)
    running = False
    


file = open("temp.txt", "w")

for i in range(46):
    file.write(" ".join(grid[i])+"\n")
file.close()

print("done")
quit()
