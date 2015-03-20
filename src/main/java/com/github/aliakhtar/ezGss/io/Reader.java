package com.github.aliakhtar.ezGss.io;

import com.github.aliakhtar.ezGss.util.Logging;

import java.io.File;
import java.util.logging.Logger;

import static com.github.aliakhtar.ezGss.io.Config.*;
public class Reader
{


    private final Logger log = Logging.get(this);

    public String readFile(String path)
    {
        return null;
    }


    public static String resolve(String path)
    {
        if ( exists(path)  )
            return path;

        String newPath = workingDir();

        if (! path.startsWith( separator() ))
            newPath += separator();

        if (exists(newPath ))
            return newPath;

        throw new IllegalArgumentException("Path not found: " + path + " , attempted: " + newPath);
    }

}
