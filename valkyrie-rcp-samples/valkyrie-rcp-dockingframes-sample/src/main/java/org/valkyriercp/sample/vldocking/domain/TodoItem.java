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
package org.valkyriercp.sample.vldocking.domain;

import java.util.Date;

public class TodoItem implements Comparable<TodoItem>
{
    private String name;
    private String description;
    private Date todoDate;

    public TodoItem(String name, String description, Date todoDate)
    {
        this.name = name;
        this.description = description;
        this.todoDate = todoDate;
    }

    public TodoItem()
    {
        this(null, null, new Date());
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getTodoDate()
    {
        return todoDate;
    }

    public void setTodoDate(Date todoDate)
    {
        this.todoDate = todoDate;
    }

    public int compareTo(TodoItem item)
    {  
        return getName().compareTo(item.getName());
    }
}
