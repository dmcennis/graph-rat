/*
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

/**
 * Error thrown when a new value is added to a property that is not assignable
 * to the class of the property.
 * 
 * @author Daniel McEnnis
 */
public class InvalidObjectTypeException extends Exception {

    String message;
    
    /**
     * Define the exception
     * @param expected string of the class the property objects accept
     * @param recieved string of the class the property objects recieved as a new value
     */
    public InvalidObjectTypeException(String expected, String recieved){
        super();
        message = "Expected class "+expected +" but found class "+recieved;
    }
    @Override
    public String getMessage() {
        return message;
    }
    
}
