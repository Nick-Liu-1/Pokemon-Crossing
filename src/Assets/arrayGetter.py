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
pic = image.load("C:\\Users\\nickl\\Desktop\\Com Sci\\Pokemon Crossing\\Assets\\Minigames\\Thin Ice\\Levels\\level10.png")

a,b = 19, 15
color1 = (153, 217, 234, 255)
color2 = (191, 223, 255, 255)
color3 = (210, 233, 255, 255)

grid = [[None for i in range(a)] for j in range(b)]


running = True
while running:
    for evt in event.get():
        if evt.type == QUIT:
            running = False

    screen.blit(pic, (0,0))

    mx, my = mouse.get_pos()
    print(screen.get_at((mx, my)))                
    
    for i in range(a):
        for j in range(b):
            if screen.get_at((i*60+30, j*60+30)) == color1:
                grid[j][i] = "0"
            elif screen.get_at((i*60+30, j*60+30)) == color2:
                grid[j][i] = "0"
            elif screen.get_at((i*60+30, j*60+30)) == color3:
                grid[j][i] = "1"
            else:
                grid[j][i] = "2"
    


   
    
    display.flip()
    myClock.tick(60)
    running = False
    


file = open("temp.txt", "w")

for i in range(b):
    file.write(" ".join(grid[i])+"\n")
file.close()

print("done")
quit()
