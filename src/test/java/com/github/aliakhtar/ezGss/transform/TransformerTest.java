package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

import static com.github.aliakhtar.ezGss.io.TestFiles.*;
import static com.github.aliakhtar.ezGss.transform.Transformer.*;
public class TransformerTest
{
    Logger log = Logging.get(this);
    private Reader reader;

    @Before
    public void setUp() throws Exception
    {
        reader = new Reader();
    }

    @Test
    public void testStripComments() throws Exception
    {
        for (String file : allTestStylesheets() )
        {
            String parsed = stripComments(reader.readFile(file));
            testNoComments( parsed );
        }
    }

    @Test
    public void testStripUrls() throws Exception
    {
        for (String file : allTestStylesheets() )
        {
            String parsed = stripUrls(reader.readFile(file));
            testNoUrls(parsed);
        }
    }

    @Test
    public void testCleanUp() throws Exception
    {
        for (String file : allTestStylesheets() )
        {
            String parsed = cleanUp(reader.readFile(file));
            testNoComments(parsed);
            testNoUrls(parsed);
        }
    }


    @Test
    public void testGetClasses() throws Exception
    {
        for (String file : allTestStylesheets() )
        {
            Collection<String> classes = getCssClasses( reader.readFile(file) );
            assertNotNull(classes);

            //normalize.css contains no classes, therefore it should be empty:
            if (! NORMALIZE.equals(file))
                assertFalse(classes.toString(), classes.isEmpty() );
            else
                assertTrue( classes.toString(), classes.isEmpty() );

            //Make sure each css class is counted only once:
            List<String> checkedList = new ArrayList<>( classes.size() );
            classes.stream().forEach(clazz ->
            {
                assertFalse(clazz, checkedList.contains(clazz));
                checkedList.add(clazz);
            });
        }
    }

    @Test
    public void testToJavaMethodName() throws Exception
    {
        assertJavaMethod("foobar", "foobar");
        assertJavaMethod("foo-bar-baz-1", "fooBarBaz1");
        assertJavaMethod("foo_bar_baz_2", "fooBarBaz2");
        assertJavaMethod("FOOBAR", "FOOBAR");
        assertJavaMethod("FOO_BAR", "fooBar");

        assertJavaMethod("fooBar", "fooBar");
        assertJavaMethod("fooBar-oops", "foobarOops");
        assertJavaMethod("fooBar_oops2", "foobarOops2");
        assertJavaMethod("foo_bar-baz_3", "fooBarbaz3");
        assertJavaMethod("FOO-BAR_BAZ", "fooBarbaz");
    }

    private void assertJavaMethod(String input, String expected)
    {
        String actual = toJavaMethodName(input);
        assertEquals(actual, expected, actual);
    }

    @Test
    public void testParse() throws Exception
    {
        for (String file : allTestStylesheets() )
        {
            Transformer t = new Transformer("com.foo.bar","SomeClass", reader.readFile(file) );
            assertTrue(t.toString(), t.getCssClasses().size() == t.getJavaMethodNames().size() );
            assertTrue( t.toString(), t.getCssClasses().size() == t.getTransformations().size() );

            if (! NORMALIZE.equals(file))
                assertFalse( t.toString(), t.getTransformations().isEmpty() );
            else
                assertTrue( t.toString(), t.getTransformations().isEmpty() );
        }
    }

    private void testNoComments(String parsed)
    {
        assertNotNull(parsed);
        assertFalse( parsed, parsed.isEmpty()  );
        assertFalse(parsed, parsed.contains("/*"));
        assertFalse(parsed, parsed.contains("*/"));
    }

    private void testNoUrls(String parsed)
    {
        assertNotNull(parsed);

        parsed = parsed.toLowerCase().trim();
        assertFalse(parsed, parsed.isEmpty());

        assertFalse(parsed, parsed.contains("url("));
        assertFalse(parsed, parsed.contains(".ttf"));
        assertFalse(parsed, parsed.contains(".eof"));
    }
}