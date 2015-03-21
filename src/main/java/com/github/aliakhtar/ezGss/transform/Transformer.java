package com.github.aliakhtar.ezGss.transform;

import com.github.aliakhtar.ezGss.io.Reader;
import com.github.aliakhtar.ezGss.util.Logging;
import com.steadystate.css.parser.CSSOMParser;
import com.sun.istack.internal.NotNull;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Transformer
{
    private final Logger log = Logging.get(this);

    private final String sourcePath;
    private final Set<String> rawClasses;

    private final static String COMMENT_REGEX = "/\\*.+?\\*/";

    public Transformer(String sourcePath)
            throws IOException
    {
        this.sourcePath = sourcePath;

        Reader reader = new Reader();

        InputStreamReader streamReader = reader.getStreamReader(sourcePath);

        CSSOMParser parser = new CSSOMParser();
        CSSStyleSheet styleSheet =
                parser.parseStyleSheet( new InputSource(streamReader), null, null);

        rawClasses = parse(styleSheet);
        streamReader.close();
    }

    private Set<String> parse(CSSStyleSheet styleSheet)
    {
        CSSRuleList ruleList = styleSheet.getCssRules();
        Set<String> classes = new HashSet<>( ruleList.getLength() );

        for (int i = 0; i < ruleList.getLength(); i++)
        {
            CSSRule rule = ruleList.item(i);
            if (! (rule instanceof CSSStyleRule))
                continue;

            CSSStyleRule cssRule = (CSSStyleRule) rule;
            String selector = cssRule.getSelectorText().toLowerCase().trim();
            if (! selector.contains("."))
                continue;

            log.info( selector );
            classes.add(selector);
        }

        return classes;
    }


    public Collection<String> getRawClasses()
    {
        return rawClasses;
    }

    public static String removeComments(@NotNull String cssBlob)
    {
        return cssBlob.replaceAll(COMMENT_REGEX, "");
    }
}
