/*
 * Created 12-6-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import java.io.File;
import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * Class for serializing and deserializing java.io.File objects
 * @author Daniel McEnnis
 */
public class FileFactory implements PropertyValueFactory<File>{

    @Override
    public File importFromString(String data, Graph g) {
        File ret = new File(data);
        return ret;
    }

    @Override
    public String exportToString(File type, Graph g) {
        return type.getAbsolutePath();
    }
    
}
