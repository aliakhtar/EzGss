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
import static java.lang.String.format;
public class Transformer
{
    private final static String COMMENT_REGEX = "/\\*.+?\\*/";

    private final static String URL_REGEX = "url\\([^)]+?\\)";
    private static final Pattern URL_PATTERN =
            compile(URL_REGEX, CASE_INSENSITIVE);

    private final static String CSS_CLASS_REGEX = "\\.[A-Z][\\w-]*";
    private final static Pattern CSS_CLASS_PATTERN
            = compile(CSS_CLASS_REGEX, CASE_INSENSITIVE);

    private final Logger log = Logging.get(this);

    private final Collection<String> rawClasses;
    private final Set<String> javaMethods;
    private final List<Transformation> transforms;

    public Transformer(String cssBlob)
            throws IOException
    {

        rawClasses = getCssClasses( cssBlob );
        javaMethods = new HashSet<>( rawClasses.size() );
        transforms = new ArrayList<>( rawClasses.size() );
        parse();
    }

    private void parse()
    {
        for (String className : rawClasses)
        {
            String javaMethodName = toJavaMethodName(className).trim();
            if (javaMethodName.isEmpty() )
            {
                log.warning(format("Skipping class: %s, couldn't be converted to java method name", className));
                continue;
            }

            if (javaMethods.contains(javaMethodName))
            {
                log.warning(format("Skipping %s , duplicate java method name: %s", className, javaMethodName));
                continue;
            }

            javaMethods.add(javaMethodName);
            transforms.add( new Transformation(className, javaMethodName) );
        }
    }

    public Collection<String> getRawClasses()
    {
        return rawClasses;
    }

    public Collection<String> getJavaMethodNames()
    {
        return javaMethods;
    }

    public Collection<Transformation> getTransformations()
    {
        return transforms;
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
        cssBlob = stripComments(cssBlob);
        cssBlob = stripUrls(cssBlob);
        return cssBlob;
    }

    public static Collection<String> getCssClasses(String cssBlob)
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
