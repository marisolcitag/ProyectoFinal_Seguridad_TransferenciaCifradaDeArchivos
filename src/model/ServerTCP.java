package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import userInterface.InterfaceEncryptedFilesTransfer;

public class ServerTCP extends Thread{
	private byte[] fileWithoutCipher;
	private String nameFile;
	private InterfaceEncryptedFilesTransfer myInterface;
	
	public ServerTCP(byte[] f, String n, InterfaceEncryptedFilesTransfer i) {
		fileWithoutCipher = f;
		nameFile = n;
		myInterface = i;
	}

	public void transferFilesProcess() {
		try {
			int bitLength = 1024;
		    BigInteger p;
		    BigInteger g;
		    String MD5 = "";
		    EncryptFile encrypt = new EncryptFile();
		    
		    //VARIABLE PARA MANEJAR MENSAJES DEL CLIENTE
			String entry;
			
			//SOCKET DEJA EN ESPERA AL SERVIDOR
			ServerSocket socketServidor = new ServerSocket(15210);
			System.out.println("Socket Servidor Iniciado");
			
			//ESPERA QUE SE CONECTE EL CLIENTE
			Socket client = socketServidor.accept();	
			System.out.println("Conexión establecida con el Cliente para Transferencia");
			
			//CANALES PARA INTERCAMBIAR INFORMACION
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
	        	        
	        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        BufferedWriter OutFromServer = new BufferedWriter( new OutputStreamWriter(client.getOutputStream() ) );
	        
	        OutputStream out = client.getOutputStream();
	        DataOutputStream dataOut = new DataOutputStream(out);
	        
	        //GENERACION DE CLAVE COMPARTIDA POR MEDIO DEL ALGORITMO DIFFIE HELLMAN
	        OutFromServer.write("GENERAR DH\n");
	        OutFromServer.flush();
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " + entry);
	        SecureRandom rnd = new SecureRandom();
	        g= BigInteger.probablePrime(bitLength, rnd);
	        if(entry.equals("LISTO PARA GENERAR DH")) {
	        	OutFromServer.write(g.toString()+"\n");
	        	System.out.println("Parametro G Generado y Enviado desde el Servidor " + " g = " + g + "");
	        	OutFromServer.flush();
	        }
	        //Lectura del Parametro P del Cliente
	        entry = inFromClient.readLine();
	        System.out.println("Recibiendo desde el Cliente el Parametro P: " +entry);
	        p = new BigInteger(entry);
	        
	        //Se usan los parametros G y P para generar la clave
	        DHParameterSpec dhParams = new  DHParameterSpec(g, p); 
	        
	        //CREA LOS PARAMETROS PARA LA CREACION DE LA CLAVE
	        System.out.println("Parametros Diffie Hellman Generados");
	        KeyPairGenerator serverKeyGen = KeyPairGenerator.getInstance("DH");
	        
	        //DECLARAR EL GENERADOR DE CLAVES EN MODO DH - Diffie Hellman
	        serverKeyGen.initialize(dhParams, new SecureRandom()); //
	        KeyAgreement serverKeyAgree = KeyAgreement.getInstance("DH");
	        KeyPair serverPair = serverKeyGen.generateKeyPair();
	        	        
	        //RECIBIDA CLAVE PUBLICA DEL CLIENTE
	        Key clientePublicKey = (Key) ois.readObject(); 
	        System.out.println("Recibida Clave Publica del Cliente: " + clientePublicKey.toString());
	        
	        //ENVIADA CLAVE PUBLICA DEL SERVIDOR
	        oos.writeObject(serverPair.getPublic());
	        oos.flush(); 
	        System.out.println("Enviada Clave Publica del Servidor: " + serverPair.getPublic().toString());
	        
	        serverKeyAgree.init(serverPair.getPrivate());
	        serverKeyAgree.doPhase(clientePublicKey, true);
	        
	        byte[] serverSharedSecret = serverKeyAgree.generateSecret();
	        SecretKeySpec claveServer = new SecretKeySpec(serverSharedSecret, 0, 16, "AES");
	        System.out.println("Clave Secreta: "+ claveServer.toString());
	        
	        //INICIO DE TRANSFERENCIA
	        OutFromServer.write("INICIO\n");
	        OutFromServer.flush();
	        System.out.println("Enviando INICIO para Comenzar la Transferencia de Archivo");
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " +  entry);
	        if(entry.equalsIgnoreCase("LISTO PARA INICIO")) {
	        	
	        	//CALCULA HASH MD5 ANTES DE CIFRAR EL ARCHIVO
	        	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        	messageDigest.reset();
	        	messageDigest.update(fileWithoutCipher);
	        	byte[] resultByte = messageDigest.digest();
	        	MD5 = bytesToHex(resultByte);
	        	System.out.println("MD5: " + MD5);
	        }
	        //CIFRA EL ARCHIVO Y LO ENVIA AL CLIENTE
	        byte[] cipherFile = encrypt.cipherFile(fileWithoutCipher, claveServer);
	        OutFromServer.write(""+ 0 + "," + cipherFile.length+"\n");
	        OutFromServer.flush();
	        System.out.println("Enviando Información de Control desde el Servidor al Cliente: " + ""+ 0 + "," + cipherFile.length);
	        entry = inFromClient.readLine();
	        System.out.println("From Client:" + entry);
	        if(entry.equalsIgnoreCase("OK")) {
	        	dataOut.write(cipherFile);
	        	dataOut.flush();
	        	System.out.println("Archivo Transferido Correctamente");
	        }
	        
	        //ESPERA CONFIRMACION DEL CLIENTE Y ENVIA EL HASH MD5 CALCULADO PREVIAMENTE
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " + entry);
	        byte[] params = encrypt.getParams(); 
	        OutFromServer.write(""+ 0 + "," + params.length+"\n");
	        OutFromServer.flush();
	        System.out.println("Out To Client: " + ""+ 0 + "," + params.length);
	        dataOut.write(params);
        	dataOut.flush();
	        if(entry.equalsIgnoreCase("RECIBIDO")) {
	        	OutFromServer.write(MD5+"\n");
	        	OutFromServer.flush();
	        }
	        
	        OutFromServer.write(nameFile+"\n");
        	OutFromServer.flush();
	        //ESPERA ESTADO DE LA TRANSFERENCIA
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " + entry);
	        if(entry.equalsIgnoreCase("TRANSFERENCIA CORRECTA")) {
	        	
	        }else {
	        } 
	        System.out.println("Se Cierra el Socket");
	        myInterface.updateListTransfer(nameFile);
	        socketServidor.close();
		} catch (Exception e) {
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
