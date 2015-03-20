package com.github.aliakhtar.ezGss.transform;

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
        Transformer transformer = new Transformer(BOOTSTRAP);
        log.info(transformer.getRawClasses().toString());
    }
}