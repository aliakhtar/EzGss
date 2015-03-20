package com.github.aliakhtar.ezGss.io;

import com.github.aliakhtar.ezGss.util.Logging;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

import static com.github.aliakhtar.ezGss.io.Reader.*;
import static com.github.aliakhtar.ezGss.io.Config.*;
public class ReaderTest
{
    Logger log = Logging.get(this);

    @Test
    public void testResolve() throws Exception
    {
        String resolved = resolve("src/test/resources/basic.css");
        testExists(resolved);

        resolved = resolve("/src/test/resources/basic.css");
        testExists(resolved);

        String abs = workingDir() + separator() + "src/test/resources/basic.css";
        testExists( resolve(abs) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailResolve() throws Exception
    {
        resolve( workingDir() + separator() + "src/main/test/nope.txt" );
    }

    private void testExists(String resolved)
    {
        assertNotNull(resolved, resolved);
        assertTrue(resolved, exists(resolved) );
    }
}