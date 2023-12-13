#include <SPI.h>
#include <RH_RF95.h>
#include <LiquidCrystal.h>

// Variables pour l'écran LCD
int rs = 12;
int en = 11;
int d4 = 5;
int d5 = 4;
int d6 = 3;
int d7 = 2;

// Création de l'objet lcd
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

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
const int MAX_DISTANCE 200;
const unsigned long MEASURE_TIMEOUT = 25000UL; // 25ms = 8m à 340m/s
const float SOUND_SPEED = 340.0 / 1000; // Vitesse du son dans l'air

// Variables de temps
unsigned long previousMillis = 0;
const long interval = 300; // Intervalle de mesure en millisecondes

// Variables de contrôle du robot
// define passé en const à la V1
const int DISTANCE_DECLENCHEMENT 10;
const float TURN_SPEED_FACTOR 0.8;  // Facteur de vitesse pour les virages (80%) donc la roue qui devras faire le moin de distance, rouleras moin vite

// Variable pour la gestion de direction 
int lastDirection = -1; // Dernière direction détectée

// Variables pour la gestion du temps sur la fonction d'incrémentation
const int boutonPin = 2;  // Broche à laquelle le bouton est connecté
int compteur = 0;         // Compteur à incrémenter
unsigned long dernierAppui = 0;  // Temps du dernier appui sur le bouton
const int delaiAntiRebond = 50;  // Durée anti-rebond du bouton en millisecondes
const int delaiAffichage = 500;  // Intervalle d'affichage du compteur en millisecondes



void setup() {
    // Configure la broche du bouton en entrée avec une résistance de tirage vers le haut
    pinMode(boutonPin, INPUT_PULLUP);

    // Init pontH
    pinMode(pontH1, OUTPUT);
    pinMode(pontH2, OUTPUT);
    pinMode(pontH3, OUTPUT);
    pinMode(pontH4, OUTPUT);

    // Init PWM pontH
    pinMode(PWM1, OUTPUT);
    pinMode(PWM2, OUTPUT);

    // Init ultra-son
    pinMode(TRIGGER, OUTPUT);
    digitalWrite(TRIGGER, LOW);
    pinMode(ECHO, INPUT);

    // Init LCD
    lcd.begin(16, 2); // Définition de la taille écran
    lcd.clear();

    // Init LORA
    pinMode(LED, OUTPUT);
    pinMode(RFM95_RST, OUTPUT);
    digitalWrite(RFM95_RST, HIGH);
    while (!Serial);
    Serial.begin(9600);
    delay(50);
    digitalWrite(RFM95_RST, LOW);
    delay(10);
    digitalWrite(RFM95_RST, HIGH);
    delay(10);
}

void loop() {
    int mode = incrementerCompteur();
    // Préparation de millis pour le capteur ultra-son
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

        if (rf95.available()) {
            uint8_t buf[RH_RF95_MAX_MESSAGE_LEN]; //?
            uint8_t len = sizeof(buf);
            if (rf95.recv(buf, &len)) {
                // séparation des valeurs reçue par LORA dans VRX et VRY
                int VRX, VRY;
                sscanf((char *)buf, "%d#%d", &VRX, &VRY);

                // map entre le bouton et la direction. 0 vaudras -127 et 1023 vaudras 127
                int directionX = map(VRX, 0, 1023, -127, 127);
                int directionY = map(VRY, 0, 1023, -127, 127);

                // de base la postion est en neutre donc -1
                int currentDirection = -1;

                // comme de base la voiture est en neutre, la vitesse est de 0
                int speed = 0;
                
                // variable qui gère la diagonale X et Y et traduit en nombre pour la vitesse
                float sqrtXY = sqrt(pow(directionX, 2) + pow(directionY, 2));
                
                // comme les direction renvoient de -127 à 127 on garde que de 0 à 127 et on map de 0 à 255 qui est la vitesse max
                int absDirectionY = abs(directionY);
                int absDirectionX = abs(directionX);

                // si obstacle détecté stop le vehicule
                if (distance_cm < DISTANCE_DECLENCHEMENT && distance_cm != 0) {
                    stopVehicle();
                    lastDirection = -1;
                    // reset de l'écran
                    clearScreen();
                } else {
                    // pas d'obstacle le véhicule peut bouger
                    if (directionX < -9 && directionY > 9) {
                        // Avant Gauche
                        speed = map(sqrtXY, 0, 127, 0, 255);
                        currentDirection = 1;
                        
                    } else if (directionX > 10 && directionY > 10) {
                        // Reculer gauche
                        speed = map(sqrtXY, 0, 127, 0, 255);
                        currentDirection = 3;
                        
                    } else if (directionX < -10 && directionY < -10) {
                        // Avant droite
                        speed = map(sqrtXY, 0, 127, 0, 255);
                        currentDirection = 2;
                        
                    } else if (directionX > 10 && directionY < -10) {
                        // Reculer droite
                        speed = map(sqrtXY, 0, 127, 0, 255);
                        currentDirection = 4;
                        
                    } else if (directionX < -10 && directionY >= -10 && directionY <= 10) {
                        // Avant
                        speed = map(absDirectionY, 0, 127, 0, 255);
                        currentDirection = 8;
                        
                    } else if (directionX > 10 && directionY >= -10 && directionY <= 10) {
                        // Reculer
                        speed = map(absDirectionY, 0, 127, 0, 255);
                        currentDirection = 7;
                        
                    } else if (directionY < -10) {
                        // Droite
                        speed = map(absDirectionX, 0, 127, 0, 255);
                        currentDirection = 6;
                        
                    } else if (directionY > 6) {
                        // Gauche
                        speed = map(absDirectionX, 0, 127, 0, 255);
                        currentDirection = 5;
                        
                    } else {
                        stopVehicle();
                        lastDirection = -1;
                        clearScreen();
                    }

                    // Vérification de modification de direction
                    if (currentDirection != lastDirection) {
                        // La direction a changé, mettre à jour l'affichage LCD
                        if (currentDirection != -1) {
                            setDirection(currentDirection, speed, mode);
                            printOnScreenDirection(directionX, directionY, mode);
                        }
                        lastDirection = currentDirection;
                    } else if (currentDirection == -1 && lastDirection != -1) {
                        // Aucun mouvement détecté, effacer l'écran LCD
                        lastDirection = -1;
                        clearScreen();
                    }
                }
                currentDirection = -1; // -1 pour aucune direction
            }
        }
    }
}

void setDirection(int direction, int speed, int mode) {
    // Pas de direction
    digitalWrite(pontH1, LOW);
    digitalWrite(pontH2, LOW);
    digitalWrite(pontH3, LOW);
    digitalWrite(pontH4, LOW);

    speed = speed * (mode == 1 ? 1.0 : 0.5);
    int leftSpeed, rightSpeed;

    switch (direction) {
        case 1:  // Avant Gauche
            digitalWrite(pontH1, LOW);
            digitalWrite(pontH2, HIGH);
            digitalWrite(pontH3, HIGH);
            digitalWrite(pontH4, LOW);
            leftSpeed =  speed * TURN_SPEED_FACTOR; // TURN_SPEED_FACTOR fait que la roue va à 80% de la vitesse
            leftSpeed = leftSpeed * (mode == 1 ? 1.0 : 0.5); // Déginit la vitesse aussi selon le mode
            analogWrite(PWM1, leftSpeed);  // Contrôle de la vitesse de la roue gauche
            analogWrite(PWM2, speed);      // Contrôle de la vitesse de la roue droite
            break;
        case 2:  // Avant Droite
            digitalWrite(pontH1, LOW);
            digitalWrite(pontH2, HIGH);
            digitalWrite(pontH3, LOW);
            digitalWrite(pontH4, HIGH);
            rightSpeed = speed * TURN_SPEED_FACTOR; // TURN_SPEED_FACTOR fait que la roue va à 80% de la vitesse
            rightSpeed = rightSpeed * (mode == 1 ? 1.0 : 0.5); // Déginit la vitesse aussi selon le mode
            analogWrite(PWM1, speed);       // Contrôle de la vitesse de la roue gauche
            analogWrite(PWM2, rightSpeed);  // Contrôle de la vitesse de la roue droite
            break;
        case 3:  // Reculer Gauche
            digitalWrite(pontH1, HIGH);
            digitalWrite(pontH2, LOW);
            digitalWrite(pontH3, LOW);
            digitalWrite(pontH4, HIGH);
            leftSpeed = speed * TURN_SPEED_FACTOR; // TURN_SPEED_FACTOR fait que la roue va à 80% de la vitesse
            leftSpeed = leftSpeed * (mode == 1 ? 1.0 : 0.5); // Déginit la vitesse aussi selon le mode
            analogWrite(PWM1, leftSpeed);  // Contrôle de la vitesse de la roue gauche
            analogWrite(PWM2, speed);      // Contrôle de la vitesse de la roue droite
            break;
        case 4: // Reculer droite
            digitalWrite(pontH1, LOW);
            digitalWrite(pontH2, HIGH);
            digitalWrite(pontH3, HIGH);
            digitalWrite(pontH4, LOW);
            rightSpeed = speed * TURN_SPEED_FACTOR; // TURN_SPEED_FACTOR fait que la roue va à 80% de la vitesse
            rightSpeed = rightSpeed * (mode == 1 ? 1.0 : 0.5); // Déginit la vitesse aussi selon le mode
            analogWrite(PWM1, speed);       // Contrôle de la vitesse de la roue gauche
            analogWrite(PWM2, rightSpeed);  // Contrôle de la vitesse de la roue droite
            break;
        case 5:  // Gauche
            digitalWrite(pontH1, LOW);
            digitalWrite(pontH2, HIGH);
            digitalWrite(pontH3, LOW);
            digitalWrite(pontH4, HIGH);
            analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
            analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
            break;
        case 6:  // Droite
            digitalWrite(pontH1, HIGH);
            digitalWrite(pontH2, LOW);
            digitalWrite(pontH3, HIGH);
            digitalWrite(pontH4, LOW);
            analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
            analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
            break;
        case 7: // Reculer 
            digitalWrite(pontH1, HIGH);
            digitalWrite(pontH2, LOW);
            digitalWrite(pontH3, LOW);
            digitalWrite(pontH4, HIGH);
            analogWrite(PWM1, speed);  // Les roues fonctionnent à la même vitesse
            analogWrite(PWM2, speed);  // Les roues fonctionnent à la même vitesse
            break;
        case 8:  // Avant
            digitalWrite(pontH1, LOW);
            digitalWrite(pontH2, LOW);
            digitalWrite(pontH3, LOW);
            digitalWrite(pontH4, LOW);
            analogWrite(PWM1, speed); // Les roues fonctionnent à la même vitesse
            analogWrite(PWM2, speed); // Les roues fonctionnent à la même vitesse
            break;
    }
}

void stopVehicle() {
    digitalWrite(pontH1, LOW);
    digitalWrite(pontH2, LOW);
    digitalWrite(pontH3, LOW);
    digitalWrite(pontH4, LOW);
    analogWrite(PWM1, 0); // Les roues ne bougent pas
    analogWrite(PWM2, 0); // Les roues ne bougent pas
}

void printOnScreenDirection(int directionX, int directionY, int mode) {
    lcd.setCursor(0, 1); // Définit le curseur à la première colonne (0) de la deuxième ligne (ligne 1).
    lcd.print("X: ");
    lcd.print(directionX); // Affiche la valeur de directionX.
    lcd.setCursor(5, 1);
    lcd.print(" Y: ");
    lcd.print(directionY);

    // Si le mode est à 1, afficher le mode sur l'écran LCD
    if (mode == 1) {
        lcd.setCursor(10, 1);
        lcd.print("Mode: 1");
    } else {
        lcd.setCursor(10, 1);
        lcd.print("Mode: 0");
    }
}

void clearScreen() {
    lcd.clear();
}


int incrementerCompteur() {
  // Vérifie si le bouton est enfoncé et suffisamment de temps s'est écoulé depuis le dernier appui
  if (digitalRead(boutonPin) == LOW && (millis() - dernierAppui >= delaiAntiRebond)) {
    dernierAppui = millis();  // Met à jour le temps du dernier appui
    
    // Incrémente le compteur
    compteur = 1 - compteur;  // Passe de 0 à 1 et vice versa
  }
  
  return compteur;
}


