package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

import userInterface.InterfaceEncryptedFilesTransfer;

public class ClientConexion extends Thread {
	private String ip;
	private byte[] file; 
	private String name;
	private InterfaceEncryptedFilesTransfer myInterface;
 	
	public ClientConexion(File f, String iP, String n, InterfaceEncryptedFilesTransfer i) {
		EncryptFile cargarArchivo = new EncryptFile();
		file = cargarArchivo.loadFile(f);
		ip = iP;
		name= n;
		myInterface = i;
	}
	
	public void initializeCommunication() {
		try {
			Socket socketCliente = new Socket(ip, 15200);
			System.out.println("Socket Cliente Iniciado ");
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        String txt = inFromServer.readLine();
	        System.out.println(txt);
	        if(txt.equalsIgnoreCase("ENVIAR")) {
	        	ServerTCP server = new ServerTCP(file, name, myInterface);
	        	System.out.println("Iniciando Servidor");
	        	server.run();
	        }else {
				JOptionPane.showMessageDialog(null,
						"El usuario canceló la transferencia o se perdió la conexión","Finalizado", JOptionPane.INFORMATION_MESSAGE);
	        }
	        socketCliente.close();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null,"No se puede establecer conexión con el host remoto","Finalizado", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void run() {
		initializeCommunication();
	}
}
