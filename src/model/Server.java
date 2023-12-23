package model;

import java.util.HashSet;

import data.DataSource;

public class Server implements Enumeration {
	
	protected int id;
	protected State state;
	protected HashSet<Service> installedServices;
	protected Firewall firewall;
	protected HashSet<Server> hostedServers; //liste des serveur qu'il héberge
	protected Server hostServer; //serveur dans lequel il est héberger
	protected String type;
	
	public Server(int id) {
		setId(id);
		setState(State.UP);
		this.installedServices = new HashSet<>();
		this.firewall = new Firewall();
		this.type = "Server";
	}


	public void setId(int id) {
	    for (Server server : DataSource.serverList) {
	        if (server.getId() == id) {
	            throw new IllegalArgumentException("Server with this ID already exists.");
	        }
	    }
	    this.id = id;
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
        	throw new IllegalArgumentException("This service is already installed here.");
        }
    }
    
    public void uninstallService(Service service) {
        // Vérifier si le service est installé sur le serveur
        if (installedServices.contains(service)) {
            // Retirer le service de la liste des services installés
            installedServices.remove(service);
        } else {
        	throw new IllegalArgumentException("This service is not installed here and cannot be uninstalled.");
        }
    }
    
    public boolean isPortOpen(int port) {
        // Vérifier si une règle s'applique à tous les ports (port 0)
        for (Rule rule : firewall.getRules()) {
            if (rule.getPort() == 0) {
                if (rule.getAction() == Action.ALLOW) {
                    return true;
                } else if (rule.getAction() == Action.DENY) {
                    return false;
                }
            }
        }
        
        // Si aucune règle ne s'applique à tous les ports, vérifier les règles spécifiques au port donné
        boolean isPortOpen = false;
        for (Rule rule : firewall.getRules()) {
            if (rule.getPort() == port) {
            	// Son firewall possède au moins une règle avec l’action ALLOW s’appliquant à ce port
                if (rule.getAction() == Action.ALLOW) {
                    isPortOpen = true;
                //Son firewall ne possède aucune règle avec l’action DENY s’appliquant à ce port
                } else if (rule.getAction() == Action.DENY) {
                    isPortOpen = false;
                }
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

	
	// est override dans les classes enfants qui peuvent héberger des serveurs
	public void deleteHostedServer(Server server) {
	}
	
	// est override dans les classes enfants qui peuvent héberger des serveurs
	public void addHostedServer(Server server) {
	}

	public HashSet<Server> getHostedServers() {
		return hostedServers;
	}
	
	public Server getHost() {
		return hostServer;
	}
	
	public void setHost(Server server) {
		this.hostServer = server;
	}

	

	@Override
	public String toString() {
		if (hostServer != null) {
			return id + "	" + state + "	" + type + "	" + hostServer.getId();
		} else {
			return id + "	" + state + "	" + type + "	" + "null";
		}
	}
}
