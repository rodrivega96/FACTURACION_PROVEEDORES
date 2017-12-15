package com.vates.eng.ha.service;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Maps.newConcurrentMap;
import static com.vates.eng.ha.util.impl.Utils.trim;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * @author Gaston Napoli
 * 
 */
@Service("ha.security.resourceInfo")
@Slf4j
public class BasicResourceInfo implements BeanDefinitionRegistryPostProcessor {

    public static final String RESOURCE_PATTERN = "(^[/][^/{]*)/*.*";

    public static final String DEFAULT_OPERATION = "get";

    public static final String PACKAGE_SEPARATOR = "::";

    public static final char TOKEN_SEPARATOR = '_';

    public static final char PATH_SEPARATOR = '/';

    private static final Map<String, String> REST_OPERATIONS = newConcurrentMap();

    static {

        REST_OPERATIONS.put("javax.ws.rs.GET", "get");

        REST_OPERATIONS.put("javax.ws.rs.POST", "post");

        REST_OPERATIONS.put("javax.ws.rs.PUT", "put");

        REST_OPERATIONS.put("javax.ws.rs.DELETE", "delete");

    }

    private Pattern pattern;

    private Set<String> resources;

    /**
     * Default constructor.
     */
    public BasicResourceInfo() {

        resources = Sets.newHashSet();

        pattern = Pattern.compile(RESOURCE_PATTERN);

    }

    /*
     * It returns which methods of a given bean are annotated with @Path.
     * 
     * @param targetBean bean to be evaluated.
     * 
     * @return the set of annotated methods.
     */
    private Set<Method> findAllWithPath(Class<?> targetBean) {

        Set<Method> methods = Sets.newHashSet(targetBean.getMethods());

        return ImmutableSet.copyOf(Iterables.filter(methods, new Predicate<Method>() {

            @Override
            public boolean apply(Method input) {

                return input.isAnnotationPresent(Path.class);

            }

        }));

    }

    public Set<String> getAvailableResources() {

        return ImmutableSet.copyOf(resources);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory
     * .support.BeanDefinitionRegistry)
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        List<BeanDefinition> serviceBeans = new ManagedList<BeanDefinition>();

        BeanDefinition cxfServerBean = registry.getBeanDefinition("ha.security.server");

        // It will try extract from @Path each individual resource path, both class level and method level.
        for (String beanName : registry.getBeanDefinitionNames()) {

            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);

            String beanClassName = beanDefinition.getBeanClassName();

            Class<?> beanClass = null;

            try {

                if (beanClassName != null) {

                    beanClass = Class.forName(beanClassName);

                    // It gets the @Path from the bean's class, if it was annotated.
                    Path mainPath = AnnotationUtils.getAnnotation(beanClass, Path.class);

                    String mainResource = null;

                    if (mainPath != null) {

                        serviceBeans.add(beanDefinition);
                        
                        log.debug("Service found: {} - {}", beanClassName, mainPath);

                        mainResource = CharMatcher.is(PATH_SEPARATOR).replaceFrom(trim(PATH_SEPARATOR, mainPath.value()), PACKAGE_SEPARATOR);

                    }

                    // It tries to find each annotated method, if there is any.
                    for (final Method method : findAllWithPath(beanClass)) {

                        // It extract the the path from annotation.
                        String literal = method.getAnnotation(Path.class).value();

                        String operation = DEFAULT_OPERATION;

                        // It gets what REST operation is being performed.
                        for (Annotation annotation : AnnotationUtils.getAnnotations(method)) {

                            // It compares each method-level annotation with REST operations annotations.
                            String annotatedOperation = annotation.annotationType().getName();
                            
                            if (REST_OPERATIONS.containsKey(annotatedOperation)) {

                                operation = REST_OPERATIONS.get(annotatedOperation);

                            }

                        }

                        Matcher matcher = pattern.matcher(literal);

                        if (matcher.matches()) {

                            if (matcher.groupCount() == 1) {

                                String element = trim(PATH_SEPARATOR, matcher.group(1));

                                StringBuilder resource = new StringBuilder(operation).append(TOKEN_SEPARATOR);

                                if (isNullOrEmpty(mainResource) && isNullOrEmpty(element)) {

                                    // If it is null or doesn't have content (any other than '/')
                                    continue;

                                }

                                resource = !isNullOrEmpty(element) ? !isNullOrEmpty(mainResource) ? resource.append(mainResource)
                                        .append(PACKAGE_SEPARATOR).append(element) : resource.append(element) : resource.append(mainResource);

                                log.debug("resource {}", resource);

                                resources.add(resource.toString().toUpperCase());

                            }

                        }

                    }

                }

            } catch (ClassNotFoundException e) {

                throw new FatalBeanException("Error getting Path info", e);

            }

        }

        // At least one CXF server bean must be defined
        if (cxfServerBean == null) {

            throw new FatalBeanException("No CXF security server bean has been defined!!!");

        }

        // At least one web service must be defined
        if (serviceBeans.size() == 0) {

            throw new FatalBeanException("At least one service should be defined, there are no @Path services defined!!!");

        }

        // It overrides <jaxrs:serviceBeans> setting of CXF server.
        MutablePropertyValues mpv = cxfServerBean.getPropertyValues();

        mpv.addPropertyValue("serviceBeans", serviceBeans);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.
     * ConfigurableListableBeanFactory)
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
