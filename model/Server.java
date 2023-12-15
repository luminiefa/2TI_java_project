package model;

import java.util.HashSet;

public class Server implements Enumeration {
	
	protected int id;
	
	protected Firewall firewall;
	protected HashSet<Service> installedServices;
	private State state;
	
	public Server(int id) {
		this.id = id; //prend l'id du dernier serveur créé et ajoute 1 pour que l'id soit unique
		this.state = State.UP;
		this.installedServices = new HashSet<>();
	}

	public int getId() {
		return id;
	}
	
	public Firewall getFirewall() {
		return firewall;
	}
	
	public void setFirewall(Firewall firewall) {
		this.firewall = firewall;
	}
	
	public State getState() {
		return state;
	}

    public void setState(State state) {
        this.state = state;
        if (state == State.DOWN) {
            // Si le serveur est éteint, éteindre tous les services installés
            for (Service service : installedServices) {
                service.setState(State.DOWN);
            }
        }
    }
    
    public void installService(Service service) {
        // Vérifier si le service a un port déjà utilisé ou s'il partage un identifiant avec un autre service
        if (!installedServices.contains(service)) {
            installedServices.add(service);
        } else {
        	throw new IllegalArgumentException("Un service similaire est déjà installé ici.");
        }
    }
    
    public void uninstallService(Service service) {
        // Vérifier si le service est installé sur le serveur
        if (installedServices.contains(service)) {
            // Retirer le service de la liste des services installés
            installedServices.remove(service);
        } else {
        	throw new IllegalArgumentException("Ce service n'est pas installé ici et ne peut donc pas être désinstallé.");
        }
    }
    
    public boolean isPortOpen(int port) {
    	// Si il rentre dans aucun if, il renvera false
        boolean isPortOpen = false;
        
        // Son firewall possède au moins une règle avec l’action ALLOW s’appliquant à ce port
        for (Rule rule : firewall.getRules()) {
        	if(rule.getPort()==port && rule.getAction()==Action.ALLOW) {
        		isPortOpen = true;
        	}
        }
        //Son firewall ne possède aucune règle avec l’action DENY s’appliquant à ce port
        for (Rule rule : firewall.getRules()) {
        	if(rule.getPort()==port && rule.getAction()==Action.DENY) {
        		isPortOpen = false;
        	}
        }
        return isPortOpen;
    }

    public boolean isServiceAvailable(Service service) {
        return installedServices.contains(service) && 	// Le service est installé sur le serveur et
        		service.getState() == State.UP && 		// L’état de ce service est UP et
        		isPortOpen(service.getPort()); 			// Le port de ce service est ouvert sur le serveur
    }

	public HashSet<Service> getInstalledServices() {
		return installedServices;
	}

	@Override
	public String toString() {
		return id + "	" + state + "	" + firewall + "		" + installedServices;
	}

}
