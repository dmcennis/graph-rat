/**
 * Aug 12, 2008-5:35:25 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 *
 * @author Daniel McEnnis
 */
public class AssertionHandler extends Handler{

    String expectation = ".*";
    
    boolean errorExpected = false;
    
    boolean triggered = false;
    
    @Override
    public void publish(LogRecord record) {
        if(record != null){
            if(errorExpected){
                if((record.getThrown()==null)){
                    junit.framework.TestCase.fail("Expected error was not logged");
                }else if(Pattern.matches(expectation, record.getThrown().getClass().getName())){
                    triggered = true;
                }else{
                    junit.framework.TestCase.fail("'"+record.getThrown().getClass().getName()+"' does not match expected error '"+expectation+"'");
                }
            }else{
                if(Pattern.matches(expectation, record.getMessage())){
                    triggered = true;
                }else{
                    junit.framework.TestCase.fail("'"+record.getMessage()+"' is not matching '"+expectation+"'");
                }
            }
        }
    }

    @Override
    public void flush() {
        ;
    }

    @Override
    public void close() throws SecurityException {
        ;
    }

    public void setPattern(String p){
        expectation = p;
    }
    
    public void setErrorExpected(boolean e){
        errorExpected = e;
    }
    
    public boolean isTriggered(){
        return triggered;
    }
}
