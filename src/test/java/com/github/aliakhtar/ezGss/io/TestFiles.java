package com.github.aliakhtar.ezGss.io;

import java.util.logging.Logger;

import static com.github.aliakhtar.ezGss.io.Config.*;
public class TestFiles
{
    public static final String DIR = "src" +separator() + "test" + separator()
                                                                   + "resources";
    public static final String BASIC = DIR + separator() + "basic.css";

    public static final String BOOTSTRAP = DIR + separator() + "bootstrap.min.css";

    public static final String BOOTSTRAP_MIN = DIR + separator() + "bootstrap.min.css";

    public static final String FOUNDATION = DIR + separator() + "foundation.css";

    public static final String FOUNDATION_MIN = DIR + separator() + "foundation.min.css";

    public static final String NORMALIZE = DIR + separator() + "normalize.css";


    public static String[] allTestStylesheets()
    {
        return new String[]{ BASIC, BOOTSTRAP, BOOTSTRAP_MIN, FOUNDATION,
                             FOUNDATION_MIN, NORMALIZE };
    }
}
