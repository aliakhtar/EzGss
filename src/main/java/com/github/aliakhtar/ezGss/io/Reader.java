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


    /**
     * Resolves a path relative to the current working directory, to its
     * absolute path. If the path is already absolute, returns it as is.
     */
    public static String resolve(String path)
    {
        if ( path.startsWith( workingDir() ) && exists(path)  )
            return path;

        String newPath = workingDir();

        if (! path.startsWith( separator() ))
            newPath += separator();

        newPath += path;

        if (exists(newPath ))
            return newPath;

        throw new IllegalArgumentException("Path not found: " + path + " , attempted: " + newPath);
    }

}
