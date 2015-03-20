package com.github.aliakhtar.ezGss.util;

import java.util.logging.Logger;

public class Logging
{
    public static Logger get(Object o)
    {
        return Logger.getLogger( o.getClass().getCanonicalName() );
    }
}
