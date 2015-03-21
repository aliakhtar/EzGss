package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.Request;
import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;
import static com.google.common.base.CaseFormat.*;
import static java.lang.String.format;
public class Transformer implements Iterable<Transformation>
{
    private final static String COMMENT_REGEX = "/\\*.+?\\*/";
    private final static Pattern COMMENT_PATTERN =
            compile(COMMENT_REGEX, DOTALL);

    private final static String URL_REGEX = "url\\([^)]+?\\)";
    private static final Pattern URL_PATTERN =
            compile(URL_REGEX, CASE_INSENSITIVE);

    private final static String CSS_CLASS_REGEX = "\\.[A-Z][\\w-]*";
    private final static Pattern CSS_CLASS_PATTERN
            = compile(CSS_CLASS_REGEX, CASE_INSENSITIVE);


    private final static String FILE_TPL = Reader.readResource("fileTemplate.txt");
    private final static String METHOD_TPL = Reader.readResource("methodTemplate.txt");

    private final static Set<String> RESERVED_KEYWORDS = getReservedKeywords();

    private final Logger log = Logging.get(this);

    private final String javaClassName;
    private final Collection<String> cssClasses;
    private final Set<String> javaMethods;
    private final List<Transformation> transforms;
    private String finalOutput;
    private Set<String> reservedKeywords;

    public Transformer(String javaClassName, String cssBlob)
            throws IOException
    {
        this.javaClassName = javaClassName;
        cssClasses = getCssClasses( cssBlob );
        javaMethods = new HashSet<>( cssClasses.size() );
        transforms = new ArrayList<>( cssClasses.size() );
        parse();
    }

    private void parse()
    {
        for (String className : cssClasses)
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

    public String getFinalJavaCode()
    {
        if (finalOutput == null)
            finalOutput = buildFinalOutput();

        return finalOutput;
    }

    private String buildFinalOutput()
    {
        String tpl = FILE_TPL.replace("$className", javaClassName);
        StringBuilder sb = new StringBuilder();

        for (Transformation t : transforms )
        {
            String method = METHOD_TPL.replace("$css", t.getCssClass());
            method = method.replace("$java", t.getJavaMethod() );
            sb.append(method);
        }

        tpl = tpl.replace("$methods", sb.toString() );
        return tpl;
    }

    public Collection<String> getCssClasses()
    {
        return cssClasses;
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
        return COMMENT_PATTERN.matcher(cssBlob).replaceAll("");
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
            //Remove preceding . from className, to .foo becomes foo
            classes.add( matcher.group().substring(1) );
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

        if (RESERVED_KEYWORDS.contains(javaMethodName))
            javaMethodName += "_";

        return javaMethodName;
    }

    public String results(Request req)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Done, source: ").append( req.getSource().getAbsolutePath() )
                .append(", dest: ").append( req.getDest().getAbsolutePath() );
        sb.append(", css classes: ").append(cssClasses.size())
          .append(", java method names: ").append(javaMethods.size() );
        return sb.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (Transformation t : transforms)
        {
            sb.append("\t").append(t).append("\n");
        }
        sb.append("], css classes: ").append(cssClasses.size())
          .append(", java method names: ").append(javaMethods.size() );
        return sb.toString();
    }

    @Override
    public Iterator<Transformation> iterator()
    {
        return transforms.iterator();
    }

    private static Set<String> getReservedKeywords()
    {
        String[] reserved = new String[]
        {
                "assert",
                "abstract", "boolean", "break", "byte",
                "case", "catch", "char", "class",
                "const", "continue", "default", "do",
                "double", "else", "enum", "extends",
                "false", "final",
                "finally", "float", "for", "goto",
                "if", "implements", "import",
                "instanceof", "int", "interface",
                "long", "native", "new", "null", "package",
                "private", "protected", "public",
                "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this",
                "throw", "throws", "transient",
                "try",  "true", "void", "volatile", "while",
        };

        //Using a set as there may be duplicates in the array
        Set<String> result = new HashSet<>( );
        result.addAll( Arrays.asList(reserved) );
        return result;
    }
}
