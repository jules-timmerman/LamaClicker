package com.snoopinou.lama;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class Fenetre extends JFrame{
	
	private Path save = Paths.get("save.txt");
	
	private JPanel contentPane = new JPanel();
	private JLabel labelTot = new JLabel("0 lamas total");
	private JLabel labelSec = new JLabel("0 per min");
	private ImageButton button = new ImageButton("/resources/lama.png");
//	private JPanel panelUpUp = new JPanel(); // Upgrades des upgrades
//	private JScrollPane scrollUpUp = new JScrollPane(panelUpUp);
	private JPanel panelUp = new JPanel();	// Upgrades
	private JScrollPane scrollUp; // Le scroll
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file = new JMenu("File");
	private JMenuItem deleteSave = new JMenuItem("Delete Save");
	
	private int tot = 0; // Cookie total
	private int sec = 0; // Cookie par seconde
	
	private int click = 1; // Cookie par click
	
	
	public Fenetre() {
		
		loadSave();
		
		this.setTitle("LamaClicker");
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e){
				
				try(BufferedWriter writer = Files.newBufferedWriter(save)){
					
					String str = "";
					str += tot+"\n";
					str += sec+"\n";
					str += click+"\n";
					
					for(Upgrades up : Upgrades.values()) {
						str += up.getQuantite()+"|";
						str += up.getPrix()+"\n";
					}
					
					writer.write(str);
					
					
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
			
		});
		
		initComp();
		
		this.setVisible(true);
		
		Thread t = new Thread(new Calcul());
		t.start();
	}
	
	private void loadSave() {
		
		try(BufferedReader reader = Files.newBufferedReader(save)){
			
			tot = Integer.parseInt(reader.readLine());
			sec = Integer.parseInt(reader.readLine());
			click = Integer.parseInt(reader.readLine());
			
			for(Upgrades up : Upgrades.values()) {
				String str = reader.readLine();
				up.setQuantite(Integer.parseInt(str.substring(0, str.indexOf("|"))));
				up.setPrix(Integer.parseInt(str.substring(str.indexOf("|")+1)));
			}
			
			
			actuSec();
			
		} catch(NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Sauvegarde inexistante ou corrompu", "Erreur lors du chargement de la sauvegarde", JOptionPane.ERROR_MESSAGE);
			
			deleteSave.doClick(); // Everything 0
			
//		} catch (NoSuchFileException e) {
//			JOptionPane.showMessageDialog(null, "Sauvegarde inexistante ou corrompu", "Erreur lors du chargement de la sauvegarde", JOptionPane.ERROR_MESSAGE);
//			
//			tot = 0;
//			sec = 0;
//			click = 0;
//			
//			for(Upgrades up : Upgrades.values()) {
//				up.reset();
//			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Sauvegarde inexistante ou corrompu", "Erreur lors du chargement de la sauvegarde", JOptionPane.ERROR_MESSAGE);
			
			deleteSave.doClick(); // Everything 0
		}
		
	}
	
	
	
	
	private void initComp() {
		
		
		button.setBorderPainted(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tot += click;
				labelTot.setText(tot+" lamas total");
			}
		});
		
		scrollUp = new JScrollPane(panelUp);
		panelUp.setLayout(new GridLayout(1,0));
		actuUp();
		
		contentPane.setLayout(new MigLayout(""));
		
		contentPane.add(labelTot, "split 2, flowy, center");
		contentPane.add(labelSec, "center, wrap");
//		contentPane.add(scrollUpUp, "spany 2, wrap, growy");
		
		contentPane.add(button, "wrap, grow, push");
		
		contentPane.add(scrollUp, "spanx 2, grow, push 10 10");
		
		deleteSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tot = 0; // Cookie total
				sec = 0; // Cookie par seconde
				click = 1; // Cookie par click
				
				for(Upgrades up : Upgrades.values()) {
					up.reset();
				}
				actuUp();
				actuSec();
			}
		});
		
		file.add(deleteSave);
		
		menuBar.add(file);
		
		this.setJMenuBar(menuBar);
		
		this.setContentPane(contentPane);
	}

	
	
	
	
	
	
	
	public void actuUp() {
		panelUp.removeAll();
		for(Upgrades up : Upgrades.values()) {
			
			JButton b = new JButton();
			
			String str = "<html>"+up.getNom()+"<br />"+up.getPrix()+"<br />"+up.getQuantite()+" total</html>";
			b.setText(str);
			b.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					up.achat();
				}
				
			});
			panelUp.add(b);
			
		}
	}
	
	
	public void actuSec() {
		int tot = 0;
		for(Upgrades up : Upgrades.values()) {
			tot += up.getVal()*up.getQuantite();
		}
		this.setSec(tot);
	}
	
	
	public int getTot() {
		return tot;
	}

	public int getSec() {
		return sec;
	}
	
	public void removeTot(int i) { // Soustraie au total i
		this.tot -= i;
		this.labelTot.setText(this.tot+" lamas total");
	}
	
	public void addTot(int i ) {
		this.tot += i;
		this.labelTot.setText(this.tot+" lamas total");
	}
	
	public void setSec(int i) {
		this.sec = i;
		labelSec.setText(i+" per second");
	}

}
