

const int Enable12 = 5;  // PWM pin to L293D's EN1 (pin 1)
const int Driver1A = 4;  // To L293D's 1A (pin 2)
const int Driver2A = 3;  // To L293D's 2A (pin 7)
const int Enable34 = 9;  // PWM pin to L293D's EN2 (pin 9)
const int Driver3A = 8;  // To L293D's 3A (pin 10)
const int Driver4A = 7;  // To L293D's 4A (pin 15)
const int IR_SENSOR = 2;
const int LEFT_EYE = 12;
const int RIGHT_EYE = 10;

void setup() {
    pinMode(Enable12, OUTPUT);
    pinMode(Driver1A, OUTPUT);
    pinMode(Driver2A, OUTPUT);
    pinMode(Enable34, OUTPUT);
    pinMode(Driver3A, OUTPUT);
    pinMode(Driver4A, OUTPUT);
    
    pinMode(IR_SENSOR, INPUT);     
    digitalWrite(IR_SENSOR, HIGH); // turn on the pullup
    pinMode(LEFT_EYE, OUTPUT);
    pinMode(RIGHT_EYE, OUTPUT);
    
    Serial.begin(9600);
}

void motorCTRL(int enablePin, int pin1, int pin2, byte speed, bool D1A, bool D2A) {
    analogWrite(enablePin, speed);  // PWM to control speed
    digitalWrite(pin1, D1A);        // Control direction
    digitalWrite(pin2, D2A);        // Control direction 
}

void loop() {

  if(digitalRead(IR_SENSOR) == HIGH){
    
    digitalWrite(LEFT_EYE, HIGH);
    digitalWrite(RIGHT_EYE, HIGH);
    delay(500);
    digitalWrite(LEFT_EYE, LOW);
    digitalWrite(RIGHT_EYE, LOW);
    delay(500);
    digitalWrite(LEFT_EYE, HIGH);
    digitalWrite(RIGHT_EYE, HIGH);
    delay(500);
    forward(800);
    stop();
    left90();
    stop();
    forward(2300);
    stop();
    negotiations(10);
    forward(1100);
    stop();
    backward(3400);
    stop();
    right90();
    stop();
    backward(2000);
    stop();
    spinAround();
    forward(500);
    stop();
    digitalWrite(LEFT_EYE, HIGH);
    digitalWrite(RIGHT_EYE, LOW);
    delay(1000);
    digitalWrite(LEFT_EYE, HIGH);
    digitalWrite(RIGHT_EYE, HIGH);
    delay(2000);
    digitalWrite(LEFT_EYE, LOW);
    digitalWrite(RIGHT_EYE, LOW);
    
    }
    
    delay(500);

   
}

void stop(){
    motorCTRL(Enable12, Driver1A, Driver2A, 0, LOW, LOW);
    motorCTRL(Enable34, Driver3A, Driver4A, 0, LOW, LOW);
    delay(500);
  }


void forward(int milis){
    motorCTRL(Enable12, Driver1A, Driver2A, 255, LOW, HIGH);
    motorCTRL(Enable34, Driver3A, Driver4A, 255, LOW, HIGH);
    delay(milis);
  }

void backward(int milis){
    motorCTRL(Enable12, Driver1A, Driver2A, 255, HIGH, LOW);
    motorCTRL(Enable34, Driver3A, Driver4A, 255, HIGH, LOW);
    delay(milis);
  }

void left90(){
    motorCTRL(Enable12, Driver1A, Driver2A, 255, HIGH, LOW);
    motorCTRL(Enable34, Driver3A, Driver4A, 255, LOW, HIGH);
    delay(645);
  }

void right90(){
    motorCTRL(Enable12, Driver1A, Driver2A, 255, LOW, HIGH);
    motorCTRL(Enable34, Driver3A, Driver4A, 255, HIGH, LOW);
    delay(645);
  }

void spinAround(){
    motorCTRL(Enable12, Driver1A, Driver2A, 255, LOW, HIGH);
    motorCTRL(Enable34, Driver3A, Driver4A, 255, HIGH, LOW);
    delay(4000);
    stop();
    motorCTRL(Enable12, Driver1A, Driver2A, 255, HIGH, LOW);
    motorCTRL(Enable34, Driver3A, Driver4A, 255, LOW, HIGH);
    delay(4000);
    stop(); 
  }

void negotiations(int seconds){

  for(int i = 0; i <= seconds; i++){
    digitalWrite(LEFT_EYE, LOW);
    delay(500);
    digitalWrite(LEFT_EYE, HIGH);
    digitalWrite(RIGHT_EYE, LOW);
    delay(500);
    digitalWrite(RIGHT_EYE, HIGH);
    
  }
  
  
  }
