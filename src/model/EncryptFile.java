package model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class EncryptFile {
	
	 private byte[] params;
	
	public EncryptFile(){
		params = null;
	}
	
	public byte[] loadFile(File path) {
		 byte[] fileInBytes = null;
		    FileInputStream theFIS = null;
		    BufferedInputStream theBIS = null;
		    byte[] buffer = new byte[1024];
		    int read = 0;
		    ByteArrayOutputStream theBOS = new ByteArrayOutputStream();

		    try{
		      theFIS = new FileInputStream(path);
		      theBIS = new BufferedInputStream(theFIS);
		      while ((read = theBIS.read(buffer)) >= 0){
		        theBOS.write(buffer, 0, read);
		      }
		      fileInBytes = theBOS.toByteArray();
		      theBOS.reset();
		      theBOS.close();
		    }
		    catch (IOException e1) {
		      e1.printStackTrace();
		    }
		    finally {
		      if (theBIS != null) {
		        try {
		          theBIS.close();
		        }
		        catch (IOException e) {
		          e.printStackTrace();
		        }
		      }
		    }
		    return fileInBytes;
	}
	public byte[] getParams() {
		return params;
	}
	public byte[] cipherFile(byte[] file, Key secretKey) {		
		byte[] encryptedFile = null;
		try {	
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
		aesCipher.init(Cipher.ENCRYPT_MODE, secretKey); 
		encryptedFile = aesCipher.doFinal(file);
		params = aesCipher.getParameters().getEncoded();
		}catch (NoSuchPaddingException e) { 
			System.err.println("Padding Problem: " + e); 
		} catch (NoSuchAlgorithmException e) { 
		System.err.println("Invalid Algorithm: " + e); 
		} catch (InvalidKeyException e) { 
		System.err.println("Invalid Key: " + e); 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return encryptedFile;
	}
}
