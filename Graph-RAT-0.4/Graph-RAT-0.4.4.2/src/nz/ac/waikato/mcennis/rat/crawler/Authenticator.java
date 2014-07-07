/**
 * copyright Daniel McEnnis published under Aferro GPL (see license.txt)
 */

package org.mcennis.graphrat.crawler;

import java.net.PasswordAuthentication;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authenticator used to go through HTTP proxies.
 * @author Daniel McEnnis
 * 
 */
public class Authenticator extends java.net.Authenticator {

        static String password = "";
        
        static String user = "dm75";
        
        /**
         * Acquires a password for this object through System.in
         */
        public static void getPassword(){
		try {
                        Logger.getLogger(Authenticator.class.getName()).log(Level.INFO,"Enter proxy password");
			java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
			password = reader.readLine();
                        reader.close();
		} catch (java.io.IOException e) {
			password = "";
			e.printStackTrace();
		}
        }

        /**
         * Set the password to be the given string
         * 
         * @param p password to be set
         */
        public static void setPassword(String p){
            password = p;
        }
        
        /**
         * Set the username for the proxy to the given string
         * 
         * @param p username of proxy
         */
        public static void setUser(String p){
            user = p;
        }
        
        /**
         * protected method that creates the authenticator used by java.net objects
         * 
         * @return Password Authenticator with static username and password.
         */
	protected PasswordAuthentication getPasswordAuthentication(){
		// TODO Auto-generated method stub
		return new java.net.PasswordAuthentication(user,password.toCharArray());
	}
        
        
}
