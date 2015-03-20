package com.github.aliakhtar.ezGss.io;

import com.github.aliakhtar.ezGss.util.Logging;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.logging.Logger;

public class Config
{
    private static final Logger log = Logging.get( Config.class );
    private static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    private static final String WORKING_DIR =  readWorkingDir();


    private static String readWorkingDir()
    {
        String dir = System.getProperty("user.dir");

        //Make sure there's no trailing slash, for consistency:
        if ( dir.endsWith( separator() ))
            dir = dir.substring(0, dir.length() -1 );

        return dir;
    }

    public static String workingDir()
    {
        return WORKING_DIR;
    }

    public static String separator()
    {
        return SEPARATOR;
    }

    public static boolean exists(String path)
    {
        return new File(path).exists();
    }
}
