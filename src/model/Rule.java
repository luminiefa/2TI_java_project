package model;

public class Rule implements Enumeration {
	
	private int port;
	private Action action;
	
	public Rule(int port, Action action) {
		setPort(port);
		this.action = action;
	}

	
    public int getPort() {
        return port;
    }
	
    public void setPort(int newPort) {
        if (newPort >= 0 && newPort <= 1024) {
        	//doit être compris entre 0 et 1024
            this.port = newPort;
        } 
        else {
            throw new IllegalArgumentException("Le numéro de port doit être un entier entre 0 et 1024.");
        }
    }

	public Action getAction() {
		return action;
	}


	public void setAction(Action newAction) {
		this.action = newAction;
	}
	
    
}
