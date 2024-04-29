# Sprint 2 by Nhat Quang Nguyen (3278 1954)

You can obtain the source code for this project from: https://git.infotech.monash.edu/FIT3077/fit3077-s1-2024/CL_Monday06pm_Team377/-/tree/nhat_sprint2/FIT3077_Sprint2

## This project is only supported on Windows
 
## (!!!!) This project is built on JDK 15, please ensure you have installed and set JDK 15 as your JDK version
+ Was tested on JDK 15.0.2, please install it from here if you haven't done so: https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html
+ Download and run Windows x64 Installer | jdk-15.0.2_windows-x64_bin.exe from the page above
+ Open command prompt, run 'java -version' to ensure you are using JDK 15.
+ Else please change it to JDK 15, by editing your environment variable
+ **If you failed to accomplish this, a pre-compiled executable is included in the 'out' directory**

## Documents/Files of Interest
+ documentation/DesignRationale.pdf - Contains the Rationale document.
+ documentation/OODesign.jgp - The UML Class and Sequence diagram describing the Object-Oriented Design
+ out - Directory where the game is compiled to, .exe executable is located here, there is a precompiled .exe for you to run if you can't compile the source code.

These last 2 are only in the code repository on GitLab:
+ compileFieryDragon.bat - Run this batch file to compile an .exe from the source code, the .exe will be located in the 'out' directory.
+ runFieryDragon.bat - Run this batch file to run the game without compiling to an .exe file.

## Running the game - 3 options
### Running the precompiled exe
+ In the 'out' directory contains a precompiled .exe file for windows called desktop.exe, you can run the game by running that file

These last 2 are only in the code repository on GitLab:
### Running the game using runFieryDragon.bat [REQUIRES JDK 15]
+ Instead of running of a compiled exe, you can run the game by invoking a Gradle task by running the runFieryDragon.bat batch file
### Compiling the game from source code [REQUIRES JDK 15]
+ You can  compile the game from source code by running the compileFieryDragon.bat, the .exe file will be located in the out directory, as desktop.exe