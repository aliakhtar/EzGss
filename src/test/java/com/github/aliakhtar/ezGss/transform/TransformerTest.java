package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

import static com.github.aliakhtar.ezGss.io.TestFiles.*;
public class TransformerTest
{
    Logger log = Logging.get(this);

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
    public void testCommentRemoval() throws Exception
    {
        Reader r = new Reader();
        testNoComments(BASIC, BOOTSTRAP_MIN, FOUNDATION, FOUNDATION_MIN, NORMALIZE);
    }

    private void testNoComments(String... files) throws Exception
    {
        Reader r = new Reader();
        for (String file : files)
        {
            String parsed = Transformer.removeComments( r.readFile(file) );
            assertNotNull(parsed);
            assertFalse( parsed, parsed.isEmpty()  );
            assertFalse(parsed, parsed.contains("/*"));
            assertFalse(parsed, parsed.contains("*/"));
        }
    }
}