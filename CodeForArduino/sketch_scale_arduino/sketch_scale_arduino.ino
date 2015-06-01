 
 #include <SoftwareSerial.h>
 
 // Define bridge excite digital IO lines
#define D0 10
#define D1 11

// Use ADC port 0
#define AN0 0
 
// Delain in microseconds to allow signal to settle 
// after excite polarity reversal
#define DELAY 800

const int bufferSize = 512;

byte buffers[bufferSize];
int bytes;

int sendWeight = 0;
double displayWeight = 0;
double tareValue;
const int nbrOfWeights = 5;
int curIndexWeight = 0;
long weights[nbrOfWeights];

SoftwareSerial mySerial(6, 9 ); // RX, TX

void setup() {
  // Setting up two Serial to be able to communicate with both the 
  // bluetooth and Wheatstone Bridge.
  Serial.begin(115200);
  mySerial.begin(9600);
  //Want both pin 10 and pin 11 top be output.
  pinMode(D0, OUTPUT);
  pinMode(D1, OUTPUT);
}

void loop() {
  initScale();
  //After initScale we want to loop through
  //1. Any message over bluetooth ?
  //  if message exist do something like ...
  //  1.1 Tare scale
  //  1.2 Send Weight over bluetooth and update weight
  //  1.3 Stop sending weight over bluetooth and dont update weight
  //If we have sendWeight activated update with the current value from scale
  // then send the value over bluetooth.
  while(1){
    if(mySerial.available()){
      int bluetoothResponse = checkForMessage(buffers);
      if(bluetoothResponse > -1){
        char tmp[bluetoothResponse];
        for(int i = 0; i < bluetoothResponse; i ++){
          tmp[i] = buffers[i];
        }
        String message(tmp);
        switch(message.toInt()){
          case 10:
            tare();              // '0' Will tare the scale
            break;
          case 11:
              sendWeight = 1;        // '1' Will start pushing the weight
            break;
          case 12:
            sendWeight = 0;          // '2' Will stop pushing the weight
            break;
        }
      }
    }
    if(sendWeight){
      curIndexWeight = (curIndexWeight + 1) % nbrOfWeights;
      weights[curIndexWeight] = (measure() - tareValue)/0.7868;
      Serial.println ( "her");
      Serial.println ( measure());
      //weights[curIndexWeight] = measureEasy() - tare;
      displayWeight = 0;
       for(int k = 0; k < nbrOfWeights; k ++){
        displayWeight = displayWeight + weights[k]; 
      }      
      displayWeight = displayWeight / nbrOfWeights;
      sendInformation(displayWeight);
      Serial.println ( displayWeight);
    }
    delay(250);
  }
}


//Method to tare the scale 
void tare(){
  tareValue = 0 ;
  for(int i = 0; i < nbrOfWeights; i ++){
      tareValue += measure();
      Serial.println ( tareValue);
  }
  tareValue = tareValue / nbrOfWeights;
  Serial.println ( tareValue);
}

// method to call first run to get good values from the start
// you could skip this but recommended to start with this.
void initScale(){
  delay(500);
  tare();
  for(int i = 0; i < nbrOfWeights; i ++){
      weights[i] = (measure() - tareValue)/0.7868;
  }
}

//Method to send information over bluetooth with an integer.
void sendInformation(int message){

 mySerial.write('#');
 mySerial.print(message);
 mySerial.write('~');
 mySerial.println();
}

//Method to send information over bluetooth with a string.
void sendInformation(String message){
  mySerial.write('#');
  char messageChar[message.length()];
  message.toCharArray(messageChar, message.length());
  mySerial.write(messageChar);
  mySerial.write('~');
}

//Method used to check the serial if there is anything to read.
//This method will check for readable information and then 
//convert this useing our protocol and see if there is any usefull message.
int checkForMessage(byte bytesRead[]){
  int i = 100;
  byte readByte;
  while(i > 0 && mySerial.available()){
    readByte = mySerial.read();
    if(readByte == '#'){
        int k = 0;
        readByte = mySerial.read();
        while(readByte != '~'){
          bytesRead[k++] = readByte;
          readByte = mySerial.read();
          i--;
          if(i <= 0){
            return -1;
          }
        }
        return k;
    }      
    i--;
  }
  return -1;
}

//Method to measure the scale 256 times and between each 
// measurement we switch the pole to get better resolution and less gitter.
long measure () {
  int a0,a1;
  long s = 220000;
  for (int i = 0; i < 256; i++) {
    digitalWrite (D0,LOW);
    digitalWrite (D1,HIGH);
    delayMicroseconds(DELAY);
    a0 = analogRead(AN0);
    
   
    // reverse polarity
     digitalWrite (D0,HIGH);
    digitalWrite (D1,LOW);
    delayMicroseconds(DELAY);
    a1 = analogRead(AN0);
    s += (long)(a0 - a1);
      
   } // next i
   // Don't care about polarity
   if ( s < 0) {
       s = -s;
   }
   
   
   // As there is a large number of samples being averaged we
   // can probably extract another bit or two from the ADC.
   // Going to be conservative and going for one extra bit.
   // So instead of dividing by 256, will divide by just 128
   // instead (ie right shift 7 bits).
   return s>>7;
}

