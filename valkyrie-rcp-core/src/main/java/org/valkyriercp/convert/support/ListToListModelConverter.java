package org.valkyriercp.convert.support;

import org.springframework.binding.convert.converters.Converter;
import org.valkyriercp.binding.value.support.ListListModel;

import javax.swing.*;
import java.util.List;

public class ListToListModelConverter implements Converter {
    @Override
    public Class getSourceClass() {
        return List.class;
    }

    @Override
    public Class getTargetClass() {
        return ListModel.class;
    }

    @Override
    public Object convertSourceToTargetClass(Object source, Class targetClass) throws Exception {
        return new ListListModel((List) source);
    }
}
