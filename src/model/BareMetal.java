package model;

import java.util.HashSet;

public class BareMetal extends Server {
	
	private HashSet<Server> hostedServers;

    public BareMetal(int id) {
    	super(id);
        hostedServers = new HashSet<>();
        this.type = "BMetal";
    }

    @Override
    public void addHostedServer(Server server) {
        if (!(server instanceof BareMetal)) {	
        	//les serveurs BareMetal ne peuvent pas être hébergés
            hostedServers.add(server);
            server.setHost(this);
        } else {
            throw new IllegalArgumentException("A BareMetal server cannot be hosted.");
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
            // Si le serveur BareMetal est éteint, éteindre tous les serveurs hébergés
            for (Server hostedServer : hostedServers) {
                hostedServer.setState(State.DOWN);
            }
        }
    }

}
