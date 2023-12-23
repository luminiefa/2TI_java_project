package model;

public class Container extends Server {
	
    public Container(int id) {
    	super(id);
    	this.type = "Cont.";
    }

    
    @Override //Différent de la classe parent Server car on peut installer que 1 Service sur un Container
    public void installService(Service service) {
        // Vérifier qu'il n'y a pas encore de service installé car un Container ne peut avoir qu'un service
    	if (installedServices.size() == 0) { 
    		// Vérifier si le service a un port déjà utilisé ou s'il partage un identifiant avec un autre service
	        if (!installedServices.contains(service)) {
	            installedServices.add(service);
	        } else {
	        	throw new IllegalArgumentException("A similar service is already installed here.");
	        }
        } else {
        	throw new IllegalArgumentException("A Container can only have one maximum service installed.");
        }
    }
  
}
