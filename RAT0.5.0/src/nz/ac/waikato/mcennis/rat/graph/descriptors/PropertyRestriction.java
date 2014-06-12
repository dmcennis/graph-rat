/**
 * PropertyRestrictionComponent
 * Created Jan 24, 2009 - 8:34:36 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.descriptors;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.NullPropertyQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyRestriction implements SyntaxObject {

    int minCount = 0;
    int maxCount = Integer.MAX_VALUE;
    PropertyQuery test;
    Class classType = Object.class;

    public PropertyRestriction() {
        test = new NullPropertyQuery();
        ((NullPropertyQuery) test).buildQuery(true);
    }

    public void setPropertyQuery(PropertyQuery test) {
        if (test != null) {
            this.test = test;
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null Query provided, leaving the original unchanged.");
        }
    }

    public boolean check(Property type, Object o) {
        Property temp = type.duplicate();
        try {
            temp.add(o);
            return check(temp);
        } catch (InvalidObjectTypeException ex) {
            return false;
        }
    }

    public boolean check(Property type) {
        if (type != null) {
            if ((type.getValue().size()) < minCount) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Min count is " + minCount + " but total count with addition is " + Integer.toString(type.getValue().size()));
                return false;
            } else if ((type.getValue().size()) > maxCount) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Min count is " + maxCount + " but total count with addition is " + Integer.toString(type.getValue().size()));
                return false;
            } else if (!classType.isAssignableFrom(type.getClass())) {
                return false;
            } else if (!test.execute(type)) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Property failed the query check");
                return false;
            } else {
                return true;
            }
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null Query provided, leaving the original unchanged.");
            return false;
        }
    }

    public boolean check(Parameter type) {
        if (type != null) {
            if ((type.getValue().size()) < minCount) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Min count is " + minCount + " but total count with addition is " + Integer.toString(type.getValue().size()));
                return false;
            } else if ((type.getValue().size()) > maxCount) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Min count is " + maxCount + " but total count with addition is " + Integer.toString(type.getValue().size()));
                return false;
            } else if (classType.isAssignableFrom(type.getClass())) {
                return true;
            }
            Property props = PropertyFactory.newInstance().create(type.getType(), type.getParameterClass());
            Iterator it = type.getValue().iterator();
            while (it.hasNext()) {
                try {
                    props.add(it.next());
                } catch (InvalidObjectTypeException ex) {
                }
            }
            if (!test.execute(props)) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Property failed the query check");
                return false;
            } else {
                return false;
            }
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null Query provided, leaving the original unchanged.");
            return false;
        }
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public PropertyQuery getTest() {
        return test;
    }

    public void setTest(PropertyQuery test) {
        if (test != null) {
            this.test = test;
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null value provided for query - change aborted");
        }
    }

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public PropertyRestriction duplicate() {
        PropertyRestriction ret = new PropertyRestriction();
        ret.minCount = this.minCount;
        ret.maxCount = this.maxCount;
        ret.classType = this.classType;
        ret.test = this.test;
        return ret;
    }

    public void setRestriction(String type, PropertyRestriction restriction) {
        this.classType = restriction.classType;
        this.maxCount = restriction.maxCount;
        this.minCount = restriction.minCount;
        this.test = restriction.test;
    }

    public void setDefaultRestriction(PropertyRestriction restriction) {
        ;// deliberate no-op

    }

    public boolean check(Property property, List value) {
        Property temp = property.duplicate();
        try {
            Iterator it = value.iterator();
            while (it.hasNext()) {
                temp.add(it.next());
            }
            return check(temp);
        } catch (InvalidObjectTypeException ex) {
            return false;
        }
    }

    public boolean check(String type, Object value) {
        if (value != null) {
            Property props = PropertyFactory.newInstance().create(type, value.getClass());
            try {
                props.add(value);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(PropertyRestriction.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            return check(props);
        } else {
            Property props = PropertyFactory.newInstance().create(type, classType);
            return check(props);
        }
    }

    public boolean check(String type, List value) {
        if (value != null) {
            if (value.size() > 0) {
                Property props = PropertyFactory.newInstance().create(type, value.getClass());
                try {
                    Iterator it = value.iterator();
                    while(it.hasNext()){
                        props.add(it.next());
                    }
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PropertyRestriction.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
                return check(props);
            }
        }
        return check(type, null);
    }
}
