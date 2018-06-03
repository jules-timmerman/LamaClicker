package com.snoopinou.lama;

public class Calcul implements Runnable {

	
	public Calcul() {
		super();
	}
	
	
	@Override
	public void run() {
		
		while(true) {
			int tot = 0;
			for(Upgrades up : Upgrades.values()) {
				tot += up.getQuantite()*up.getVal();
			}
			Main.fenetre.addTot(tot);
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	
	}
}
