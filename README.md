# GoogleSnake

A program to play Google Snake

Run the main method in Snake.java and open the Google Snake game in a new window.

### Algorithm

The program reads the Google Snake board from the visible part of the screen. It stores the position of the snake and the apple and makes a movement decision based on the Euclidean distance between the snake head and apple. The main limitation of this approach is that the snake can trap itself. One solution to this is to use area fill to determine if the head is entrapped by the body.

### Performance

The bot averages a score of ~30 depending on the position of the apples. 
