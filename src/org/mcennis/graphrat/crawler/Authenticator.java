/**

 * copyright Daniel McEnnis published under Aferro GPL (see license.txt)

 */

/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
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

		return new PasswordAuthentication(user,password.toCharArray());

	}

        

        

}

