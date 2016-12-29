package com;

import java.io.File;
import java.io.FileOutputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGen {
	public static void main(String[] args) {

		genkey();
		System.out.println("Key is generated ");

	}

	private static void genkey() {
		try {

			byte[] bytes = new byte[16];
			File f = new File(
					"C:\\workspace\\EncryptDecrypt\\src\\com\\des.key");
			SecretKey key = null;
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256);
			key = kgen.generateKey();
			bytes = key.getEncoded();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
