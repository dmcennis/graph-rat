/*

 * BasicIODescriptor.java

 *

 * Created on 14 September 2007, 13:54

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package org.mcennis.graphrat.descriptors;

import org.dynamicfactory.propertyQuery.Query;



/**

 *


 * 

 * Class for describing the input that a component requires. Implements the 

 * InputDesciptor and IODescriptorInternal interfaces.

 * @author Daniel McEnnis
 */

public class    BasicIODescriptor implements IODescriptorInternal{
    String propertyName;
    IODescriptor.Type type;
    String relation = null;
    Query query = null;
    String algorithmName;
    String description="";
    boolean appendGraphID;

    public boolean appendGraphID() {
        return appendGraphID;
    }

    public void setAppendGraphID(boolean appendGraphID) {
        this.appendGraphID = appendGraphID;
    }

    /** Creates a new instance of BasicIODescriptor */

    public BasicIODescriptor() {

    }

    public void setQuery(Query q) {
        query = q;
    }

    public Query getQuery() {
        return query;
    }



    @Override

    public void setProperty(String s) {

        propertyName = s;

    }



    @Override

    public void setClassType(IODescriptor.Type t) {

        type = t;

    }



    @Override

    public void setRelation(String s) {

        relation = s;

    }



    @Override

    public void setAlgorithmName(String s) {

        algorithmName = s;

    }



    @Override

    public String getAlgorithmName() {

        return algorithmName;

    }



    @Override

    public String getRelation() {

        return relation;

    }



    @Override

    public IODescriptor.Type getClassType() {

        return type;

    }



    @Override

    public String getProperty() {

        return propertyName;

    }

    @Override
    public BasicIODescriptor prototype(){
        return new BasicIODescriptor();
    }

    public void setDescription(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

}

