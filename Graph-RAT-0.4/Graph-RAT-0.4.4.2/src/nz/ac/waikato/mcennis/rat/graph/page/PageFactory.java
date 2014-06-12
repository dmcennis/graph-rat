/*
 * PageFactory.java
 *
 * Created on 1 May 2007, 17:02
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.page;

import java.util.Properties;

/**
 * Class for generating Page objects
 *
 * @author Daniel McEnnis
 * 
 */
public class PageFactory {
    
    static PageFactory instance = null;
    
    /**
     * Create a reference ot a static PageFactory object
     * 
     * @return reference to singelton factory.
     */
    public static PageFactory newInstance(){
        if(instance == null){
            instance = new PageFactory();
        }
        return instance;
    }
    /** Creates a new instance of PageFactory */
    protected PageFactory() {
    }
    
    /**
     * Creates a new Page object.
     * 
     * Only BasicPage is supported.
     * 'PageID' sets the id of the page.
     * 
     * 
     * @param props property map used to create and initialize this object
     * @return newly created page object
     */
    public Page create(Properties props){
        return new BasicCartesianPage(props.getProperty("PageID"));
    }
}
