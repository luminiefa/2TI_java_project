package model;

import java.util.HashSet;

public class Firewall implements Enumeration {
	
	private HashSet<Rule> rules;
	
	
	public Firewall() {
		rules = new HashSet<>();			//Lorsqu’il est créé, un firewall contient toujours une seule règle,
		addRule(new Rule(0, Action.DENY));	//pour le numéro de port 0 avec l’action DENY.
	}
	
	
	public HashSet<Rule> getRules() {
		return rules;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
	}
	
	public void removeRule(Rule rule) {
		rules.remove(rule);
	}

}
