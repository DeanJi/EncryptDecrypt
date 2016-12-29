package com;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.*;


public class Decrypt {
    static Object[][] obj = null;

    public static void main(String[] args) {
    	
    	args = new String[3];
		args[0] = "C:\\workspace\\EncryptDecrypt\\src\\com\\des.key";
		args[1] = "C:\\workspace\\EncryptDecrypt\\src\\com\\Common_Constants.properties.enc";
		args[2] = "C:\\workspace\\EncryptDecrypt\\src\\com\\Common_Constants.properties";
    	
        if (args.length != 3) {
            System.out
                    .println("Usage: Decrypt <keyfile> <encrypted file name> <outpur file name>");
            System.exit(-1);
        }
        System.out.println("Decrypting the property file ... ");
        System.out.println("Decrypting the property file 0 ... "+args[0]);
        System.out.println("Decrypting the property file 1 ... "+args[1]);
        System.out.println("Decrypting the property file 2 ... "+args[2]);
        Decrypt d = new Decrypt();
        Properties p = d.doDecrypt(args[0], args[1], args[2]);

        System.out.println("Decrypted the file ... ");
    }

    public class MyResourceBundle extends ListResourceBundle {

        protected Object[][] getContents() {
            return obj;
        }

    }

    /**
     * Decrypts a property file.
     * Assumes the keyFile and the propFile are in the classpath.
     * They must be in the physical file system (not in the jar file) preferably without spaces.
     * @param password
     * @param keyFile
     * @param propFile
     */
    public Properties doDecrypt(String keyFile, String propFile,
            String outputFileName) {
        Properties dbProp = new Properties();
        try {
            
            SecretKeySpec spec = getKeySpec(keyFile);
        	File f1 = new File(propFile);
            FileInputStream in1 = new FileInputStream(f1);
            Cipher cipher = Cipher.getInstance("AES");  
            cipher.init(Cipher.DECRYPT_MODE, spec);  
            Base64 dec = new Base64();
            
            FileChannel fc = in1.getChannel();
            byte[] input = new byte[(int) fc.size()];
            ByteBuffer bb1 = ByteBuffer.wrap(input);
            fc.read(bb1);
            
            byte output1[] = cipher.doFinal(dec.decode(input));
            ByteArrayInputStream bis = new ByteArrayInputStream(output1);
            dbProp.load(bis);
            if (outputFileName != null && outputFileName.trim().length() > 0) {
                writeFile(output1, outputFileName);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbProp;
    }

    public static SecretKeySpec getKeySpec(String keyfile) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = new byte[16];
        File f = new File(keyfile);       
        SecretKeySpec spec = null;
        if (f.exists()) {
          new FileInputStream(f).read(bytes);
        } else {
           System.out.println("Keyfile "+ keyfile + " doesnot exists");
        }
        spec = new SecretKeySpec(bytes,"AES");
        
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
