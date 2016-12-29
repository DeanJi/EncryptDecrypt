package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class Encrypt {
	public static void main(String[] args) {
		
		args = new String[3];
		args[0] = "C:\\workspace\\EncryptDecrypt\\src\\com\\des.key";
		args[1] = "C:\\workspace\\EncryptDecrypt\\src\\com\\Common_Constants.properties";
		args[2] = "C:\\workspace\\EncryptDecrypt\\src\\com\\Common_Constants.properties.enc";
		
		if (args.length != 3) {
			System.out.println("Usage: encrypt <keyfile> <propfile> <outfile>");
			System.exit(-1);
		}
		System.out.println("Encrypting the property file ... ");
		System.out.println("Encrypting the property file args[0] ... "
				+ args[0]);
		System.out
				.println("Encrypting the property file args[1]... " + args[1]);
		System.out.println("Encrypting the property file args[2] ... "
				+ args[2]);
		new Encrypt().doEncrypt(args[0], args[1], args[2]);
		System.out.println("Encrypted the property file ... ");
	}

	
	private void doEncrypt(String keyFileName, String propFile,
			String outputFile) {
		try {
			
			SecretKeySpec spec = getKeySpec(keyFileName);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, spec);

			Base64 enc = new Base64();

			File f = new File(propFile);
			FileInputStream in = new FileInputStream(f);
			FileChannel fc = in.getChannel();
			byte[] input = new byte[(int) fc.size()]; // fc.size returns the
														// size of the file
														// which backs the
														// channel
			ByteBuffer bb = ByteBuffer.wrap(input);
			fc.read(bb);
			byte encrypted[] = cipher.doFinal(input);

			writeFile(enc.encode(encrypted), outputFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SecretKeySpec getKeySpec(String keyfile) throws IOException,
			NoSuchAlgorithmException {
		byte[] bytes = new byte[16];
		File f = new File(keyfile);
		SecretKeySpec spec = null;
		if (f.exists()) {
			new FileInputStream(f).read(bytes);
		} else {
			System.out.println("Keyfile " + keyfile + " doesnot exists");
		}
		spec = new SecretKeySpec(bytes, "AES");
		return spec;
	}

	public void writeFile(byte[] content, String name) throws IOException,
			FileNotFoundException {
		File f = new File(name);
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(content);
		fos.close();
	}

}
