package com.planittesting;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.engine.execution.AfterEachMethodAdapter;
import org.junit.jupiter.engine.execution.BeforeEachMethodAdapter;
import org.junit.jupiter.engine.extension.ExtensionRegistry;

public class BrowserParameterResolver
        implements ParameterResolver, AfterEachMethodAdapter, BeforeEachMethodAdapter {

    private ParameterResolver parameterisedTestParameterResolver;

    @Override
    public void invokeBeforeEachMethod(ExtensionContext context, ExtensionRegistry registry) {
        invoke(registry);
    }

    @Override
    public void invokeAfterEachMethod(ExtensionContext context, ExtensionRegistry registry) throws Throwable {
        invoke(registry);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        if (isExecutedOnAfterOrBeforeMethod(parameterContext)) {//} && !parameterContext.getParameter().getDeclaringExecutable().getDeclaringClass().getName().contains("BaseTest")) {
            ParameterContext pContext = getMappedContext(parameterContext, extensionContext);
            return parameterisedTestParameterResolver.supportsParameter(pContext, extensionContext);
        }
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterisedTestParameterResolver.resolveParameter(getMappedContext(parameterContext, extensionContext),extensionContext);
    }

    private void invoke(ExtensionRegistry registry) {
        Optional<ParameterResolver> resolverOptional = registry.getExtensions(ParameterResolver.class)
                .stream()
                .filter(parameterResolver -> parameterResolver.getClass().getName().contains("ParameterizedTestParameterResolver"))
                .findFirst();
        if (!resolverOptional.isPresent()) {
            throw new IllegalStateException("ParameterizedTestParameterResolver missed in the registry. Probably it's not a Parameterized Test");
        } else {
            parameterisedTestParameterResolver = resolverOptional.get();
            // try {
            //     var field = parameterisedTestParameterResolver.getClass().getDeclaredField("arguments");
            //     field.setAccessible(true);
            //     var args = (Object[])field.get(parameterisedTestParameterResolver);
            //     System.out.print("\nARGS: ");
            //     Stream.of(args).forEach(System.out::print);
            //     System.out.println("\n");
            // } catch (Exception e) {}
        }
    }

    private MappedParameterContext getMappedContext(ParameterContext parameterContext,
            ExtensionContext extensionContext) {
        return new MappedParameterContext(
                parameterContext.getIndex(),
                extensionContext.getRequiredTestMethod().getParameters()[parameterContext.getIndex()],
                Optional.of(parameterContext.getTarget()));
    }

    private boolean isExecutedOnAfterOrBeforeMethod(ParameterContext parameterContext) {
        return Arrays.stream(parameterContext.getDeclaringExecutable().getDeclaredAnnotations())
                .anyMatch(this::isAfterEachOrBeforeEachAnnotation);
    }

    private boolean isAfterEachOrBeforeEachAnnotation(Annotation annotation) {
        return annotation.annotationType() == BeforeEach.class || annotation.annotationType() == AfterEach.class;
    }
}
