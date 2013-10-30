package org.valkyriercp.sample.showcase.messages;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.props.Props;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JoddPropsMessageSource extends AbstractMessageSource {
    private List<String> baseResourceNames;
    private Map<Locale, Props> cachedProps;
    private String extension;

    public JoddPropsMessageSource(String extension) {
        this.extension = extension;
        baseResourceNames = Lists.newArrayList();
        cachedProps = Maps.newHashMap();
    }

    public JoddPropsMessageSource() {
        this("props");
    }

    public List<String> addBaseName(String baseName) {
        baseResourceNames.add(baseName);
        return baseResourceNames;
    }

    private Props getProps(Locale locale) {
        if (cachedProps.containsKey(locale)) {
            return cachedProps.get(locale);
        } else {
            Props props;
            try {
                props = createProps(locale);
            } catch (IOException e) {
                throw new RuntimeException("Error reading JODD properties", e);
            }
            cachedProps.put(locale, props);
            return props;
        }
    }

    private Props createProps(Locale locale) throws IOException {
        Props props = new Props();
        for (String baseName : baseResourceNames) {
            ClassPathResource langAndCountry = new ClassPathResource(baseName + "_" + locale.getLanguage().toLowerCase() + "_" + locale.getCountry().toLowerCase() + "." + extension);
            if (langAndCountry.exists()) {
                props.load(langAndCountry.getInputStream());
            } else {
                ClassPathResource lang = new ClassPathResource(baseName + "_" + locale.getLanguage().toLowerCase() + "." + extension);
                if (lang.exists()) {
                    props.load(lang.getInputStream());
                } else {
                    ClassPathResource fallback = new ClassPathResource(baseName + "." + extension);
                    if (fallback.exists()) {
                        props.load(fallback.getInputStream());
                    }
                    else {
                        throw new IllegalStateException("Could not find JODD properties for basename " + baseName);
                    }
                }
            }
        }
        return props;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String value = getProps(locale).getValue(code);
        return new MessageFormat(value != null ? value : "", locale);
    }
}
