package io.ib67;

import io.ib67.util.reflection.AccessibleClass;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.function.Predicate;

public class URLStreamHandlerFactoryImpl implements URLStreamHandlerFactory {
    private static URLStreamHandlerFactory defaultFactory = (URLStreamHandlerFactory) AccessibleClass.of(URL.class).staticField("defaultFactory").get(null);
    private final Predicate<URL> fliter;

    public URLStreamHandlerFactoryImpl(Predicate<URL> fliter) {
        this.fliter = fliter;
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        return new URLStreamHandlerImpl(defaultFactory.createURLStreamHandler(protocol),fliter);
    }
}
