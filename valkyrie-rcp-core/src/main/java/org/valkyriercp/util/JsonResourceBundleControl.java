package org.valkyriercp.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class JsonResourceBundleControl extends ResourceBundle.Control
{
    private static final String CHARSET = "UTF-8";
    private static final String FORMAT_JSON = "json";

    @Override
    public List<String> getFormats(String arg0)
    {
        List<String> formats = Lists.newArrayList();
        formats.add(FORMAT_JSON);
        return formats;
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException
    {
        if (!FORMAT_JSON.equals(format))
        {
            return null;
        }

        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, format);

        InputStream is = loader.getResourceAsStream(resourceName);
        if (is == null)
        {
            // Can happen, for example, if ResourceBundle searches a '_nl.json' file while you only have a '_nl_NL.json' file
            return null;
        }
        BufferedReader br = new BufferedReader( new InputStreamReader(is, CHARSET) );
        Gson gson = new Gson();
        Map map = gson.fromJson(br, Map.class);
        br.close();

        // Read the json data and iterate through all keys adding the values to a JSonResourceBundle

        JSonResourceBundle rb = new JSonResourceBundle();
        for (Object key : map.keySet())
        {
            rb.put(key.toString(), map.get(key));
        }

        return rb;
    }

    private static class JSonResourceBundle extends ResourceBundle
    {
        private Map<String, Object> data = new HashMap<String, Object>();

        @Override
        public Enumeration<String> getKeys()
        {
            return Collections.enumeration(data.keySet());
        }

        @Override
        protected Object handleGetObject(String key)
        {
            return data.get(key);
        }

        public void put(String key, Object value)
        {
            data.put(key, value);
        }
    }
}