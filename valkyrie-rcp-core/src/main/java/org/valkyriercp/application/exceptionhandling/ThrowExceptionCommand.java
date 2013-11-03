package org.valkyriercp.application.exceptionhandling;

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.command.support.ApplicationWindowAwareCommand;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * A development command used to test the exception handler configuration.
 * Pops up a dialog and allows the user to type in the full classname of an exception to throw.
 * <p/>
 * It is usually not used in production and therefor configured with this property:
 * &lt;property name="visible" value="${development.mode}" /&gt;
 * @author gds
 */

public class ThrowExceptionCommand extends ApplicationWindowAwareCommand
{
    private static final String THROWABLE_MESSAGE = "Throwable message made by ThrowExceptionCommand";

    private final static String ID = "throwExceptionCommand";

    public ThrowExceptionCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand()
    {
        List<Class<? extends Throwable>> throwableClassList = askUserThrowableClassList();
        if (throwableClassList != null) {
            throwThrowable(throwableClassList);
        }
    }

    private List<Class<? extends Throwable>> askUserThrowableClassList()
    {
        String throwableClassListString = (String) JOptionPane.showInputDialog(resolveParentFrame(),
                "Please fill in the full classname of the exception to throw:\n"
                        + "Causes can optionally be appended by semicolons(;). For example:\n"
                        + "  org.springframework.remoting.RemoteLookupFailureException;"
                        + "java.rmi.ConnectException;java.net.ConnectException",
                "Which exception do you want to throw?",
                JOptionPane.INFORMATION_MESSAGE, null, null, "java.lang.IllegalStateException");
        if (throwableClassListString == null) {
            // User cancelled
            return null;
        }
        List<Class<? extends Throwable>> throwableClassList = new ArrayList<Class<? extends Throwable>>();
        String[] throwableClassListStringTokens = throwableClassListString.split(";");
        for (String throwableClassListStringToken : throwableClassListStringTokens)
        {
            Class<? extends Throwable> throwableClass;
            try
            {
                throwableClass = (Class<? extends Throwable>) Class.forName(throwableClassListStringToken);
            }
            catch (ClassNotFoundException e)
            {
                throw new IllegalArgumentException(
                        "ThrowExceptionCommand failure: Class (" + throwableClassListStringToken
                                + ") does not exist.", e);
            }
            throwableClassList.add(throwableClass);
        }
        return throwableClassList;
    }

    private JFrame resolveParentFrame() {
        ApplicationWindow activeWindow = ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow();
        return (activeWindow == null) ? null : activeWindow.getControl();
    }

    private void throwThrowable(List<Class<? extends Throwable>> throwableClassList)
    {
        Class<? extends Throwable> throwableClass = throwableClassList.remove(0);
        Throwable throwable;
        try
        {
            throwable = buildThrowable(throwableClassList, throwableClass);
        }
        catch (InstantiationException e)
        {
            throw new IllegalArgumentException(
                    "ThrowExceptionCommand failure: Could not build throwable chain.", e);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException(
                    "ThrowExceptionCommand failure: Could not build throwable chain.", e);
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException(
                    "ThrowExceptionCommand failure: Could not build throwable chain.", e);
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException(
                    "ThrowExceptionCommand failure: Could not build throwable chain.", e);
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new IllegalArgumentException("ThrowExceptionCommand failure: The root throwable ("
                    + throwableClass + ") should extend RuntimeException or Error.");
        }
    }

    private Throwable buildThrowable(List<Class<? extends Throwable>> throwableClassList,
            Class<? extends Throwable> throwableClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException
    {
        Throwable throwable;
        if (throwableClassList.isEmpty()) {
            try {
                throwable = throwableClass.getDeclaredConstructor(String.class).newInstance(
                        THROWABLE_MESSAGE);
            } catch (NoSuchMethodException e) {
                throwable = throwableClass.newInstance();
            }
        } else {
            Throwable cause = buildThrowAndCatch(throwableClassList);
            try {
                throwable = throwableClass.getDeclaredConstructor(String.class, Throwable.class)
                        .newInstance(THROWABLE_MESSAGE, cause);
            } catch (NoSuchMethodException e) {
                try {
                    throwable = throwableClass.getDeclaredConstructor(String.class, Exception.class)
                            .newInstance(THROWABLE_MESSAGE, cause);
                } catch (NoSuchMethodException e2) {
                    try {
                        throwable = throwableClass.getDeclaredConstructor(Throwable.class).newInstance(cause);
                    } catch (NoSuchMethodException e3) {
                        throwable = throwableClass.getDeclaredConstructor(Exception.class).newInstance(cause);
                    }
                }
            }
        }
        return throwable;
    }

    private Throwable buildThrowAndCatch(List<Class<? extends Throwable>> throwableClassList)
            throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException
    {
        Class<? extends Throwable> throwableClass = throwableClassList.remove(0);
        Throwable throwable = buildThrowable(throwableClassList, throwableClass);
        try {
            throw throwable;
        } catch (Throwable catchedThrowable) {
            return catchedThrowable;
        }
    }

}

