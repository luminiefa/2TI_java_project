package model;

public class Service implements Enumeration {
	
	private String id;
	private int port;
	private State state;
	
	public Service(String id, int port) {
		this.id = id;
		setPort(port);
		this.state = State.DOWN;
	}
	
	public String getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int newPort) {
        if (newPort >= 1 && newPort <= 1024) {
        	//doit être compris entre 1 et 1024
            this.port = newPort;
        } 
        else {
            throw new IllegalArgumentException("The port number must be an integer between 1 and 1024.");
        }
    }

    public State getState() {
        return state;
    }


    public void setState(State newState) {
        this.state = newState;
    }

	@Override
	public String toString() {
		return id + "	"  + state + "	" + port;
	}

}
