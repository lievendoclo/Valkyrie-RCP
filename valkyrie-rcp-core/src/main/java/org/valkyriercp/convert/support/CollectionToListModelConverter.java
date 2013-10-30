package org.valkyriercp.convert.support;

import org.springframework.binding.convert.converters.Converter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class CollectionToListModelConverter extends ListToListModelConverter {
    @Override
    public Class getSourceClass() {
        return Collection.class;
    }

    @Override
    public Object convertSourceToTargetClass(Object source, Class targetClass) throws Exception {
        return super.convertSourceToTargetClass(new ArrayList((Collection) source), targetClass);
    }
}
