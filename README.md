# Sprint 3 by Team 377 at Clayton - Installation Guide

You can obtain the source code for this project from: https://git.infotech.monash.edu/FIT3077/fit3077-s1-2024/CL_Monday06pm_Team377

## This project is only supported on Windows
 
## Guides for compiling and running the prototype
### Compiling
Please refer to the team's document for more clarification
- Step 1: Open IntelliJ Idea software
- Step 2: Go to Gradle panel on the right sidebar
- Step 3: Go to desktop -> Tasks -> build and run jpackageImage task
- Step 4: After the task is finished, a build folder will be generated inside desktop
- Step 5: Go to build -> jpackage -> desktop -> app
- Step 6: Run the desktop-1.0.jar file to launch the prototype

_**Note**_: Please see the following instructions to run the jar file
### Running
Here are the system requirements to run the game.
- SDK: either corretto-20 or temurin-21
- Java Virtual Machine (JVM): java version "21.0.2" 2024-01-16 LTS

Navigate to the `executable` folder, take the `save_file.yaml` from the `assets` folder and put it into the `executable/app` folder. Here you can modify the `save_file.yaml` file, to load your game configuration.
- **saved**: This is to indicate that if the previous game is saved. Set this to “true” if you want to load the game configuration from this yaml
- **load_option**: custom or default, this is handled by the game. It is set to “custom” if you choose to load your custom game configuration, “default” if you choose to load a default configuration. You do not need to change this option
- **chitCardNumber**: a list of numbers for chit cards in the game
- **chitCardFlipped**:  a list of strings to indicate if the chit cards are currently flipped
- **chitCardType**: a list of types of chit cards
- **boardCustom**: a list of types of slot on the board, this can be of any length
- **currentPlayer**: the index of the player who is having their turn, zero-based. Maximum value is 3.
- **playerPositionCustom**: the position of the player on the board
- **playerDistanceFromCaveCustom**: the distance of each player from their cave, can be negative or positive.
- **playerNumber**: the number of players on the board, maximum is 4, minimum is 1
- **volcanoCardSize**: the size of all volcano cards on the board
- **cavePosition**: the positions of the caves, zero-based, ranges between 0 and length of the board minus 1

Next, click on the `desktop-1.0.jar` file to run the game.

## Navigating to the code repository to follow video instruction
- Unzip `Sprint3_Team377_Repository.zip` archive
- Navigate to into the `sprint4-implementation` directory, under this directory is the code for the project
- Right-click and run using IntelliJ IDEA, alternatively open this directory in an existing IntelliJ IDEA window

## Other directories
- `docs` - contain the high-resolution images of diagrams and document containing all text-based requirement for this sprint
- `executable` - a compiled EXE and other dependencies needed, right-click the `desktop-1.0.jar` file inside `app` to run the game, make sure to have JDK 15 installed


