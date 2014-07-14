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
