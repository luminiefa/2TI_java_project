package control;


import java.util.HashSet;

import control.menu.FirewallMenu;
import data.DataSource;
import model.Enumeration.Action;
import model.Firewall;
import model.Rule;
import model.Server;
import ui.input.Input;
import ui.output.Output;

public class FirewallController extends MenuController<FirewallMenu.FirewallMenuAction> {

	private Server server = DataSource.selectedServer;
	private Firewall firewall = server.getFirewall();
	private HashSet<Rule> rules = firewall.getRules();
	
	// MainController uses a MainMenu
	public FirewallController() {
		super(new FirewallMenu());
	}

	public void processAction() {

		// Execute some action
		// Menu principal
		switch (this.getMenuAction()) {
		case LIST:
			this.listFirewallRules();
			break;
		case ADD:
			this.addRule();
			break;
		case DELETE:
			this.deleteRule();
			break;
		case TEST:
			this.testPort();
			break;
		case EXIT:
			this.exitRequest();
			break;
		}
	}
	

	private void listFirewallRules() {
		Output.message("ACTION	PORT");
		for (Rule rule : rules) {
			Output.message(rule.toString());
		}
	}
	
	
	private void addRule() {
		
		String portString = Input.readString("Enter the rule port");
		try {
			int port = Integer.parseInt(portString); // Transforme le port en integer
			
			if (port >= 0 && port <= 1024) {
				for (Rule rule : rules) {
		            if (rule.getPort() == port) {
		            	Output.message("Error: A rule already exists on this port.");
		                return; // Sortir de la méthode si le nom n'est pas unique
		            }
		        }
				String action = Input.readString("Enter an action [ALLOW/DENY]");
				
				if (action.equals("ALLOW")) {
					firewall.addRule(new Rule(port, Action.ALLOW)); //Ajoute le service
					Output.message("An ALLOW rule has been added to port " + port);
					
				} else if (action.equals("DENY")){
					firewall.addRule(new Rule(port, Action.DENY)); //Ajoute le service
					Output.message("An DENY rule has been added to port " + port);
					
				} else {Output.message("Error: Invalid action format. Please write ALLOW or DENY in all capital letters.");}
				
			} else {Output.message("Error: Invalid port format. Please enter a number between 0 and 1024.");}
			
		} catch (NumberFormatException e) { // Si arrive pas à tranformer le port en integer renvoie erreur
	    	Output.message("Error: Invalid port format. Please enter a valid integer.");
	    }
	}
	
	
	private void deleteRule() {
		String portString = Input.readString("Enter the rule port");
		try {
			int port = Integer.parseInt(portString); // Transforme le port en integer
			
			boolean removed = false;
	        for (Rule rule : rules) {
	            if (rule.getPort() == port) {
	            	firewall.removeRule(rule);;
	                Output.message("The rule on port " + port + " has been deleted.");
	                removed = true; //indique qu'il a supprimé quelque chose
	                break; // arrête la boucle for si service trouvé
	            }
	        }
	        if (!removed) { //Si rien de supprimé après la boucle : erreur
	            Output.message("No rule found on port " + port);
	        }
			
		} catch (NumberFormatException e) { // Si arrive pas à tranformer le port en integer renvoie erreur
	    	Output.message("Error: Invalid port format. Please enter a valid integer.");
	    }
		
	}
	
	private void testPort() {
		String portString = Input.readString("Enter the rule port");
		try {
			int port = Integer.parseInt(portString); // Transforme le port en integer
			if (server.isPortOpen(port)) {
				Output.message("Port " + port + " is OPEN.");
			} else {
				Output.message("Port " + port + " is CLOSED.");
			}
			
		} catch (NumberFormatException e) { // Si arrive pas à tranformer le port en integer renvoie erreur
	    	Output.message("Error: Invalid port format. Please enter a valid integer.");
	    }
	}

}
	
