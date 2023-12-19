package model;

import java.util.HashSet;

public class VM extends Server {
	
    private HashSet<Server> hostedServers;

    public VM(int id) {
    	super(id);
        hostedServers = new HashSet<>();
    }

    public void addHostedServer(Server server) {
        if (server instanceof Container) {	
        	//les serveurs doivent être de type container
            hostedServers.add(server);
        } else {
            throw new IllegalArgumentException("Une machine virtuelle ne peut héberger que des serveurs de type Container.");
        }
    }
    
    @Override
    public void setState(State state) {
        super.setState(state);
        if (state == State.DOWN) {
            // Si la machine virtuelle est éteinte, éteindre tous les serveurs hébergés
            for (Server hostedServer : hostedServers) {
                hostedServer.setState(State.DOWN);
            }
        }
    }

}
