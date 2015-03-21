package com.github.aliakhtar.ezGss;

import com.github.aliakhtar.ezGss.transform.Transformer;
import com.github.aliakhtar.ezGss.util.Logging;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.github.aliakhtar.ezGss.io.Config.*;
public class Request
{
    private final Logger log = Logging.get(this);

    private final File source;
    private final File dest;

    private final String javaClassName;

    public Request(String sourcePath, String destPath)
    {
        source = new File(sourcePath );

        File dest = new File( destPath );

        javaClassName = detectJavaClassName(dest);

        if ( dest.getName().toLowerCase().endsWith(".java") )
        {
            this.dest = dest;
            return;
        }

        if (! destPath.endsWith(  separator() ))
            destPath += separator();

        destPath += javaClassName + ".java";
        this.dest = new File(destPath);
    }

    private String detectJavaClassName(File dest)
    {
        //For Something.java , return Something, stripping out .java
        if (dest.isFile() || dest.getName().toLowerCase().endsWith(".java"))
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

    public File getSource()
    {
        return source;
    }

    public File getDest()
    {
        return dest;
    }
}
