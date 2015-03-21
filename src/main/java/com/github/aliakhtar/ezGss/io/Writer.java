package com.github.aliakhtar.ezGss.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;
public class Writer
{
    public void writeOrOverwrite(String absPath, String output)
            throws IOException
    {
        Path path = Paths.get(absPath);
        Files.write(path, output.getBytes(), TRUNCATE_EXISTING, WRITE);
    }
}
