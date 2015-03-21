package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;
import static com.google.common.base.CaseFormat.*;
public class Transformer
{
    private final Logger log = Logging.get(this);

    private final Set<String> rawClasses;

    private final static String COMMENT_REGEX = "/\\*.+?\\*/";

    private final static String URL_REGEX = "url\\([^)]+?\\)";
    private static final Pattern URL_PATTERN =
            compile(URL_REGEX, CASE_INSENSITIVE);

    private final static String CSS_CLASS_REGEX = "\\.[A-Z][\\w-]*";
    private final static Pattern CSS_CLASS_PATTERN
            = compile(CSS_CLASS_REGEX, CASE_INSENSITIVE);

    public Transformer(String cssBlob)
            throws IOException
    {

        rawClasses = getCssClasses( cssBlob );
    }



    public Collection<String> getRawClasses()
    {
        return rawClasses;
    }

    public static String stripComments(String cssBlob)
    {
        return cssBlob.replaceAll(COMMENT_REGEX, "");
    }

    public static String stripUrls(String cssBlob)
    {
        return URL_PATTERN.matcher(cssBlob).replaceAll("");
    }

    public static String cleanUp(String cssBlob)
    {
        cssBlob = cssBlob.toLowerCase();
        cssBlob = stripComments(cssBlob);
        cssBlob = stripUrls(cssBlob);
        return cssBlob;
    }

    public static Set<String> getCssClasses(String cssBlob)
    {
        cssBlob = cleanUp(cssBlob);
        Matcher matcher = CSS_CLASS_PATTERN.matcher(cssBlob);

        Set<String> classes = new HashSet<>();
        while (matcher.find() )
        {
            classes.add( matcher.group() );
        }

        return classes;
    }

    public static String toJavaMethodName(String cssClassName)
    {
        String javaMethodName = cssClassName;

        if (javaMethodName.contains("-"))
            javaMethodName = LOWER_HYPHEN.to(LOWER_CAMEL, javaMethodName);

        if (javaMethodName.contains("_"))
            javaMethodName = LOWER_UNDERSCORE.to(LOWER_CAMEL, javaMethodName);

        return javaMethodName;
    }
}
