package model;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

import userInterface.InterfaceEncryptedFilesTransfer;

public class ServerConexion extends Thread {
	
	private InterfaceEncryptedFilesTransfer myInterface;
	
	public ServerConexion(InterfaceEncryptedFilesTransfer i) {
		myInterface = i;
	}
	
	public void communicationProcess() {
		ServerSocket socketServidor;
			try {
				//Creando Socket de Conexion en el Puerto 15200
				socketServidor = new ServerSocket(15200);
				while(true) {
					//Espera y Acepta cuando reciba Conexi�n 
					Socket client = socketServidor.accept();
					System.out.println("Nueva Conexi�n Establecida");
					
					//Conexi�n y Mensaje con IP Remota y Confirmaci�n de Transferencia 
					BufferedWriter OutFromServer = new BufferedWriter( new OutputStreamWriter(client.getOutputStream() ) );
				    String IPremote= client.getInetAddress().toString();
				    String msj = "Desde el Host Remoto con direcci�n IP: " + IPremote + " se esta transfiriendo un archivo. Por favor confirmar autorizaci�n para transferencia";
				    int dialogResult = JOptionPane.showConfirmDialog (null, msj,"Warning",JOptionPane.YES_NO_OPTION);
				    
				    //Da orden de enviar al cliente y ejecutar ClienteTCP
				    if(dialogResult == JOptionPane.YES_OPTION){
				    	OutFromServer.write("ENVIAR\n");
				    	OutFromServer.flush();
				    	System.out.println("ENVIANDO AL CLIENTE");
				    	sleep(3000);
				    	ClientTCP cliente = new ClientTCP(client.getInetAddress(),myInterface);
				    	cliente.run();
				    }
				    //Da orden de cancelar al cliente y cierra canal
				    else {
				    	OutFromServer.write("CANCELADO");
				    	OutFromServer.flush();
				    	OutFromServer.close();
				    }
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showConfirmDialog (null, "Ha ocurrido un error en la conexi�n","Warning",JOptionPane.ERROR_MESSAGE);
			}
	}

	@Override
	public void run() {
		communicationProcess();
	}
}
