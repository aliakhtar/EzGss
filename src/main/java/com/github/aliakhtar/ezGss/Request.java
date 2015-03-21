package com.github.aliakhtar.ezGss;

import com.github.aliakhtar.ezGss.transform.Transformer;
import com.github.aliakhtar.ezGss.util.Logging;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Request
{
    private final Logger log = Logging.get(this);

    private final File source;
    private final File dest;

    private final String javaClassName;

    public Request(String[] argsArray)
    {
        List<String> args = Arrays.asList(argsArray);
        source = (args.get(0) != null)  ? new File(args.get(0)) : null;
        dest = (args.get(1) != null) ? new File( args.get(1) ) : null;

        javaClassName = (dest != null) ? detectJavaClassName() : null;
    }

    private String detectJavaClassName()
    {
        //For Something.java , return Something, stripping out .java
        if (dest.isFile() )
            return dest.getName().substring( 0, dest.getName().lastIndexOf(".") );


        //e.g something-style.css -> SomethingStyle
        String name =  source.getName().substring(0, source.getName().lastIndexOf("."));

        name = Transformer.toJavaMethodName(name);
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

        return name;
    }

    public String getJavaClassName()
    {
        return javaClassName;
    }
}
