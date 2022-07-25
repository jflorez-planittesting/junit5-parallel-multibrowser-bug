package com.planittesting;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BrowsersArgumentsProvider implements ArgumentsProvider {
    private static final Logger logger = LoggerFactory.getLogger(BrowsersArgumentsProvider.class.getSimpleName());
    

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        var params = cartesianProduct(getBrowsers(context), List.of()).toList();
        params.forEach(a -> logger.info("BROWSER-ARGUMENT: "+a.get()[0]));
        return params.stream();
    }

    protected List<WithBrowsers.Browser> getBrowsers(ExtensionContext context) {
        List<WithBrowsers.Browser> browsers = List.of();
        var classLevelAnnotation = context.getRequiredTestClass().getAnnotation(WithBrowsers.class);
        var methodLevelAnnotation = context.getRequiredTestMethod().getAnnotation(WithBrowsers.class);
        if(classLevelAnnotation != null) browsers = Stream.of(classLevelAnnotation.value()).toList();
        //method level annotation overrides the class level one
        if(methodLevelAnnotation != null) browsers = Stream.of(methodLevelAnnotation.value()).toList();
        logger.info("BROWSERS: "+browsers.stream().map(b->b.toString()).collect(Collectors.joining(", ")));
        return browsers;
    }

    protected <T extends Arguments> Stream<Arguments> cartesianProduct(List<WithBrowsers.Browser> browsers, List<Object> parameters) {
        if (parameters.isEmpty()) return browsers.stream().map(Arguments::of);
        return browsers.stream().flatMap(ai -> parameters.stream().map(bi -> Arguments.of(ai, bi)));
    }
    
}
