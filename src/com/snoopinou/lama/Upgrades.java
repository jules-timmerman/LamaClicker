package com.snoopinou.lama;

import javax.swing.JOptionPane;

public enum Upgrades {
	
	AutoClick("AutoClick", 1, 10, 0.1), // Nom, val, prix, croissance
	Elevage("Elevage", 10, 50, 0.2),
	Incubateur("Incubateur", 100, 500, 0.3),
	Clonage("Clonage", 500, 1000, 0.4),
	Corobizar("Corobizar", 1000, 3000, 0.5);
	
	private String nom = ""; // Self-explanatory
	private int quantite = 0; // Tot
	private int val = 0; // Combien par sec
	private int prix = 0; // Prix
	private double croissance = 0; // Coeff pour chaque achat
	
	private int prixOri = 0; // Pour reset
	
	Upgrades(String nom, int val, int prix, double croissance){
		
		this.nom = nom;
		this.val = val;
		this.prix = prix;
		this.croissance = croissance;
		
		this.prixOri = prix;
	}
	
	public void achat() {
		
		if(Main.fenetre.getTot() >= this.prix) { // Si assez d'argent
			Main.fenetre.removeTot((int) Math.round(this.prix));
			this.prix += this.prix*this.croissance;
			this.quantite++;
			
			Main.fenetre.actuUp();
			Main.fenetre.actuSec();
		}else {
			JOptionPane.showMessageDialog(null, "Pas assez de lama", "Pauvre", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int getVal() {
		return this.val;
	}
	
	public int getQuantite() {
		return this.quantite;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public int getPrix() {
		return this.prix;
	}
	
	public void setQuantite(int i ) {
		this.quantite = i;
	}
	
	public void setPrix(int i ) {
		this.prix = i;
	}
	
	public void reset() {
		this.quantite = 0; // Tot
		this.prix = this.prixOri; // Prix
	}
	
}
