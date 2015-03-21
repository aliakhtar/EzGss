package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

import static com.github.aliakhtar.ezGss.io.TestFiles.*;
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
    public void testGetRawClasses() throws Exception
    {
        Transformer transformer = new Transformer(BASIC);
        log.info( transformer.getRawClasses().toString() );
    }

    @Ignore
    @Test
    public void testBootstrap() throws Exception
    {
        Transformer transformer = new Transformer(BOOTSTRAP_MIN);
        log.info(transformer.getRawClasses().toString());
    }

    @Test
    public void testStripComments() throws Exception
    {
        for (String file : allTestStylesheets() )
        {
            String parsed = Transformer.stripComments(reader.readFile(file));
            testNoComments( parsed );
        }
    }

    private void testNoComments(String parsed)
    {
        assertNotNull(parsed);
        assertFalse( parsed, parsed.isEmpty()  );
        assertFalse(parsed, parsed.contains("/*"));
        assertFalse(parsed, parsed.contains("*/"));
    }
}