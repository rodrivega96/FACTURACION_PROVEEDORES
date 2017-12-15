package com.vates.eng.ha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.testng.annotations.Test;

import lombok.extern.java.Log;

@Log
public class PatternTest {

    @Test
    public void test() {

        Pattern pattern = Pattern.compile("(^[/][^/]*)/.*");

        String value = "/echo/1/to/2";
        
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches()) {

            log.info("Dump: " + ToStringBuilder.reflectionToString(matcher, ToStringStyle.MULTI_LINE_STYLE));

            log.info("group: " + matcher.groupCount());
            
            log.info("group 0: " + matcher.group(0));
            
            log.info("group 1: " + matcher.group(1));
            
            log.info("end: " + matcher.end());
            
            log.info("group: " + matcher.group());
            
            log.info("end(0): " + matcher.end(0));
            
            log.info("end(1): " + matcher.end(1));

        }

    }

}
