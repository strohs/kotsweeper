# kotsweeper
recreation of Microsoft's Minesweeper using Kotlin and [Open JavaFX 11](https://openjfx.io/index.html).
  
![alt text](https://github.com/strohs/kotsweeper/blob/master/kotsweeper_screenshot.jpg "kotsweeper screenshot")

## About
This is an educational project designed to teach me some kotlin and JavaFX by creating a clone of Microsoft's classic
[minesweeper](https://en.wikipedia.org/wiki/Minesweeper_(video_game)) (the version that shipped with Windows 98 through 
Windows XP.) Kotsweeper keeps most of the original's functionality, including the ability to right-click on a square in 
order to set a flag or question-mark. The game ends once you have placed all your mine marking flags at which point the 
board will be revealed and you can see which squares were marked correctly. To restart the game, left-click the smiley 
face button. To set the board dimensions, use the option menu to set the numbers of rows/cols from between 5 and 25

## prerequisites
* at least Java 11 installed on your system. I used OpenJDK 11.0.9.1
 
## Running
This project was developed with IntelliJ IDEA, Kotlin 1.3, Open JavaFX11 and Gradle. GradleWrapper has been provided
to make building and running the project easier.

* make sure your in the project root directory: `kotsweeper`
* from the project root directory run `./gradlew run`
    * if your on windows run `gradlew.bat run`
  
