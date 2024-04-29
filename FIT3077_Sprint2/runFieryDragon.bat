ECHO OFF
ECHO "Compiling game, if this fail, please make sure you have at JDK 15 or above installed..."
gradlew build || gradlew run
ECHO "Compilation finish, please check the ./out directory, close this screen at any time"
PAUSE