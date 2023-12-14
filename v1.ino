#include <SPI.h>
#include <RH_RF95.h>

// Variables pour LORA
#define RFM95_CS 10
#define RFM95_RST 9
#define RFM95_INT 2
#define RF95_FREQ 434.0
RH_RF95 rf95(RFM95_CS, RFM95_INT);
#define LED 13

// Variables du PONT H
#define pontH1 4
#define pontH2 5
#define pontH3 6
#define pontH4 7

// Variables pour la puissance des roues
#define PWM1 A3
#define PWM2 A4

// Variables pour le joystick 
#define PinX A1
#define PinY A2

// Variables pour le ultra-son
#define TRIGGER 3
#define ECHO 8
#define MAX_DISTANCE 200
const unsigned long MEASURE_TIMEOUT = 25000UL; // 25ms = 8m à 340m/s
const float SOUND_SPEED = 340.0 / 1000; // Vitesse du son dans l'air

// Variables de temps
unsigned long previousMillis = 0;
const long interval = 300; // Intervalle de mesure en millisecondes

// Variables de contrôle du robot
// define passé en const à la V1
#define DISTANCE_DECLENCHEMENT 10
#define TURN_SPEED_FACTOR 0.8  // Facteur de vitesse pour les virages (80%)

// Variable pour la gestion de direction 
int lastDirection = -1; // Dernière direction détectée

void setup() {
    pinMode(TRIGGER, OUTPUT);
    digitalWrite(TRIGGER, LOW);
    pinMode(ECHO, INPUT);

    pinMode(A0, OUTPUT);

    pinMode(LED, OUTPUT);
    pinMode(RFM95_RST, OUTPUT);
    digitalWrite(RFM95_RST, HIGH);
    while (!Serial);
    Serial.begin(9600);
    delay(10);
    Serial.println("Initialisation du LoRa...");
    digitalWrite(RFM95_RST, LOW);
    delay(10);
    digitalWrite(RFM95_RST, HIGH);
    delay(10);
    while (!rf95.init()) {
        Serial.println("Échec de l'initialisation du LoRa!");
        while (1);
    }

    Serial.println("LoRa OK!");
    if (!rf95.setFrequency(RF95_FREQ)) {
        Serial.println("Erreur setFrequency");
        while (1);
    }
    Serial.print("Fréquence établie sur: ");
    Serial.println(RF95_FREQ);

    // ajouté à la V1
    pinMode(pontH1, OUTPUT);
    pinMode(pontH2, OUTPUT);
    pinMode(pontH3, OUTPUT);
    pinMode(pontH4, OUTPUT);
}

void loop() {
    unsigned long currentMillis = millis();
    if (currentMillis - previousMillis >= interval) {
        previousMillis = currentMillis;
        
        // Envoie d'une impulsion HIGH de 10 us sur broche trigger
        digitalWrite(TRIGGER, HIGH);
        delayMicroseconds(10);
        digitalWrite(TRIGGER, LOW);

        // Mesure le temps entre l'envoi de l'impulsion et son echo
        long measure = pulseIn(ECHO, HIGH, MEASURE_TIMEOUT);

        // Calcul de la distance en millimètres et conversion en centimètres
        float distance_mm = measure / 2.0 * SOUND_SPEED;
        float distance_cm = distance_mm / 10.0;

        // Affichage de la distance
        Serial.print("Distance : ");
        Serial.print(distance_cm, 2); // Affiche deux décimales
        Serial.println(" cm de ");

        if (rf95.available()) {
            uint8_t buf[RH_RF95_MAX_MESSAGE_LEN];
            uint8_t len = sizeof(buf);
            if (rf95.recv(buf, &len)) {
                int VRX, VRY, push;
                sscanf((char *)buf, "%d#%d#%d", &VRY, &VRX, &push);
                Serial.print("VRx=");
                Serial.println(VRX);
                Serial.print("VRy=");
                Serial.println(VRY);

                digitalWrite(A0, push);


                int directionX = map(VRX, 0, 1023, -127, 127);
                int directionY = map(VRY, 0, 1023, -127, 127);

                int currentDirection = -1;
                Serial.println(distance_cm);

                int absDirectionY = abs(directionY);
                int absDirectionX = abs(directionX);

                if (distance_cm < DISTANCE_DECLENCHEMENT && distance_cm != 0) {
                    // Arrêter le véhicule s'il y a un obstacle à proximité
                    // Reculer
                        digitalWrite(pontH1, HIGH);
                        digitalWrite(pontH2, LOW);
                        digitalWrite(pontH3, LOW);
                        digitalWrite(pontH4, HIGH);
                        int speed = 255; // Vitesse maximale
                        analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
                        analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
                        currentDirection = 7;
                    lastDirection = -1;
                } else {
                    if (directionX < -9 && directionY > 9) {
                        // Avant Gauche
                        digitalWrite(pontH1, LOW);
                        digitalWrite(pontH2, HIGH);
                        digitalWrite(pontH3, HIGH);
                        digitalWrite(pontH4, LOW);
                        int speed = 255; // Vitesse maximale
                        int leftSpeed = speed * TURN_SPEED_FACTOR;
                        analogWrite(PWM1, leftSpeed);  // Contrôle de la vitesse de la roue gauche
                        analogWrite(PWM2, speed);       // Contrôle de la vitesse de la roue droite
                        currentDirection = 1;
                    } else if (directionX > 10 && directionY > 10) {
                        // Reculer gauche
                        digitalWrite(pontH1, HIGH);
                        digitalWrite(pontH2, LOW);
                        digitalWrite(pontH3, LOW);
                        digitalWrite(pontH4, HIGH);
                        int speed = 255; // Vitesse maximale
                        int leftSpeed = speed * TURN_SPEED_FACTOR;
                        analogWrite(PWM1, leftSpeed);  // Contrôle de la vitesse de la roue gauche
                        analogWrite(PWM2, speed);       // Contrôle de la vitesse de la roue droite
                        currentDirection = 3;
                    } else if (directionX < -10 && directionY < -10) {
                        // Avant droite
                        digitalWrite(pontH1, LOW);
                        digitalWrite(pontH2, HIGH);
                        digitalWrite(pontH3, LOW);
                        digitalWrite(pontH4, HIGH);
                        int speed = 255; // Vitesse maximale
                        int rightSpeed = speed * TURN_SPEED_FACTOR;
                        analogWrite(PWM1, speed);       // Contrôle de la vitesse de la roue gauche
                        analogWrite(PWM2, rightSpeed);  // Contrôle de la vitesse de la roue droite
                        currentDirection = 2;
                    } else if (directionX > 10 && directionY < -10) {
                        // Reculer droite
                        digitalWrite(pontH1, LOW);
                        digitalWrite(pontH2, HIGH);
                        digitalWrite(pontH3, HIGH);
                        digitalWrite(pontH4, LOW);
                        int speed = 255; // Vitesse maximale
                        int rightSpeed = speed * TURN_SPEED_FACTOR;
                        analogWrite(PWM1, speed);       // Contrôle de la vitesse de la roue gauche
                        analogWrite(PWM2, rightSpeed);  // Contrôle de la vitesse de la roue droite
                        currentDirection = 4;
                    } else if (directionX < -10 && directionY >= -10 && directionY <= 10) {
                        // Avant
                        digitalWrite(pontH1, LOW);
                        digitalWrite(pontH2, HIGH);
                        digitalWrite(pontH3, HIGH);
                        digitalWrite(pontH4, LOW);
                        
                        int speed = 255; // Vitesse maximale
                        analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
                        analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
                        currentDirection = 8;
                    } else if (directionX > 10 && directionY >= -10 && directionY <= 10) {
                        // Reculer
                        digitalWrite(pontH1, HIGH);
                        digitalWrite(pontH2, LOW);
                        digitalWrite(pontH3, LOW);
                        digitalWrite(pontH4, HIGH);
                        int speed = 255; // Vitesse maximale
                        analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
                        analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
                        currentDirection = 7;
                    } else if (directionY < -10) {
                        // Droite
                        digitalWrite(pontH1, HIGH);
                        digitalWrite(pontH2, LOW);
                        digitalWrite(pontH3, HIGH);
                        digitalWrite(pontH4, LOW);
                        int speed = 255; // Vitesse maximale
                        analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
                        analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
                        currentDirection = 6;
                    } else if (directionY > 7) {
                        // Gauche
                        digitalWrite(pontH1, LOW);
                        digitalWrite(pontH2, HIGH);
                        digitalWrite(pontH3, LOW);
                        digitalWrite(pontH4, HIGH);
                        int speed = 255; // Vitesse maximale
                        analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
                        analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
                        currentDirection = 5;
                    } else {
                        digitalWrite(pontH1, LOW);
                        digitalWrite(pontH2, LOW);
                        digitalWrite(pontH3, LOW);
                        digitalWrite(pontH4, LOW);
                        analogWrite(PWM1, 0); // Roue gauche à l'arrêt
                        analogWrite(PWM2, 0); // Roue droite à l'arrêt
                        lastDirection = -1;
                    }

                    if (currentDirection != lastDirection) {
                        // La direction a changé, mettre à jour l'affichage LCD
                        if (currentDirection != -1) {
                            switch (currentDirection) {
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 7:
                                    break;
                                case 8:
                                    break;
                            }
                            lastDirection = currentDirection;
                        }
                    } else if (currentDirection == -1 && lastDirection != -1) {
                        // Aucun mouvement détecté, effacer l'écran LCD
                        lastDirection = -1;
                    }
                }
                currentDirection = -1; // -1 pour aucune direction
            } else {
                Serial.println("Échec de la réception");
            }
        }
    }
}
