package org.valkyriercp.widget.editor.provider;

public class MaximumRowsExceededException extends RuntimeException
{
    private int maxRows;
    private int numberOfRows;

    public MaximumRowsExceededException(int maxRows, int numberOfRows)
    {
        this.maxRows = maxRows;
        this.numberOfRows = numberOfRows;
    }

    public int getMaxRows()
    {
        return maxRows;
    }

    public int getNumberOfRows()
    {
        return numberOfRows;
    }
}
