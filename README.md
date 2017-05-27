# Battleship
Old game made smart.
-



This project has been started as an AI assignment for my studies. The rules are self-explanatory 
and described quite well in here: https://www.wikiwand.com/en/Battleship_(game) .
There is no automation for players' moves, i.e. the player (you) decides who is to make a move by 
either hitting opponent's board or clicking a button 'Hit me'.

The point of the project was to make the opponent (computer) act smart. 
There are two modes of the 'smartness':

    1. offensive - computer calculates the next best shot at player's board and fires
    2. defensive - computer calculates which squares are safest for its fleet and places its ships there
    
### Offensive mode
Calculation for the next best shot is divided in 3 main parts. 

**Firstly** PC looks at the current player's board
and looks for any open squares having two or more hits at a ship that is still floating (i.e. not killed).
If there are any the next hit will be made at either side of that red block. 
If either of block sides has an empty uncovered square PC will head the other direction.

**Secondly** PC will scout for any open squares having a single hit on either of player's ships (not killed ones).
If there are any, the next hit will be made to a square next to the red one. Subsequent hits will be made similarly
until second square of that same ship is uncovered. Then this block will be handled as per *first* logic.
 
**Thirdly** PC analyzes historical records about all the games stored in DB. 
It picks a closed square from player's board that had been successfully hit by computer most times. This implementation
assumes that player tends to prefer placing his/her ships in some particular squares rather than others.

If none of the above are available, the next square to hit will be picked randomly.

### Defensive mode
PC attempts to build its board up in a way that would allow it to remain in the game as long as possible, i.e.
to hide its ships from player as well as possible.
That being said computer fetches all the squares coordinates Player used to hit the most, ranks each coordinate by
probability of being hit and starts making up blocks of those coordinates so that ships would fit into them.
Blocks having the lowest probability of being hit are filled with smallest ships.

If database does not have enough information to make a well calculated decision ships will be placed randomly.


## Prerequisites
* H2 database running in Server mode
* Java 1.8+
* Tomcat 8+ (to support Servlet 3)
* javascript-enabled browser

Once deployed UI will be accessible @ http://localhost:8080/sbattle/ui 

# NB!
Keep in mind that on each deployment ***database will be rewritten*** by default. All records will be cleared and
schema will be recreated automatically. To prevent that (or to use different DB with different settings) feel free to 
customize your database settings by passing a _.properties_ file to tomcat. 
For more info see class: ***PropertiesLoader.java***


### Author
Darius Juodokas a.k.a. _netikras_

## Licence
The following licence applies to this project


    ------------------------------------------------------------------
    "THE PIZZA-WARE LICENSE" (Revision 42):
    Darius Juodokas  wrote these files. As long as you
    retain this notice you can do whatever you want with this stuff. If we
    meet some day, and you think this stuff is worth it, you can buy me a
    pizza in return.
    ------------------------------------------------------------------


## Screens

<img src="https://raw.githubusercontent.com/netikras/battleship/master/meta/pics/first_page.png" alt="first page">
<img src="https://raw.githubusercontent.com/netikras/battleship/master/meta/pics/second_page.png" alt="second page">
<img src="https://raw.githubusercontent.com/netikras/battleship/master/meta/pics/gameplay.png" alt="gameplay">
<img src="https://raw.githubusercontent.com/netikras/battleship/master/meta/pics/win_pc.png" alt="PC won">