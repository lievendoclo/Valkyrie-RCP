package org.valkyriercp.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Oliver Hutchison
 * @author Geoffrey De Smet
 */
public class TestBean {

    private String simpleProperty;

    private Date dateProperty;

    public TestEnum enumProperty;

    private Map mapProperty = Maps.newHashMap();

    private List listProperty = Lists.newArrayList();

    private Object[] arrayProperty;

    private Object singleSelectListProperty;

    private TestBean nestedProperty;

    public Object readOnly;

    public Object writeOnly;

    public Number numberProperty;

    private boolean booleanProperty;

    public Number getNumberProperty() {
        return numberProperty;
    }

    public void setNumberProperty(Number numberProperty) {
        this.numberProperty = numberProperty;
    }

    public String getSimpleProperty() {
        return simpleProperty;
    }

    public void setSimpleProperty(String simpleProperty) {
        this.simpleProperty = simpleProperty;
    }

    public Date getDateProperty() {
        return dateProperty;
    }

    public void setDateProperty(Date dateProperty) {
        this.dateProperty = dateProperty;
    }

    public TestEnum getEnumProperty() {
        return enumProperty;
    }

    public void setEnumProperty(TestEnum enumProperty) {
        this.enumProperty = enumProperty;
    }

    public Map getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map mapProperty) {
        this.mapProperty = mapProperty;
    }

    public List getListProperty() {
        return listProperty;
    }

    public void setListProperty(List listProperty) {
        this.listProperty = listProperty;
    }

    public Object getSingleSelectListProperty() {
        return singleSelectListProperty;
    }

    public void setSingleSelectListProperty(final Object singleSelectListProperty) {
        this.singleSelectListProperty = singleSelectListProperty;
    }

    public TestBean getNestedProperty() {
        return nestedProperty;
    }

    public void setNestedProperty(TestBean nestedProperty) {
        this.nestedProperty = nestedProperty;
    }

    public Object getReadOnly() {
        return readOnly;
    }

    public void setWriteOnly(Object writeOnly) {
        this.writeOnly = writeOnly;
    }

    public Object[] getArrayProperty() {
        return arrayProperty;
    }

    public void setArrayProperty(Object[] arrayProperty) {
        this.arrayProperty = arrayProperty;
    }

    public boolean isBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

}
