package com.github.aliakhtar.ezGss;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.io.Writer;
import com.github.aliakhtar.ezGss.transform.Transformer;

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

        transformer = new Transformer( req.getJavaClassName(), cssBlob );
        new Writer().writeOrOverwrite(req.getDest().getAbsolutePath(), transformer.getFinalJavaCode() );
    }

    private void validate(String[] args)
    {
        if (args.length == 0)
            exit("Usage: java -jar EzGss.jar <source.gss> <dest[.java]>");

        if (args[0].trim().isEmpty() )
            exit("Please provide the path to the source gss file.");

        if (args.length < 2 || args[1].trim().isEmpty() )
            exit("Please provide the path where the resulting GssResource interface will be written.");

        if (args.length > 2)
            exit( format("%d arguments provided, please provide 2 arguments, in the format: source dest", args.length) );

        String source = args[0].trim();
        String dest = args[1].trim();

        if (! source.toLowerCase().endsWith(".gss") )
            exit( format("%s doesn't end in .gss", source) );

        req = new Request(source, dest);

        if (! req.getSource().exists() )
            exit(format("Source file: %s does not exist", req.getSource().getAbsolutePath()));

        if (! req.getSource().canRead() )
            exit(format("Source file: %s is not readable", req.getSource().getAbsolutePath()));

        if (! req.getDest().exists())
            req.getDest().mkdirs();
    }

    private void exit(String message)
    {
        System.out.println(message);
        System.exit(0);
    }
}
