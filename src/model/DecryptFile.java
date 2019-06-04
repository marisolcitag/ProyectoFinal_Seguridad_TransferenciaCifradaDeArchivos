package model;

import java.io.File;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.Key;

import javax.crypto.Cipher;

public class DecryptFile {
	
	public DecryptFile() {	
	}
	
	/**
	  * Referencia Bibliogr�fica de c�digo adaptador a esta implementaci�n.
	  * http://www.forosdelweb.com/f45/escribir-fichero-array-bytes-recuperarlos-494509/
	  */
	public void writeFile(byte[] data, String fileName) {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File("./archivos_transferidos/" + fileName);			
			int cont = 1;
			while	(file.exists()) {
				file = new File("./archivos_transferidos/" + fileName.replace('.', '/').split("/")[0] + cont + "." + fileName.replace('.', '/').split("/")[1] );
				cont++;
			}
			fop = new FileOutputStream(file);
			file.createNewFile();
			fop.write(data);
			fop.flush();
			fop.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public byte[] decipherFile(byte[] encryptedFile, Key secretKey, byte[] encodedParams) {
		byte[] decryptedFile = null; 
		try {
			//Se generan parametros del algoritmo de descifrado
		     AlgorithmParameters aesParams = AlgorithmParameters.getInstance("AES");
		     //Se pasan parametros de cifrado del servidor
		     aesParams.init(encodedParams);
		     //Inicializar y configurar Cipher
			 Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
			 aesCipher.init(Cipher.DECRYPT_MODE, secretKey, aesParams);
			 //Descifra el archivo
	         decryptedFile = aesCipher.doFinal(encryptedFile);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	return decryptedFile;
	}
}
