/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.sample.dataeditor.domain;

public class SupplierFilter
{
    private String nameContains;

    private String contactNameContains;

    public String getContactNameContains()
    {
        return contactNameContains;
    }

    public void setContactNameContains(String contactNameContains)
    {
        this.contactNameContains = contactNameContains;
    }

    public String getNameContains()
    {
        return nameContains;
    }

    public void setNameContains(String nameContains)
    {
        this.nameContains = nameContains;
    }

    public static SupplierFilter fromSupplier(Supplier f)
    {
        SupplierFilter filter = new SupplierFilter();
        filter.setContactNameContains(f.getContactName());
        filter.setNameContains(f.getName());
        return filter;
    }
}
