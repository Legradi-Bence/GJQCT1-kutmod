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


Lastly the Code Windows acts like functions, so if we have two, we can call it like this:

//two window code

FirstWindowName

for(int i = 1; i < 10; i++){
function SecondWindowName;
}



SecondWindowName

move(i);
turn(left);


//moveto53


turn(west);
move(20);
turn(north);
move(20);
turn(east);
move(20);
turn(right);
move(7);
turn(left);
move(1);
turn(right);



//full program

for(int i = 0; i<20;i++){
takeOrder();
boolean flag = true;
if(getFirstOrderCoffeeName()=="Latte"){
turn(back);
move(1);
takeCoffee();
turn(left);
}
if(getFirstOrderCoffeeName()=="Cappuccino"){
turn(back);
move(1);
turn(right);
move(1);
turn(left);
takeCoffee();
turn(left);
move(1);
}
if(getFirstOrderCoffeeName()=="Melange"){
turn(back);
move(1);
turn(right);
move(2);
turn(left);
takeCoffee();
turn(left);
move(2);
}
if(getFirstOrderCoffeeName()=="Presso"){
turn(back);
move(1);
turn(right);
move(3);
turn(left);
takeCoffee();
turn(left);
move(3);
}
if(getFirstOrderCoffeeName()=="Frappe"){
turn(back);
move(1);
turn(right);
move(4);
turn(left);
takeCoffee();
turn(left);
move(4);
}

if(getFirstOrderTableName() == "Table_9_1" && flag){
move(2);
turn(left);
move(2);
turn(right);
move(2);
turn(left);
placeCoffee();
turn(left);
move(2);
turn(left);
move(1);
turn(right);
move(2);
turn(right);
flag = false;
}
if(getFirstOrderTableName() == "Table_9_3" && flag){
move(4);
turn(left);
placeCoffee();
turn(left);
move(4);
turn(right);
move(1);
flag = false;
}
if(getFirstOrderTableName() == "Table_9_5" && flag){
move(4);
turn(right);
placeCoffee();
turn(right);
move(4);
turn(right);
move(1);
flag = false;
}
if(getFirstOrderTableName() == "Table_9_7" && flag){
move(2);
turn(right);
move(4);
turn(left);
move(2);
turn(left);
placeCoffee();
turn(left);
move(2);
turn(right);
move(5);
turn(left);
move(2);
turn(right);
flag = false;
}
if(getFirstOrderTableName() == "Table_9_9" && flag){
move(2);
turn(right);
move(4);
turn(left);
move(2);
turn(right);
placeCoffee();
turn(right);
move(2);
turn(right);
move(5);
turn(left);
move(2);
turn(right);
flag = false;
}
if(getFirstOrderTableName() == "Table_13_1" && flag){
move(2);
turn(left);
move(2);
turn(right);
move(6);
turn(left);
placeCoffee();
turn(left);
move(6);
turn(left);
move(1);
turn(right);
move(2);
turn(right);
flag = false;
}
if(getFirstOrderTableName() == "Table_13_3" && flag){
move(8);
turn(left);
placeCoffee();
turn(left);
move(8);
turn(right);
move(1);
flag = false;
}
if(getFirstOrderTableName() == "Table_13_5" && flag){
move(8);
turn(right);
placeCoffee();
turn(right);
move(8);
turn(right);
move(1);
flag = false;
}
if(getFirstOrderTableName() == "Table_13_7" && flag){
move(2);
turn(right);
move(4);
turn(left);
move(6);
turn(left);
placeCoffee();
turn(left);
move(6);
turn(right);
move(5);
turn(left);
move(2);
turn(right);
flag = false;
}
if(getFirstOrderTableName() == "Table_13_9" && flag){
move(2);
turn(right);
move(4);
turn(left);
move(6);
turn(right);
placeCoffee();
turn(right);
move(6);
turn(right);
move(5);
turn(left);
move(2);
turn(right);
flag = false;
}
}