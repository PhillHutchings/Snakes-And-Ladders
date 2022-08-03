# Snakes-And-Ladders


main class Game.java

 classic snakes and ladders board game
 
 This is my version of the classic snakes and ladders board game, it is my first attempt at a board game and i am pretty pleased with how it turned out, i especially like the fact that you can have an infinite amount of different boards as i made it so it is randomly generated, and also giving you the option to save a board that is great for playing.

	I created the images for the snakes and ladders game using paint.net, i originally just made one ladder and sized it according to the distance between the randomly generated top of the ladder square and the bottom square, but this was right as a really loong ladder would strentch the image to much and it would look silly, so i made three different sizes, small, medium and large and depending on the size the appropriate ladder would be choosen, this was not a problem for the sanke as it resized with little distortion, but i made three colours of snake which are choosen at random per snake generated just to enhance the look of the board, there was only one real hill i had to climb and that was find the points on the straight line between the head of the snake and its tail, as well as the top of the ladder and the bottom, so that when the player lands on the square the players slides or climbs as needed, after looking online all i found was quite complicated, using mathmatical algorithms and dint really give any hints how i could apply it in code, so i got to work on my own method which works perfectly for this program, i merely used line2d to make the line and then using a linked hash map i looped through the board from the start position i.e snakes head, or ladders bottom and used the intersects method to add the point found to the map, i found that using a 1 by 1 rectangle the player went off coarse, but i changed it to a 2 by 2 rectangle, i then looped through the map and updated the players x and y position to the coordinates then with a do while to pause it for 7 milliseconds between changes and the player movement was perfectk, I really liked the way the dice roll came out as well i made the dice in paint.net.
	The only down side and thing i would change is that i started programming for the board first and then when i did the start window where you choose players and such, i kind of had to do a work around for loading the board and players if was nothing horrendous i just had to recall a method to make the players load.


						THE GAME

1/ 	
	run the game and the first window opens up, choose how many players 1 to 3, enter names if you like and choose piece colours, also an option to choose difficulty level, easy = 12 ladders, 5 snakes, medium = 6 ladders 10 snakes and hard = 3 ladders 15 snakes,  then click begin.


2/	
	The board will be presented, click new board to get a new random board, keep going until you see one you like, rolling the dice will block this action, you can save a board at any point in the game, savd boards can be loaded prior to the first roll of the dice.

3/
	First player to square 100 wins, you have to roll the exact number to land on the final square i.e if you are 4 squares away you must land a 4 on the dice to finish, any player within 6 squares of the finish will only use one dice. on finish the game you have the option to play again or quit, quiting will terminate the running game.
