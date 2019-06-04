package userInterface;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.ClientConexion;
import model.ServerConexion;

public class InterfaceEncryptedFilesTransfer extends JFrame {
	
	private static final long serialVersionUID = 10L;
	private ControllerEncryptedFilesTransfer controllerEncryptedFilesTransfer;
	private String name;
	private File file;
	private ArrayList<String> transferFiles;
	
	public InterfaceEncryptedFilesTransfer() {
		transferFiles = new ArrayList<String>();
		transferFiles.add("Archivos Transferidos:");
		file = null;
		name = "";
		setTitle("Transferencia Cifrada de Archivos");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 500);
		setLocationRelativeTo(null);
		
		controllerEncryptedFilesTransfer = new ControllerEncryptedFilesTransfer(this);
		controllerEncryptedFilesTransfer.setVisible(true);
		add(controllerEncryptedFilesTransfer, BorderLayout.CENTER);
	
		ServerConexion serverConexion = new ServerConexion(this);
		serverConexion.start();
	}
	
	public void initializeTransfer(String ipRemote) {
		if(file!=null) {
			System.out.println("Ip Remota " +ipRemote);
			ClientConexion conexionCliente = new ClientConexion(file, ipRemote, name, this);
			conexionCliente.start();
		}
		else {
      	  JOptionPane.showMessageDialog(null,
					"Por Favor Seleccione un Archivo para Iniciar la Transferencia", "ERROR ARCHIVO NO SELECCIONADO", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	public void chooseFile(File f, String n) {
		file = f;
		name = n;
	}
		
	public void updateListTransfer(String data) {
		transferFiles.add("-"+data);
		controllerEncryptedFilesTransfer.getTransferList().setListData(transferFiles.toArray());
	}
	
	public static void main(String[] args) {
		InterfaceEncryptedFilesTransfer window = new InterfaceEncryptedFilesTransfer();
		window.setVisible(true);
	}
	
}
