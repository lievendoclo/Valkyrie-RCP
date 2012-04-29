package org.valkyriercp.util;

import org.apache.commons.lang.time.DateUtils;

import javax.swing.text.DefaultFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This formatter expects format "##/##/####" or "########" to parse correctly. When no separator present, parsing from
 * right to left to find minutes, then remainder should be hours
 */
public class DateFormatter extends DefaultFormatter
{
    private static final long serialVersionUID = 1L;
    public static final char SEPARATOR = '/';
    public static final String SEPARATOR_STRING = "/";
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar referenceDay = null;

    public DateFormatter()
    {
        setAllowsInvalid(true);
        setCommitsOnValidEdit(false);
        setOverwriteMode(false);
        dateFormat.setLenient(false);
    }

    public Object stringToValue(String string) throws ParseException
    {
        if ((string == null) || (string.length() == 0))
            return null;
        Calendar localReferenceDay;
        if (referenceDay == null)
            localReferenceDay = Calendar.getInstance();
        else
            localReferenceDay = referenceDay;

        int length = string.length();
        int daySep = string.indexOf(SEPARATOR);
        int monthSep = string.indexOf(SEPARATOR, daySep + 1);
        // assume ('I had a dream that') users are consistent, when one '/' is given do completion, otherwise parse numbers
        if ((length == 1) && string.equals(SEPARATOR_STRING))// one separator means today
        {
            return localReferenceDay.getTime();
        }
        else if ((length == 2) && string.equals(SEPARATOR_STRING + SEPARATOR_STRING))
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            calendar.set(Calendar.MONTH, 12);
            calendar.set(Calendar.YEAR, 9999);
            return DateUtils.truncate(calendar, Calendar.DATE);
        }
        else if ((daySep == -1) && (length > 2)) // only numbers, at least 4 -> parse numbers right to left
        {
            int year = length > 4 ? Integer.parseInt(string.substring(length - 4)) : localReferenceDay.get(Calendar.YEAR);
            int month = -1;
            int day = -1;
            if (length < 5)// no further calculations, year = today
            {
            }
            else
            if ((length == 8) || ((year > (localReferenceDay.get(Calendar.YEAR) - 80)) && (year < (localReferenceDay.get(Calendar.YEAR) + 20)))) // in timeframe
                string = string.substring(0, length - 4);
            else
            {
                year = getCorrectYear(localReferenceDay, Integer.parseInt(string.substring(length - 2)));
                string = string.substring(0, length - 2);
            }
            if (string.length() == 1)
            {
                month = Integer.parseInt(string);
            }
            else if (string.length() > 1)
            {
                month = Integer.parseInt(string.substring(string.length() - 2));
                if (month > 12)
                {
                    month = Integer.parseInt(string.substring(string.length() - 1));
                    string = string.substring(0, string.length() - 1);
                }
                else
                    string = string.substring(0, string.length() - 2);
                if (string.length() > 0)
                    day = Integer.parseInt(string);
            }
            string = "" + (day == -1 ? 1 : day) + "/" + (month == -1 ? 1 : month) + "/" + year;
        }
        else // add missing elements
        {
            if (daySep == -1) // fix: dd + /
                string += SEPARATOR_STRING;
            if ((daySep == -1) || (daySep == length - 1)) // fix: dd/ + MM
                string += (localReferenceDay.get(Calendar.MONTH) + 1);
            if (monthSep == -1) // fix: dd/MM + /
                string += SEPARATOR_STRING;
            if ((monthSep == -1) || (monthSep == length - 1)) // fix: dd/MM/ + yyyy
                string += localReferenceDay.get(Calendar.YEAR);
            else if ((length - monthSep) <= 3) // fix: dd/MM/yy + yy
            {
                int shortYear = Integer.parseInt(string.substring(monthSep + 1));
                string = string.substring(0, monthSep + 1) + getCorrectYear(localReferenceDay, shortYear); // 19xx
            }
        }

        return dateFormat.parse(string);
    }

    private int getCorrectYear(Calendar localReferenceDay, int shortYear)
    {
        int todayYear = localReferenceDay.get(Calendar.YEAR);
        int todayCentury = todayYear - todayYear % 100;
        if ((todayCentury + shortYear) > (todayYear + 20))
            return (todayCentury - 100 + shortYear); // 19xx

        return (todayCentury + shortYear); // 20xx
    }

    public String valueToString(Object value) throws ParseException
    {
        if (value != null)
        {
            return dateFormat.format(value);
        }
        return "";
    }

    public void setReferenceDay(Calendar referenceDay)
    {
        this.referenceDay = referenceDay;
    }
}
