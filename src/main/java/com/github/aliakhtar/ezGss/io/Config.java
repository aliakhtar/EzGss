package com.github.aliakhtar.ezGss.io;

import com.github.aliakhtar.ezGss.util.Logging;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.logging.Logger;

public class Config
{
    private final Logger log = Logging.get(this);
    private static final String WORKING_DIR =  System.getProperty("user.dir");
    private static final String SEPARATOR = FileSystems.getDefault().getSeparator();


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
