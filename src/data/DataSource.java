package data;

import java.util.ArrayList;
import java.util.List;

import model.Server;

/**
 * Contains the program data
 */
public class DataSource {
	
	//Liste contenant les serveurs
	public static final List<Server> serverList = new ArrayList<>();
	
	//Serveur selectionné depuis le main menu
	public static Server selectedServer;

}
