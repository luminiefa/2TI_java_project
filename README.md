# 2TI_java_project
Projet en duo java Q1


# Projet java - Gestion de Serveurs et Services
## Architecture
### Server

La classe Server est la classe principale représentant un serveur. Elle possède un identifiant unique, un état (UP ou DOWN), un firewall, et un ensemble de services installés. Les principales méthodes incluent :

    installService(Service service): Installe un nouveau service sur le serveur en vérifiant les contraintes.
    isPortOpen(int port): Vérifie si un port est ouvert sur le serveur en fonction des règles du firewall.
    isServiceAvailable(Service service): Vérifie si un service est disponible sur le serveur en prenant en compte son état et la configuration du firewall.
    setState(State state): Modifie l'état du serveur et éteint tous les services installés si l'état est DOWN.

### BareMetal
La classe BareMetal hérite de Server et représente un serveur physique. Elle peut héberger d'autres serveurs, tels que des Container ou des VM.

### Container
La classe Container hérite également de Server et représente un conteneur. Elle peut héberger au maximum un service et doit être installée sur un serveur BareMetal ou VM.

### VM
La classe VM est une machine virtuelle qui hérite de Server. Elle peut héberger d'autres serveurs, mais uniquement de type Container.

### Service
La classe Service représente un service installé sur un serveur. Elle possède un identifiant, un numéro de port et un état (UP ou DOWN). Les méthodes incluent setState(State state) pour modifier l'état du service.

###Firewall
La classe Firewall représente le pare-feu du serveur et contient un ensemble de règles (Rule). Chaque règle spécifie un numéro de port et une action (ALLOW ou DENY).

###Rule
La classe Rule représente une règle du firewall avec un numéro de port et une action.

###Enumeration
L'interface Enumeration contient deux énumérations : State pour représenter l'état (UP ou DOWN) et Action pour représenter l'action du firewall (ALLOW ou DENY).