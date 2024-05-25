#include <LiquidCrystal_I2C.h>
#include <SPI.h>
#include <MFRC522.h>
#include <RTC.h>
#include "arduino_secrets.h"
#include <ArduinoJson.h>
#include <WiFiS3.h>
#include "ArduinoGraphics.h"
#include "Arduino_LED_Matrix.h"

struct ResponseData {
  const char* firstRowText;
  const char* secondRowText;
  const boolean greenLedOn;

  ResponseData(const char* firstRow, const char* secondRow, boolean greenOn)
    : firstRowText(firstRow), secondRowText(secondRow), greenLedOn(greenOn) {}
};

// Constants
const int GATE_ID = 1;
const int ON = HIGH;
const int OFF = LOW;
const int RED_LED_PIN = A1;
const int GREEN_LED_PIN = A0;
const int SS_PIN = 10;
const int RST_PIN = 9;
const int BUZZER_PIN = A2;
const int INFO_BUTTON_PIN = 2;

const char* SERVER = "192.168.0.21";
const int PORT_NO = 8080;
const String LOGGING_ENDPOINT = String(GATE_ID) + "/?uid=";
const String GET_LAST_LOG_ENDPOINT = String(GATE_ID) + "/lastLogInfo/?uid=";

// Network credentials
char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;


// Objects
LiquidCrystal_I2C lcd(0x27, 16, 2);
MFRC522 mfrc522(SS_PIN, RST_PIN);
RTCTime currentTime;
WiFiClient client;
ArduinoLEDMatrix matrix;


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

  if (isTagDetected()) {
    if(isButtonPressed()){
      processTag(GET_LAST_LOG_ENDPOINT);
    }else {
      processTag(LOGGING_ENDPOINT);
    }

  }

  delay(1000);
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

  delay(3000);
  initialState();
}

void initializeHardware() {
  pinMode(RED_LED_PIN, OUTPUT);
  pinMode(GREEN_LED_PIN, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(INFO_BUTTON_PIN, INPUT);

  lcd.init();
  lcd.backlight();
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();
  RTC.begin();
  matrix.begin();
}

void initialState() {
  lcdScreenInfo1stRow("Scan your tag");
  ledsState(RED_LED_PIN, ON);
  ledsState(GREEN_LED_PIN, OFF);
}

boolean isTagDetected() {
  return mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial();
}

boolean isButtonPressed(){
  return digitalRead(INFO_BUTTON_PIN) == HIGH;
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
  String request = "GET /" + endpointName + cardUid + " HTTP/1.1";

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


  if (firstRowText || secondRowText || greenLedOn) {
    Serial.println("Response from server: ");
    Serial.print("First row text: ");
    Serial.println(firstRowText);
    Serial.print("Second row text: ");
    Serial.println(secondRowText);
    Serial.print("Turn GREEN led on?: ");
    Serial.println(greenLedOn);

  } else {
    Serial.println("All fields not found in JSON");
  }



  if (error) {
    Serial.print("deserializeJson() failed: ");
    Serial.println(error.c_str());
    return ResponseData("", "", false);
  }

  return ResponseData(firstRowText, secondRowText, greenLedOn);
}

void weclomeMatrixText(){
  matrix.beginDraw();

  matrix.stroke(0xFFFFFFFF);
  matrix.textScrollSpeed(50);

  // add the text
  const char text[] = "Welcome to Entry Systems";
  matrix.textFont(Font_5x7);
  matrix.beginText(0, 1, 0xFFFFFF);
  matrix.println(text);
  matrix.endText(SCROLL_LEFT);

  matrix.endDraw();

}

