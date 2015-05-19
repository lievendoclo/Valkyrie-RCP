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
package org.valkyriercp.rules.constraint.property;

import org.springframework.context.MessageSourceResolvable;
import org.valkyriercp.rules.reporting.TypeResolvable;

public abstract class AbstractTypeResolvablePropertyConstraint extends AbstractPropertyConstraint
        implements
        TypeResolvable,
        MessageSourceResolvable
{

    private String type;

    public AbstractTypeResolvablePropertyConstraint()
    {
    }

    public AbstractTypeResolvablePropertyConstraint(String propertyName)
    {
        super(propertyName);
    }

    public AbstractTypeResolvablePropertyConstraint(String propertyName, String type)
    {
        super(propertyName);
        setType(type);
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Object[] getArguments()
    {
        return null;
    }

    public String[] getCodes()
    {
        return new String[]{type};
    }

    public String getDefaultMessage()
    {
        return type;
    }
}

