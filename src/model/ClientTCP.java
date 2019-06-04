package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import model.DecryptFile;
import userInterface.InterfaceEncryptedFilesTransfer;

public class ClientTCP extends Thread{
	
	//IP Servidor Remoto
	private InetAddress IPServer;
	private InterfaceEncryptedFilesTransfer myInterface;
	
	public ClientTCP(InetAddress IPserver, InterfaceEncryptedFilesTransfer s) {
		//Se Inicializa IP del Servidor
		IPServer = IPserver;
		myInterface = s;
	}
	
	public void transferFilesProcess() {
		try {
			//Se declaran las variables de las llaves 
		    BigInteger p;
		    BigInteger g;
		    
		    //Se declara la clase para desencriptar
		    DecryptFile descifrar = new DecryptFile();
		    
		    //Se declaran las variables de lectura, almacenamiento y control
			String parametro;
			byte[] lectura = null;
			byte[] params = null;
			byte[] archivoDescifrado = null;
			boolean correcto = false;
			
			//Se Abre el Puerto al Servidor para Transmitir
			System.out.println("Abriendo Puerto desde el Cliente al Servidor para Transmitir");
	        Socket socketCliente = new Socket(IPServer, 15210);
	        
	        //Crea los flujos de objetos
	        ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
	        
	        //Crea los flujos de escritura y lectura
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        BufferedWriter OutFromClient = new BufferedWriter( new OutputStreamWriter(socketCliente.getOutputStream()));
	        
	        //Crea el flujo de lectura de datos
	        InputStream in = socketCliente.getInputStream();
	        DataInputStream dataIn = new DataInputStream(in);
	       
	        //Lectura de la orden de inicio del servidor
	        parametro = inFromServer.readLine();
	        System.out.println("Mensaje del Servidor: "+parametro);
	        if(parametro.equalsIgnoreCase("GENERAR DH")) {
	        	
	        	//Mensaje de confirmación al servidor
	        	OutFromClient.write("LISTO PARA GENERAR DH\n");
	        	OutFromClient.flush();
	        }	
	        
	        //Lectura del Parametro G del Servidor
	        parametro = inFromServer.readLine();
	        System.out.println("Recibiendo desde el Servidor el Parametro G: "+parametro);
	        g = new BigInteger(parametro);
	        
	        //Se genera el Parametro P del Cliente
	        SecureRandom secureP = new SecureRandom();
	        p = BigInteger.probablePrime(1024, secureP);
	        
	        //Transmision del Parametro P
	        OutFromClient.write(p.toString()+"\n"); 
	        System.out.println("Parametro P Generado y Enviado desde el Cliente " + " p = " + p + "");
	        OutFromClient.flush();
	        
	        //Se usan los parametros G y P para generar la clave
	        DHParameterSpec dhParams = new  DHParameterSpec(g, p); 
	        
	        //CREA LOS PARAMETROS PARA LA CREACION DE LA CLAVE
	        KeyPairGenerator clienteKeyGen = KeyPairGenerator.getInstance("DH");
	        
	        //DECLARAR EL GENERADOR DE CLAVES EN MODO DH - Diffie Hellman
	        clienteKeyGen.initialize(dhParams, new SecureRandom());
	        KeyPair clientePair = clienteKeyGen.generateKeyPair();
	        
	        //Se obtiene la clave publica y se transmite al servidor
	        oos.writeObject(clientePair.getPublic()); 
	        
	        //Se obtiene la clave publica del servidor
	        Key serverPublicKey = (Key) ois.readObject(); 
	        System.out.println(serverPublicKey.toString());
	        
	        //Se crea el KeyAgreement para comprobar las llaves
	        KeyAgreement clienteKeyAgree = KeyAgreement.getInstance("DH");
	        
	        //Clave Privada del Cliente
	        clienteKeyAgree.init(clientePair.getPrivate());
	        
	        //Clave Publica del Servidor
	        clienteKeyAgree.doPhase(serverPublicKey, true);
	        
	        //Genera la Clave Secreta
	        byte[] clienteSharedSecret =clienteKeyAgree.generateSecret();	        
	        SecretKeySpec claveCliente = new SecretKeySpec(clienteSharedSecret,0,16, "AES");
	        
	        //Lectura de Inicio de Transmision del Servidor
	        parametro = inFromServer.readLine();
	        if(parametro.equalsIgnoreCase("INICIO")) {
	        	
	        	//Confirmacion Inicio de Transmision al Servidor
	        	OutFromClient.write("LISTO PARA INICIO\n");
	        	OutFromClient.flush();
	        	System.out.println("Inicio de Transmisión al Servidor");
	        }
	        
	        //Espera los parametros para iniciar la lectura del archivo
	        parametro = inFromServer.readLine();
	        System.out.println("Recibiendo Información de Control desde el Servidor: "+parametro);
	        //Se inicializan los parametros de tamano y byte de inicio del archivo
	        int inicio = Integer.parseInt(parametro.split(",")[0]);
	        int tam = Integer.parseInt(parametro.split(",")[1]);
	        if(tam > 0) {
	        	//Mensaje de confirmacion de inicio de la transmision
		        OutFromClient.write("OK\n");
		        OutFromClient.flush();
		        
		        //Lectura del canal 
		        lectura = new byte[tam];
		        dataIn.readFully(lectura, inicio, tam);
		        
		        //Mensaje de confirmacion de recepcion de la transmision del archivo
		        OutFromClient.write("RECIBIDO\n");
		        OutFromClient.flush();
		        System.out.println("Transmision: ARCHIVO RECIBIDO");
	        }
	        
	        //Lectura de inicio y tamano del parametro de desencriptacion
	        parametro = inFromServer.readLine();	        
	        inicio = Integer.parseInt(parametro.split(",")[0]);
	        tam = Integer.parseInt(parametro.split(",")[1]);
	       if(tam > 0) {
	    	   //Lectura del parametro de desencriptacion
	    	   params = new byte[tam];
	    	   dataIn.readFully(params, inicio, tam);
	    	   System.out.println("Recibi "+parametro);
	       }
	        
	        //Comprobacion archivo no vacio
	        if(lectura != null) {
	        	//Lectura del MD5 transmitido desde el servidor
	        	parametro = inFromServer.readLine(); 
	        	System.out.println("Recibi: "+parametro);
	        	
	        	//Calcula el hash del MD5 del servidor
	        	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        	messageDigest.reset();	        	
	        	
	        	//Descifra el archivo y lo pasa al messageDigest
	        	archivoDescifrado = descifrar.decipherFile(lectura, claveCliente, params);
	        	messageDigest.update(archivoDescifrado);
	        	
	        	//Se calcula el MD5 en bytes
	        	byte[] resultByte = messageDigest.digest();
	        	
	        	//Se pasa de bytes a HEX
	        	String MD5 = bytesToHex(resultByte);
	        	//Se compara MD5 del archivo descifrado con el transmitido por el servidor
	        	if(parametro.equalsIgnoreCase(MD5))
	        		correcto = true;
	        }
	        
	        //Lectura nombre del archivo
	        parametro = inFromServer.readLine();
	        
	        //Mensaje de confirmacion de la transmision
	        if(correcto) {
	        	OutFromClient.write("TRANSFERENCIA CORRECTA\n");
	        	
	        	//Se guarda el archivo en la carpeta /Transferencia del proyecto
	        	descifrar.writeFile(archivoDescifrado, parametro);
	        	myInterface.updateListTransfer(parametro);
	        	System.out.println("El Cliente recibió la transferencia correctamente");
	        }
	        //Mensaje de informe de error de la transmision
	        else {
	        	OutFromClient.write("TRANSFERENCIA INCORRECTA\n");
	        	System.out.println("El Cliente recibió la transferencia incorrectamente");
	        }
	        //Se cierran los canales y el socket
	        OutFromClient.flush();
	        oos.close();
	        OutFromClient.close();
	        socketCliente.close();
	        System.out.println("Conexion Cerrada");
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public static String bytesToHex(byte[] data) {
	      if (data==null) {
	         return null;
	      } else {
	         int len = data.length;
	         String str = "";
	         for (int i=0; i<len; i++) {
	            if ((data[i]&0xFF)<16) str = str + "0" 
	               + java.lang.Integer.toHexString(data[i]&0xFF);
	            else str = str
	               + java.lang.Integer.toHexString(data[i]&0xFF);
	         }
	         return str.toUpperCase();
	      }
	   }
	
	@Override
	public void run() {
		transferFilesProcess();
	}
}
