# Catacomb Crawlers

## Project Description

A game which makes use of classes to have the hero go through a catacomb defeating monsters. Reinforces the use of **classes in java**

### How to run the Project 

To run it, simply put all of the required files (Actor.java, Dependencies.java, Game.java, GameBoard.java, SaveFile.java, Town.java) in a folder and run the Game.java file

### Instructions

Your goal is to upgrade a town by utilizing gold obtained from the catacombs. Be sure to go into settings and enable saving before creating your character if you plan on coming back to the game. To maneuver through the catacombs, you can either use WASD or north, east, south, and west as inputs. The more monsters you defeat, the more gold you earn. Be careful of going deeper into the catacombs, as although you will get more gold, you will find increasingly difficult monsters.

## Lessons Learned

Throughout developing this game, I faced many challenges. The thing I wanted to add to the game originally was a save function. However, a save function has little use if there is not anything to save. So, I decided to make a simple progression system to allow the player to get more powerful. The hardest thing to create was the town, as I had to think of a simple way to add options that the user can select from. The library is currently incomplete, as I could not think of what it could be used for, but I also didn't want to get rid of it. Perhaps overhauling the battle mechanics to add different attacks that you learned from the books, or maybe the books contain maps which allow you to start further along into the dungeon could be added. This game was fairly bug-free throughout development. After creating the methods to display information to the user, such as the gameBoard display function and the dependencies text window function, everything simply worked. This project helped me understand the difficulties of object oriented programming, but also how much nicer it is to code in after you understand it. I could go further and create a child class of gameBoard for the catacombs, but I didn't feel like it.