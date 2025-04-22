After downloading:
./gradlew run
or 
gradlew run
should run the game.

You can make a new code window with the New Code Window button.
You also have to name this window.
After creation you can write java like code in it, and can run with the Run button.
There are also functions for moving the robot.
Few examples:
move(1);

turn(east);

for(int i = 1; i < 10; i++){
move(i);
turn(left);
}

int a = 10;
int b = a;
while(a == b){
move(3);
turn(back);
}

Stopping not yet implemented, so while(true) like functions will run indefinetly, DANGEROUS!

Lastly the Code Windows acts like functions, so if we have two, we can call it like this:

FirstWindowName

for(int i = 1; i < 10; i++){
function SecondWindowName;
}



SecondWindowName

move(i);
turn(left);
