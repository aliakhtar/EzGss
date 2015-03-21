package com.github.aliakhtar.ezGss.io;

import com.github.aliakhtar.ezGss.util.Logging;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

import static com.github.aliakhtar.ezGss.io.Config.*;
public class Reader
{
    private final Logger log = Logging.get(this);

    public String readFile(String readPath)
            throws IOException
    {
        String absolutePath = resolve(readPath);
        Path path = Paths.get(absolutePath);
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(sb::append);

            return sb.toString().trim();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public InputStreamReader getStreamReader(String readPath)
            throws IOException
    {
        String absolutePath = resolve(readPath);
        Path path = Paths.get(absolutePath);
        return new InputStreamReader( Files.newInputStream(path) );
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

    public static String readResource(String path)
    {
        ClassLoader clsLoader = Reader.class.getClassLoader();
        try (
                   InputStream is = clsLoader.getResourceAsStream(path);
                   InputStreamReader isr = new InputStreamReader(is);
                   BufferedReader reader = new BufferedReader(isr);
            )
        {
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(sb::append);
            return sb.toString();
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e);
        }
    }
}
