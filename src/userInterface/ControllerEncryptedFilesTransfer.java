package userInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.InetAddress;
import java.net.DatagramSocket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class ControllerEncryptedFilesTransfer extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1877L;
	private InterfaceEncryptedFilesTransfer myInterface;
	private JPanel panelUp, panelDown, panelLeft;
	private JLabel lbIpLocal, lbIpRemote;
	private JTextField txtIpOctet1, txtIpOctet2, txtIpOctet3, txtIpOctet4;
	private JTextField txtIpRemoteOctet1, txtIpRemoteOctet2, txtIpRemoteOctet3, txtIpRemoteOctet4;
	private JTextField txtFileName;
	private JButton btnChooseFile, btnTransferFile;
	private JList transferList;
	private JFileChooser fc;
	
	public ControllerEncryptedFilesTransfer(InterfaceEncryptedFilesTransfer interfaz){
		
		setInterface(interfaz);
		GridBagConstraints gbc = new GridBagConstraints();	
	    
	    fc = new JFileChooser();
	    
	    //SECCIÓN INFORMATIVA RESPECTO A LA DESCRIPCIÓN DEL PROYECTO
	    panelUp = new JPanel();
		panelUp.setLayout(new GridLayout(5, 1));
		JLabel title = new JLabel("PROYECTO FINAL SEGURIDAD");
		title.setFont(new Font("Arial", 0, 20));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel title2 = new JLabel("TRANSFERENCIA CIFRADA DE ARCHIVOS");
		title2.setFont(new Font("Arial", 0, 20));
		title2.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel title3 = new JLabel("Algoritmo de cifrado: AES");
		title3.setFont(new Font("Arial", 0, 16));
		title3.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel title4 = new JLabel ("Clave 128 Bits");
		title4.setFont(new Font("Arial", 0, 16));
		title4.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel title5 = new JLabel ("Algoritmo Diffie-Hellman.");
		title5.setFont(new Font("Arial", 0, 16));
		title5.setHorizontalAlignment(SwingConstants.CENTER);
		panelUp.add(title);
		panelUp.add(title2);
		panelUp.add(title3);
		panelUp.add(title4);
		panelUp.add(title5);
		myInterface.add(panelUp,BorderLayout.NORTH);

		//SECCIÓN INFORMATIVA IP LOCAL E IP REMOTA	    
		panelLeft = new JPanel();
		panelLeft.setLayout(new GridBagLayout());
		GridBagConstraints a = new GridBagConstraints();
		a.gridwidth = 1;
		a.weightx = .01;
		a.weighty = .2;
		        
		//IP LOCAL	    
		JLabel l1 = new JLabel("");
		JLabel l2 = new JLabel("");
		JLabel l3 = new JLabel("");
		JLabel l4 = new JLabel("");
		JLabel l5 = new JLabel("");
		JLabel l6 = new JLabel("");
		JLabel l7 = new JLabel("");
		JLabel l8 = new JLabel("");
		JLabel l9 = new JLabel("Información Host Local");
		JLabel l10 = new JLabel("");
		JLabel l11 = new JLabel("");
		JLabel l12 = new JLabel("");
		JLabel l13 = new JLabel("");
		JLabel l14 = new JLabel("");
		JLabel l15 = new JLabel("");
		JLabel l16 = new JLabel("");
			    
		lbIpLocal=new JLabel("Ip Local");
			    
		txtIpOctet1 = new JTextField(3);
		txtIpOctet1.setEditable(false);
		txtIpOctet1.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));
				
		JLabel punto1 =  new JLabel(".");
				       
		txtIpOctet2 = new JTextField(3);
		txtIpOctet2.setEditable(false);
		txtIpOctet2.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));
				
		JLabel punto2 =  new JLabel(".");
			
		txtIpOctet3 = new JTextField(3);
		txtIpOctet3.setEditable(false);
		txtIpOctet3.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));
				
		JLabel punto3 =  new JLabel(".");
				
		txtIpOctet4 = new JTextField(3);
		txtIpOctet4.setEditable(false);
		txtIpOctet4.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));
				  
		//IP REMOTA
		lbIpRemote=new JLabel("Ip Remota");
				
		txtIpRemoteOctet1 = new JTextField(3);
		txtIpRemoteOctet1.setEditable(true);
		txtIpRemoteOctet1.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));
				
		JLabel punto4 =  new JLabel(".");
				
		txtIpRemoteOctet2 = new JTextField(3);
		txtIpRemoteOctet2.setEditable(true);
		txtIpRemoteOctet2.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));

		JLabel punto5 =  new JLabel(".");
				
		txtIpRemoteOctet3 = new JTextField(3);
		txtIpRemoteOctet3.setEditable(true);
		txtIpRemoteOctet3.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));

		JLabel punto6 =  new JLabel(".");
				
		txtIpRemoteOctet4 = new JTextField(3);
		txtIpRemoteOctet4.setEditable(true);
		txtIpRemoteOctet4.setBorder(BorderFactory.createLineBorder(Color.decode("#2C6791")));

		JLabel l17 = new JLabel(" Información Host Remoto");
		JLabel l18 = new JLabel("");
		JLabel l19 = new JLabel("");
		JLabel l20 = new JLabel("");
		JLabel l21 = new JLabel("");
		JLabel l22 = new JLabel("");
		JLabel l23 = new JLabel("");
		JLabel l24 = new JLabel("");
		JLabel l25 = new JLabel("");
		JLabel l26 = new JLabel("");
		JLabel l27 = new JLabel("");
		JLabel l28 = new JLabel("");
		JLabel l29 = new JLabel("");
		JLabel l30 = new JLabel("");
		JLabel l31 = new JLabel("");
		JLabel l32 = new JLabel("");
			    
		//ADD PANEL LEFT
		a.gridx = 0;
		a.gridy = 0;
		panelLeft.add(l1,a);
		a.gridx = 1;
		a.gridy = 0;
		panelLeft.add(l2,a);
		a.gridx = 2;
		a.gridy = 0;
		panelLeft.add(l3,a);
		a.gridx = 3;
		a.gridy = 0;
		panelLeft.add(l4,a);
		a.gridx = 4;
		a.gridy = 0;
		panelLeft.add(l5,a);
		a.gridx = 5;
		a.gridy = 0;
		panelLeft.add(l6,a);
		a.gridx = 6;
		a.gridy = 0;
		panelLeft.add(l7,a);
		a.gridx = 7;
		a.gridy = 0;
		panelLeft.add(l8,a);
		a.gridx = 0;
		a.gridy = 1;
		panelLeft.add(l9,a);
		a.gridx = 1;
		a.gridy = 1;
		panelLeft.add(l10,a);
		a.gridx = 2;
		a.gridy = 1;
		panelLeft.add(l11,a);
		a.gridx = 3;
		a.gridy = 1;
		panelLeft.add(l12,a);
		a.gridx = 4;
		a.gridy = 1;
		panelLeft.add(l13,a);
		a.gridx = 5;
		a.gridy = 1;
		panelLeft.add(l14,a);
		a.gridx = 6;
		a.gridy = 1;
		panelLeft.add(l15,a);
		a.gridx = 7;
		a.gridy = 1;
		panelLeft.add(l16,a);
		a.gridx = 0;
		a.gridy = 2;
		panelLeft.add(lbIpLocal,a);
		a.gridx = 1;
		a.gridy = 2;
		panelLeft.add(txtIpOctet1,a);
		a.gridx = 2;
		a.gridy = 2;
		panelLeft.add(punto1,a);
		a.gridx = 3;
		a.gridy = 2;
		panelLeft.add(txtIpOctet2,a);
		a.gridx = 4;
		a.gridy = 2;
		panelLeft.add(punto2,a);
		a.gridx = 5;
		a.gridy = 2;
		panelLeft.add(txtIpOctet3,a);
		a.gridx = 6;
		a.gridy = 2;
		panelLeft.add(punto3,a);
		a.gridx = 7;
		a.gridy = 2;
		panelLeft.add(txtIpOctet4,a);
		a.gridx = 0;
		a.gridy = 3;
		panelLeft.add(l17,a);
		a.gridx = 1;
		a.gridy = 3;
		panelLeft.add(l18,a);
		a.gridx = 2;
		a.gridy = 3;
		panelLeft.add(l19,a);
		a.gridx = 3;
		a.gridy = 3;
		panelLeft.add(l20,a);
		a.gridx = 4;
		a.gridy = 3;
		panelLeft.add(l21,a);
		a.gridx = 5;
		a.gridy = 3;
		panelLeft.add(l22,a);
		a.gridx = 6;
		a.gridy = 3;
		panelLeft.add(l23,a);
		a.gridx = 7;
		a.gridy = 3;
		panelLeft.add(l24,a);
		a.gridx = 0;
		a.gridy = 4;
		panelLeft.add(lbIpRemote,a);
		a.gridx = 1;
		a.gridy = 4;
		panelLeft.add(txtIpRemoteOctet1,a);
		a.gridx = 2;
		a.gridy = 4;
		panelLeft.add(punto4,a);
		a.gridx = 3;
		a.gridy = 4;
		panelLeft.add(txtIpRemoteOctet2,a);
		a.gridx = 4;
		a.gridy = 4;
		panelLeft.add(punto5,a);
		a.gridx = 5;
		a.gridy = 4;
		panelLeft.add(txtIpRemoteOctet3,a);
		a.gridx = 6;
		a.gridy = 4;
		panelLeft.add(punto6,a);
		a.gridx = 7;
		a.gridy = 4;
		panelLeft.add(txtIpRemoteOctet4,a);
		a.gridx = 0;
		a.gridy = 5;
		panelLeft.add(l25,a);
		a.gridx = 1;
		a.gridy = 5;
		panelLeft.add(l26,a);
		a.gridx = 2;
		a.gridy = 5;
		panelLeft.add(l27,a);
		a.gridx = 3;
		a.gridy = 5;
		panelLeft.add(l28,a);
		a.gridx = 4;
		a.gridy = 5;
		panelLeft.add(l29,a);
		a.gridx = 5;
		a.gridy = 5;
		panelLeft.add(l30,a);
		a.gridx = 6;
		a.gridy = 5;
		panelLeft.add(l31,a);
		a.gridx = 5;
		a.gridy = 5;
		panelLeft.add(l32,a);
		myInterface.add(panelLeft,BorderLayout.WEST);
		
		//OBTENER IP HOST LOCAL.
		/**
		  * Referencia Bibliográfica de código implementado.
		  * https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
		  */
	    String ipLoc = "";
	    try {
	    	 DatagramSocket socket = new DatagramSocket();
	    	 socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
	    	 ipLoc = socket.getLocalAddress().getHostAddress();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	    txtIpOctet1.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctet1.getText().length()== 3)
	    	 	     e.consume();
	    		if (txtIpOctet1.getText().length() == 2)
	    			 txtIpOctet2.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	  
	    txtIpOctet2.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctet2.getText().length() == 3)
	    	 	     e.consume();
	    		if (txtIpOctet2.getText().length() == 2)
	    			 txtIpOctet3.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	   
	    txtIpOctet3.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctet3.getText().length()== 3)
	    	 	     e.consume();
	    		if (txtIpOctet3.getText().length() == 2)
	    			 txtIpOctet4.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	  
	    txtIpOctet4.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctet4.getText().length()== 3)
	    	 	     e.consume();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
		
	    txtIpOctet1.setText(ipLoc.replace('.', 'f').split("f")[0]+"");
	    txtIpOctet2.setText(ipLoc.replace('.', 'f').split("f")[1]+"");
	    txtIpOctet3.setText(ipLoc.replace('.', 'f').split("f")[2]+"");
	    txtIpOctet4.setText(ipLoc.replace('.', 'f').split("f")[3]+"");
	    //FIN IP HOST LOCAL
	    
	    //OBTENER IP REMOTA
	    txtIpRemoteOctet1.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpRemoteOctet1.getText().length()== 3) {
	    			e.consume();
	    		}    
	    		if (txtIpRemoteOctet1.getText().length() == 2)
	    			 txtIpRemoteOctet2.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    
	    txtIpRemoteOctet2.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpRemoteOctet2.getText().length() == 3)
	    	 	     e.consume();
	    		if (txtIpRemoteOctet2.getText().length() == 2)
	    			 txtIpRemoteOctet3.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    
	    txtIpRemoteOctet3.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpRemoteOctet3.getText().length()== 3)
	    	 	     e.consume();
	    		if (txtIpRemoteOctet3.getText().length() == 2)
	    			 txtIpRemoteOctet4.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    
	    txtIpRemoteOctet4.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpRemoteOctet4.getText().length()== 3)
	    	 	     e.consume();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    //FIN OBTENER IP REMOTA
	    	        
	    //PANEL DOWN
	    panelDown = new JPanel();
	    panelDown.setLayout(new FlowLayout());
	    
	    JLabel lbChoose = new JLabel("Seleccionar Archivo a Transferir");
	    
	    btnChooseFile = new JButton(new ImageIcon("./data/chooseFile.png"));
	    btnChooseFile.setOpaque(true);
	    btnChooseFile.setBorder(null);
	    btnChooseFile.setToolTipText("Elegir archivo para enviar");
	    btnChooseFile.addActionListener(this);
	    btnChooseFile.setActionCommand("choose");
	    	    	    
	    txtFileName = new JTextField(10);
	    txtFileName.setEditable(false);
	    	    
	    panelDown.add(lbChoose);
	    panelDown.add(btnChooseFile);
	    panelDown.add(txtFileName);   
	    myInterface.add(panelDown,BorderLayout.SOUTH);
	    	  	    
	  	//SECCIÓN TRANSFERENCIA DE ARCHIVOS
	    JLabel labelImage = new JLabel(new ImageIcon("./data/transferBanner.gif"));
	       
	    //BOTÓN TRANSFERENCIA DE ARCHIVO
	    btnTransferFile = new JButton(new ImageIcon("./data/sendFile.png"));
	    btnTransferFile.setOpaque(true);
	    btnTransferFile.setBorder(null);
	    btnTransferFile.addActionListener(this);
	    btnTransferFile.setActionCommand("transfer");
	    gbc.gridwidth = 1;
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    add(labelImage,gbc);
	    gbc.gridwidth = 1;
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    add(btnTransferFile, gbc);
	    
	  //SECCIÓN INFORMATIVA DE INTERCAMBIO DE ARCHIVOS
	    transferList = new JList();
	    transferList.getBorder();
	    transferList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );	
	    JScrollPane scroll = new JScrollPane(transferList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(220, 100));	
		scroll.setViewportView(transferList);
	    gbc.gridwidth = 11;
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    add(scroll, gbc);
	    //FIN LISTA DE TRANSFERENCIA DE ARCHIVOS 
	    //FIN SECCIÓN TRANSFERENCIA DE ARCHIVO   
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		String p = e.getActionCommand();
		if(p.equals("choose")){
			 int dialog = fc.showOpenDialog(this);
	         if (dialog == JFileChooser.APPROVE_OPTION) {
	              File file = fc.getSelectedFile();
	              if(file!=null) {
	              myInterface.chooseFile(file, file.getName());
	              txtFileName.setText(file.getName());
	              }
	         }
		}else if(p.equals("transfer")) {
			String ip = txtIpRemoteOctet1.getText() + "." + txtIpRemoteOctet2.getText() + "." + txtIpRemoteOctet3.getText() + "." + txtIpRemoteOctet4.getText()+"";
			myInterface.initializeTransfer(ip);
		}
	}

	public JList getTransferList() {
		return transferList;
	}
	
	public InterfaceEncryptedFilesTransfer getInterface() {
		return myInterface;
	}
		 
	public void setInterface(InterfaceEncryptedFilesTransfer i) {
		this.myInterface = i;
	}
}
