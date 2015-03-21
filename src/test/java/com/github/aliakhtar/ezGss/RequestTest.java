package com.github.aliakhtar.ezGss;

import org.junit.Test;

import static org.junit.Assert.*;

import static com.github.aliakhtar.ezGss.io.Config.*;
public class RequestTest
{
    @Test
    public void testGetJavaClassName() throws Exception
    {
        String dir = workingDir() + separator();

        testClassName("", dir + "Foo.java", "Foo");
        testClassName(dir + "foo-bar.css", "", "FooBar");
    }

    private void testClassName(String source, String dest, String expected)
    {
        String[] args = new String[] {source,  dest };
        Request r = new Request(args);

        assertEquals(r.getJavaClassName(), expected, r.getJavaClassName());
    }
}