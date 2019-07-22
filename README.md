# Minesweeper-Solver 

A Minesweeper solver, written in Java 

Since the dawn of Windows XP, Minesweeper has caught the attention of bored children to bored adults everywhere. But who has the time to play out a full game anymore? 

## How Does it Work?
I rewrote the Minesweeper game in Java, using the Java Swing library. You may think, why would you rewrite the entire game, when the game alread exists? Well, now that I have modeled the game in Java, the bot has direct interface to the game, but can only see what a regular player sees. This also means that we can get insights into the bots thinking. For example, by hovering over a tile in the GUI, you can see the bots (accurate)
prediction of where it thought the mine was.
![Minesweeper Picture](https://i.imgur.com/yo4Tvmc.jpg)

## How to view source code and develop
Just download the github repo as a zip, and import it into your eclipse workspace, that easy!
To get it running, thanks to the power of Java and the JVM, you just have to run MinesweeperGUI.java.

## Solving Strategies
As of right now, the bot uses a "neighbors" approach, where based on every new move it makes, it will use current hints on the board to generate a prediction for the "safety" of each square on the board. When it makes a move, it will choose the square with the most confidence of being a regular square, and not a mine. Hover over any square to view the bot's predictions.

 ![Minesweeper Picture](https://i.imgur.com/9rN6Vme.jpg)
*Keep in mind, the nature of Minesweeper does not allow for a completely deterministic strategy, so some chance is involved, and even with the most advanced techniques, can not be completely fool-proof*
