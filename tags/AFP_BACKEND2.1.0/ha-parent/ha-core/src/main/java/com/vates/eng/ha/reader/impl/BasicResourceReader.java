package com.vates.eng.ha.reader.impl;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.vates.eng.ha.service.BasicResourceInfo.PACKAGE_SEPARATOR;
import static com.vates.eng.ha.service.BasicResourceInfo.PATH_SEPARATOR;
import static com.vates.eng.ha.util.impl.Utils.replace;
import static com.vates.eng.ha.util.impl.Utils.trim;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.model.URITemplate;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.impl.BasicResource;
import com.vates.eng.ha.reader.ResourceReader;

/**
 * @author Gaston Napoli
 * 
 */
@Service
@Slf4j
public class BasicResourceReader extends ResourceReader {

    public static final String RESOURCE_PATTERN = "(^[/][^/]*)/*.*";

    private final Pattern pattern;

    /**
     * Default constructor.
     */
    public BasicResourceReader() {

        pattern = Pattern.compile(RESOURCE_PATTERN);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.reader.TokenReader#getToken(java.lang.Object)
     */
    @Override
    public BasicResource getToken(Message value) {

        String resource = null;

        // Gets data from method which is being routed.
        OperationResourceInfo resourceInfo = value.getExchange().get(OperationResourceInfo.class);

        if (resourceInfo != null) {

            // Gets path at class level.
            ClassResourceInfo classResourceInfo = resourceInfo.getClassResourceInfo();

            log.debug("classResourceInfo.path [{}]", classResourceInfo.getPath());

            if (classResourceInfo.getPath() != null) {

                resource = replace(PATH_SEPARATOR, PACKAGE_SEPARATOR, trim(PATH_SEPARATOR, classResourceInfo.getPath().value()));
           
            }

            URITemplate template = resourceInfo.getURITemplate();

            log.debug("template.getLiteralChars() = [{}]", template.getLiteralChars());

            Matcher matcher = pattern.matcher(template.getLiteralChars());

            if (matcher.matches()) {

                if (matcher.groupCount() == 1) {

                    String path = matcher.group(1);

                    log.info("Path enrutamiento: [{}]", path);

                    String element = trim(PATH_SEPARATOR, path);

                    if (!isNullOrEmpty(element)) {

                        resource = !isNullOrEmpty(resource) ? new StringBuilder(resource).append(PACKAGE_SEPARATOR).append(element).toString()
                                : element;

                        log.debug("resource new: [{}]", resource);

                    }

                }

            }

        }

        return new BasicResource(resource);

    }

}
