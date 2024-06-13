#include <LiquidCrystal_I2C.h>
#include <SPI.h>
#include <MFRC522.h>
#include <RTC.h>
#include "arduino_secrets.h"
#include <ArduinoJson.h>
#include <WiFiS3.h>
#include "ArduinoGraphics.h"
#include "Arduino_LED_Matrix.h"
#include <Servo.h>

struct ResponseData {
  const char* firstRowText;
  const char* secondRowText;
  const boolean greenLedOn;
  const boolean warningOn;

  ResponseData(const char* firstRow, const char* secondRow, boolean greenOn, boolean warningOn)
    : firstRowText(firstRow), secondRowText(secondRow), greenLedOn(greenOn), warningOn(warningOn) {}
};

// Constants
int GATE_ID = 1;
const int ON = HIGH;
const int OFF = LOW;
const int RED_LED_PIN = A1;
const int GREEN_LED_PIN = A0;
const int SS_PIN = 10;
const int RST_PIN = 9;
const int BUZZER_PIN = A2;
const int INFO_BUTTON_PIN = 2;
const int GATE_SWITCH_BUTTON_PIN = 3;
const int IR_BEAM_SWITCH = 7;
const int SERVO_PIN = 6;

const char* SERVER = "192.168.231.228"; //"192.168.117.94";
const int PORT_NO = 8081;
String LOGGING_ENDPOINT = "/?uid=";
String GET_LAST_LOG_ENDPOINT = "/lastLogInfo/?uid=";

// Network credentials
char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;


// Objects
LiquidCrystal_I2C lcd(0x27, 16, 2);
MFRC522 mfrc522(SS_PIN, RST_PIN);
RTCTime currentTime;
WiFiClient client;
ArduinoLEDMatrix matrix;
Servo myservo;


void setup() {
  initializeHardware();
  Serial.println("Gate id: " + String(GATE_ID));
  lcdScreenInfo1stRow("Gate id: " + String(GATE_ID));
  lcdScreenInfo2ndRow("System logging...");
  weclomeMatrixText();
  connectToWiFi();
  initialState();
}

void loop() {
  printCurrentDateTime();
  displayGateIdOnMatrix();
  setCurrentGateId();

  if (isTagDetected()) {
    if(isButtonPressed(INFO_BUTTON_PIN)){
      processTag(GET_LAST_LOG_ENDPOINT);
    }else {
      processTag(LOGGING_ENDPOINT);
    }

  }

  delay(500);
}

void processTag(String endpoint) {
  beeperOn(1000);
  lcdScreenInfo1stRow("Processing...");

  String cardUid = readCardUid();
  String responseFromServer = sendRequestToServer(cardUid, endpoint);
  ResponseData responseDataInfo = parseResponse(responseFromServer);

  lcdScreenInfo1stRow(responseDataInfo.firstRowText);
  lcdScreenInfo2ndRow(responseDataInfo.secondRowText);
  ledsState(GREEN_LED_PIN, responseDataInfo.greenLedOn);
  flashWarningSign(responseDataInfo.warningOn);
  isGateOpen(responseDataInfo.greenLedOn);

  delay(4000);
  initialState();
}

void initializeHardware() {
  pinMode(RED_LED_PIN, OUTPUT);
  pinMode(GREEN_LED_PIN, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(INFO_BUTTON_PIN, INPUT);
  pinMode(GATE_SWITCH_BUTTON_PIN, INPUT);
  pinMode(IR_BEAM_SWITCH, OUTPUT);

  lcd.init();
  lcd.backlight();
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();
  RTC.begin();
  matrix.begin();
  myservo.attach(SERVO_PIN);
  myservo.write(10);
}

void initialState() {
  lcdScreenInfo1stRow("Scan your tag G" + String(GATE_ID));
  ledsState(RED_LED_PIN, ON);
  ledsState(GREEN_LED_PIN, OFF);
  digitalWrite(IR_BEAM_SWITCH, LOW);
}

boolean isTagDetected() {
  return mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial();
}

boolean isButtonPressed(int button){
  return digitalRead(button) == HIGH;
}


void ledsState(int ledPin, int state) {

  digitalWrite(ledPin, state);

  if(ledPin == GREEN_LED_PIN && state == ON){
    digitalWrite(RED_LED_PIN, OFF);
  }
}



void lcdScreenInfo1stRow(String message) {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print(message);
}

void lcdScreenInfo2ndRow(String message) {
  lcd.setCursor(0, 1);
  lcd.print(message);
}

void printCurrentDateTime(){
  RTC.getTime(currentTime);
  RTC.setTime(currentTime);

  String dateCurrent = "";
  String timeCurrent = "";

  dateCurrent += String(currentTime.getDayOfMonth());
  dateCurrent += "/";
  dateCurrent += String(Month2int(currentTime.getMonth()));
  dateCurrent += "/";
  dateCurrent += String(currentTime.getYear());

  timeCurrent += formatTime(currentTime.getHour()); // Format hour
  timeCurrent += ":";
  timeCurrent += formatTime(currentTime.getMinutes()); // Format minute

  lcd.setCursor(0, 1);
  lcd.print(dateCurrent);
  lcd.setCursor(11, 1);
  lcd.print(timeCurrent);

}

String formatTime(int timeValue) {
  if (timeValue < 10) {
    // Add leading zero for single digit values
    return "0" + String(timeValue);
  } else {
    // No need to modify for double digit values
    return String(timeValue);
  }
}

void beeperOn(int beepLength_ms) {
  digitalWrite(BUZZER_PIN, HIGH);
  delay(beepLength_ms);
  digitalWrite(BUZZER_PIN, LOW);
}

String readCardUid() {
  String uid = "";
  for (byte i = 0; i < mfrc522.uid.size; i++) {
    if (mfrc522.uid.uidByte[i] < 0x10) {
      uid += "0";
    }
    uid += String(mfrc522.uid.uidByte[i], HEX);
  }
  Serial.println("Card id : " + uid);
  delay(500); // Delay to avoid continuous reading

  return uid;
}


void connectToWiFi() {
  // Check for the WiFi module
  if (WiFi.status() == WL_NO_MODULE) {
    Serial.println("Communication with WiFi module failed!");
    while (true);
  }

  // Check firmware version
  String fv = WiFi.firmwareVersion();
  if (fv < WIFI_FIRMWARE_LATEST_VERSION) {
    Serial.println("Please upgrade the firmware");
  }

  // Connect to WiFi network
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    WiFi.begin(ssid, pass);
    delay(10000);
    Serial.println("Connection to WiFi successed !");
  }

  // Print WiFi status
    printWifiStatus();

}

void printWifiStatus() {
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  IPAddress ip = WiFi.localIP();
  Serial.print("Arduino IP Address: ");
  Serial.println(ip);

  long rssi = WiFi.RSSI();
  Serial.print("Signal strength (RSSI): ");
  Serial.print(rssi);
  Serial.println(" dBm");
}

String sendRequestToServer(String cardUid, String endpointName) {
  String request = "GET /" + String(GATE_ID) + endpointName + cardUid + " HTTP/1.1";

  if (client.connect(SERVER, PORT_NO)) {
    Serial.println("Connected to server: " + String(SERVER));
    client.println(request);
    Serial.println("Request: " + request);
    client.println("Host: " + String(SERVER));
    client.println("Connection: close");
    client.println();
    delay(800);

    return readResponse();

  }else {
    Serial.println("Failed to connect to server");
    return "";
  }
}

String readResponse() {

  String response = "";
  while (client.available()) {
    response += client.readString();
  }
  return response;
}


ResponseData parseResponse(String response) {
  int jsonStart = response.indexOf('{');
  int jsonEnd = response.lastIndexOf('}');
  String jsonString = response.substring(jsonStart, jsonEnd + 1);

  JsonDocument doc;
  DeserializationError error = deserializeJson(doc, jsonString);


  const char* firstRowText = doc["firstRowText"];
  const char* secondRowText = doc["secondRowText"];
  const boolean greenLedOn = doc["greenLedOn"];
  const boolean warningOn = doc["warningOn"];


  if (firstRowText || secondRowText || greenLedOn) {
    Serial.println("Response from server: ");
    Serial.print("First row text: ");
    Serial.println(firstRowText);
    Serial.print("Second row text: ");
    Serial.println(secondRowText);
    Serial.print("Turn GREEN led on?: ");
    Serial.println(greenLedOn);
    Serial.print("Warning sign on?: ");
    Serial.println(warningOn);

  } else {
    Serial.println("All fields not found in JSON");
  }



  if (error) {
    Serial.print("deserializeJson() failed: ");
    Serial.println(error.c_str());
    return ResponseData("Logging Failed.", "Try again.", false, false);
  }

  return ResponseData(firstRowText, secondRowText, greenLedOn, warningOn);
}

void weclomeMatrixText(){
  matrix.beginDraw();

  //matrix.stroke(0xFFFFFFFF);
  matrix.textScrollSpeed(60);

  // add the text
  const char text[] = "Welcome to Entry Systems";
  matrix.textFont(Font_5x7);
  matrix.beginText(0, 1, 0xFFFFFF);
  matrix.println(text);
  matrix.endText(SCROLL_LEFT);

  matrix.endDraw();

}

void displayGateIdOnMatrix() {
  matrix.beginDraw();
  matrix.clear();

  matrix.stroke(0xFFFFFFFF);
  matrix.textScrollSpeed(50);

  // Create the message with the GATE_ID
  String message = String(GATE_ID);

  // Add the text to the matrix
  matrix.textFont(Font_5x7);
  matrix.beginText(3, 1, 0xFFFFFF);
  matrix.print(message);
  matrix.endText();

  matrix.endDraw();
}

void flashWarningSign(boolean isOn){
  if (isOn) {
    for (int i = 0; i < 15; i++) {  // Loop to blink for 5 seconds
      beeperOn(300);
      matrix.loadFrame(LEDMATRIX_DANGER);
      delay(200);
      matrix.beginDraw();
      matrix.clear();
      matrix.endDraw();
      delay(200);
    }
    digitalWrite(IR_BEAM_SWITCH, HIGH);
    Serial.println("Security man initialized");
    delay(3000);
  }

  }

void isGateOpen(boolean shouldOpen){
  int openingGateDurationMilis = 3000;

  if(shouldOpen){
    myservo.write(90);
    delay(openingGateDurationMilis);
    myservo.write(10);
    ledsState(GREEN_LED_PIN, OFF);
    ledsState(RED_LED_PIN, ON);

    }

  }


void setCurrentGateId() {
  if(isButtonPressed(GATE_SWITCH_BUTTON_PIN)) {
    if(GATE_ID == 1){
      GATE_ID = 2;
      } else{
        GATE_ID = 1;
        }
  initialState();
  }
  }