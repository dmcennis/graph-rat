/* * BasicParameter.java * * Created on 14 September 2007, 13:55 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.descriptors;/** * *  * Base implementation of all the parameter interfaces. * @author Daniel McEnnis */public class BasicParameter implements ParameterInternal{        private String name;        private Class classType;        private boolean structural;        private Object value;        /** Creates a new instance of BasicParameter */    public BasicParameter() {    }    @Override    public void setName(String name) {        this.name = name;    }    @Override    public void setStructural(boolean b) {        structural = b;    }    @Override    public String getName() {        return name;    }    @Override    public Class getType() {        return classType;    }    @Override    public Object getValue() {        return value;    }    @Override    public boolean isStructural() {        return structural;    }    @Override    public void setValue(Object o) {        value = o;    }    @Override    public void setType(Class c) {        classType = c;    }    }