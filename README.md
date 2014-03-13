MoarCommands
============

Fixed - 
1	Creates new files for each member


Broken - 
1	/nick doesnt write nickname to user file (so nicknames are only in
effect until user leaves and returns again)

2	/bannick due to issue #1 (going to remove for Aero, See To Do #2)


To Do -
1	Fix Broken

2 Remove /bannick and make method of communicating with Aero's ban plugin.

3	Clean up the mess....

4	Make separate classes (to prepare for more commands, each class for each command [Main.java will be for calling each individual class??])

5	Make more commands
