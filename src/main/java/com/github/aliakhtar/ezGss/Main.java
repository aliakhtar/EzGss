package com.github.aliakhtar.ezGss;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.io.Writer;
import com.github.aliakhtar.ezGss.transform.Transformer;

import java.io.File;

import static java.lang.String.format;
public class Main
{
    private Request req;
    private Transformer transformer;

    public static void main(String[] args) throws Exception
    {
        new Main(args);
    }

    public Main(String[] args) throws Exception
    {
        validate(args);
        String cssBlob = new Reader().readFile( req.getSource().getAbsolutePath() );

        transformer = new Transformer(req.getPackageName(), req.getJavaClassName(),
                                             cssBlob );
        new Writer().writeOrOverwrite(req.getDest().getAbsolutePath(),
                                             transformer.getFinalJavaCode() );

        exit( transformer.results(req) );
    }

    private void validate(String[] args)
    {
        String usage = "Usage: java -jar EzGss.jar <source.gss> <dest[.java]> [package]";
        if (args.length == 0)
            exit(usage, false);

        if (args[0].trim().isEmpty())
            exit(usage);

        if (args.length == 1 && args[0].toLowerCase().contains("help"))
            exit(usage);

        if (args.length < 2 || args[1].trim().isEmpty() )
            exit("Please provide the path where the resulting GssResource interface will be written.");

        if (args.length > 3 )
            exit( format("%d arguments provided, please provide 3 arguments, in the format: <source> <dest> [package]", args.length) );

        String source = args[0].trim();
        String dest = args[1].trim();
        String packageName = "???";

        if (! source.toLowerCase().endsWith(".gss") )
            exit( format("%s doesn't end in .gss", source) );

        if (args.length < 3 || ( args.length == 3 && args[2].trim().isEmpty() ) )
            System.out.println( format("(No package name given, using %s as package)", packageName) );
        else
            packageName = args[2].trim();

        req = new Request(source, dest, packageName);

        if (! req.getSource().exists() )
            exit(format("Source file: %s does not exist", req.getSource().getAbsolutePath()));

        if (! req.getSource().canRead() )
            exit(format("Source file: %s is not readable", req.getSource().getAbsolutePath()));

        if (! req.getDest().exists())
        {
            String dirPath = req.getDest().getAbsolutePath();
            dirPath = dirPath.replace( req.getDest().getName(), "" );
            File dir = new File(dirPath);
            dir.mkdirs();
        }
    }

    private void exit(String msg)
    {
        exit(msg, true);
    }

    private void exit(String message, boolean isError)
    {
        if (! isError)
            System.out.println(message);
        else
            System.err.println(message);
        System.exit(0);
    }
}
