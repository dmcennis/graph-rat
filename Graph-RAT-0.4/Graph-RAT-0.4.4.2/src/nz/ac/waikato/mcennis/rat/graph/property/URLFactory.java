/*
 * Created 12-5-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * Factory for serializing and deserializing java.net.URL objects
 * @author Daniel McEnnis
 */
public class URLFactory implements PropertyValueFactory<URL>{

    @Override
    public URL importFromString(String data, Graph g) {
        URL ret = null;
        try {
            ret = new URL(data);
        } catch (MalformedURLException ex) {
            Logger.getLogger(URLFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public String exportToString(URL type, Graph g) {
        return type.toString();
    }

}