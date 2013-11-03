package org.valkyriercp.sample.dataeditor.ui;

import com.google.common.base.Function;
import org.valkyriercp.form.binding.swing.editor.LookupBinder;
import org.valkyriercp.sample.dataeditor.domain.Supplier;
import org.valkyriercp.sample.dataeditor.domain.SupplierFilter;

import java.awt.*;

public class SupplierBinder extends LookupBinder<Supplier>
{
    public SupplierBinder()
    {
        setDataEditorId("supplierDataEditor");
        setRequiredSourceClass(Supplier.class);
        setObjectLabelFunction(new Function<Supplier, String>() {
            @Override
            public String apply( Supplier o) {
                return o.getName();
            }
        });
        setCreateFilterFromFieldFunction(new Function<String, SupplierFilter>() {
            @Override
            public SupplierFilter apply(String textFieldValue) {
                SupplierFilter s = new SupplierFilter();
                s.setNameContains(textFieldValue);
                return s;
            }
        });
        setDialogSize(new Dimension(800,600));
    }
}
