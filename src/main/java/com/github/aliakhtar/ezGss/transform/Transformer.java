package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;
import com.steadystate.css.parser.CSSOMParser;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.logging.Logger;

public class Transformer
{
    private final Logger log = Logging.get(this);

    private final String sourcePath;
    private final CSSOMParser parser = new CSSOMParser();
    private final CSSStyleSheet styleSheet;

    public Transformer(String sourcePath)
            throws IOException
    {
        this.sourcePath = sourcePath;

        Reader reader = new Reader();

        InputStreamReader streamReader = reader.getStreamReader(sourcePath);
        styleSheet = parser.parseStyleSheet( new InputSource(streamReader), null, null);

    }


    public Collection<String> getRawClasses()
    {
        return null;
    }
}
