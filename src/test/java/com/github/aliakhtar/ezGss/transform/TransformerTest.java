package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
    @Ignore
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
        log.info( Transformer.stripUrls( reader.readFile(BASIC) ) );
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