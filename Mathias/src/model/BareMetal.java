package model;

import java.util.HashSet;

public class BareMetal extends Server {
	
    private HashSet<Server> hostedServers;

    public BareMetal() {
        hostedServers = new HashSet<>();
    }

    public void addHostedServer(Server server) {
        if (!(server instanceof BareMetal)) {	
        	//les serveurs BareMetal ne peuvent pas être hébergés
            hostedServers.add(server);
        } else {
            throw new IllegalArgumentException("Un serveur BareMetal ne peut pas être hébergé.");
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
