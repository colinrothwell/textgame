John's mountain biking adventure
================================

Game
----

The Game class will have one of each of the I/O system's classes (except Command). It will probably also need a handle on the Player class.

I/O System
----------

Made up of four classes: Listener, Modifier, Printer and Command. Listener and Modifier pass around Commands generated from user input. Modifier then changes the game state. Printer takes care of all the output from the game.

Or that's how Lewis did it. I think it's now simpler than that.

We communicate using UserReponse objects with a type, and a message.
Conversation options is one of the possibilities.
To-do
-----


* TODO: write CommandFormatException while (true) { 

* TODO: success condition 

* TODO: write printState (and others for different parts of state)

(don't write anything past this point)
