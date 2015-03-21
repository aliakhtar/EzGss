package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.util.Logging;


public class Transformation
{
    private final String cssClass;
    private final String javaMethod;

    public Transformation(String cssClass,
                          String javaMethod)
    {
        this.cssClass = cssClass;
        this.javaMethod = javaMethod;
    }

    public String getCssClass()
    {
        return cssClass;
    }

    public String getJavaMethod()
    {
        return javaMethod;
    }

    @Override
    public String toString()
    {
        return String.format("[%s -> %s]", cssClass, javaMethod);
    }
}
