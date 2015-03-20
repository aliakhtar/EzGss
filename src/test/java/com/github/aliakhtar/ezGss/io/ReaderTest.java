package com.github.aliakhtar.ezGss.io;

import com.github.aliakhtar.ezGss.util.Logging;
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.logging.Logger;

import static org.junit.Assert.*;

import static com.github.aliakhtar.ezGss.io.Reader.*;
import static com.github.aliakhtar.ezGss.io.Config.*;
import static com.github.aliakhtar.ezGss.io.TestFiles.*;
public class ReaderTest
{
    Logger log = Logging.get(this);

    @Test
    public void testResolve() throws Exception
    {
        String resolved = resolve( BASIC );
        testExists(resolved);

        resolved = resolve( separator() + BASIC );
        testExists(resolved);

        String abs = workingDir() + separator() + BASIC;
        testExists( resolve(abs) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailResolve() throws Exception
    {
        resolve( workingDir() + separator() + DIR + separator() + "nope.txt" );
    }

    private void testExists(String resolved)
    {
        assertNotNull(resolved, resolved);
        assertTrue(resolved, exists(resolved) );
    }


    @Test
    public void testRead() throws Exception
    {
        Reader reader = new Reader();
        String output = reader.readFile(BASIC);
        assertNotNull(output);
        assertFalse(output, output.trim().isEmpty());
        assertTrue(output, output.contains("{"));
        assertTrue(output, output.contains("}"));

        InputStreamReader stream = reader.getStreamReader(BASIC);
        assertNotNull( stream );
        assertTrue( stream.ready() );
        stream.read();
        stream.close();
    }
}