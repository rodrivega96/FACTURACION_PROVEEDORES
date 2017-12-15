package com.vates.eng.ha.util.impl;

import java.text.MessageFormat;

import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.message.Message;

import com.google.common.base.CharMatcher;

/**
 * Utilitary class.
 * 
 * @author Gaston Napoli
 * 
 */
@Slf4j
public class Utils {

    // Suppress default constructor for noninstantiability
    private Utils() {

        throw new AssertionError();

    }

    /**
     * It performs a wildcard matching for the provided text and pattern.
     * 
     * @param text
     *            the text to be tested for matches.
     * 
     * @param pattern
     *            the pattern to be matched for. This can contain the wildcard character '*' (asterisk).
     * 
     * @return <tt>true</tt> if a match is found, <tt>false</tt> otherwise.
     */
    public static boolean wildCardMatch(String text, String pattern) {

        // Create the cards by splitting using a RegEx. If more speed is desired, a simpler character based splitting can be done.
        String[] cards = pattern.split("\\*");

        // Iterate over the cards.
        for (String card : cards) {

            log.debug("\tcard: {}", card);

            int idx = text.indexOf(card);

            // Card not detected in the text.
            if (idx == -1) {

                return false;

            }

            // Move ahead, towards the right of the text.
            text = text.substring(idx + card.length());

        }

        return true;

    }

    /**
     * It returns a string representing the URI extracted from a CXF web service request.
     * 
     * @param message
     *            CXF message.
     * @return the corresponding URI.
     */
    public static String getUriFromMessage(Message message) {

        return HttpUtils.getPathToMatch(message, true);

    }

    /**
     * It trims a given character from a string in its beginning and end.
     * 
     * @param charToTrim
     *            character to trim.
     * @param source
     *            source where character will be trimmed.
     * @return The char-trimmed string.
     */
    public static String trim(final char charToTrim, final String source) {

        return CharMatcher.is(charToTrim).trimFrom(source);

    }

    /**
     * It replaces a given character with a string on a source string.
     * 
     * @param charToReplace
     *            character to be replaced.
     * @param replacement
     *            string which character will be replaced for.
     * @param source
     *            source where character will be replaced.
     * @return a string with the corresponding character replaced by the defined string.
     */
    public static String replace(final char charToReplace, final String replacement, final String source) {

        return CharMatcher.is(charToReplace).replaceFrom(source, replacement);

    }

    /**
     * It returns a formatted string which represents a grant. In this case, basic grant representation is based on the following pattern:
     * 
     * Operation parameter in lower case + underscore character + Resource parameter in lower case.
     * 
     * i.e.: 
     *     Operation = GET 
     *     Resource = User 
     * 
     * Therefore Grant = get_user
     * 
     * @param operation
     *            Operation applied on a resource.
     * @param resource
     *            Resource to be operated.
     * @return
     */
    public static String basicGrantFormat(String operation, String resource) {

        return MessageFormat.format("{0}_{1}", operation.toLowerCase(), resource.toLowerCase());

    }
}
