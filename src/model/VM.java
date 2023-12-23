package model;

import java.util.HashSet;

public class VM extends Server {

    public VM(int id) {
    	super(id);
        hostedServers = new HashSet<>();
        this.type = "VM";
    }

    
    @Override
    public void addHostedServer(Server server) {
        if (server instanceof Container) {	
        	//les serveurs doivent être de type container
            hostedServers.add(server);
            server.setHost(this);
        } else {
            throw new IllegalArgumentException("A virtual machine can only host Container servers.");
        }
    }
    
    
    @Override
	public void deleteHostedServer(Server server) {
		if (hostedServers.contains(server)) {
			hostedServers.remove(server);
			server.setHost(null);
		} else {
            throw new IllegalArgumentException("The server you are trying to remove is not hosted here.");
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
