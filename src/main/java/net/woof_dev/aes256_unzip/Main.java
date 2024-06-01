package net.woof_dev.aes256_unzip;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.out.println("set filePath and password.");
      return;
    }

    String filename = args[0];
    String password = args[1];
    
    Unzipper unzipper = new Unzipper();
    String[] fileArray = unzipper.extract(filename, password);

    System.out.println("results");      
    for(String file : fileArray) {
        System.out.println(file);    	
    }
  }
}
