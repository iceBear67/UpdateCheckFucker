package io.ib67;

import io.ib67.util.reflection.AccessibleClass;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class URLStreamHandlerImpl extends URLStreamHandler {
    private final URLStreamHandler wrapped;
    private final Predicate<URL> filter;

    private static final MethodHandle OPEN_CONNECTION_URL=AccessibleClass.of(URLStreamHandler.class).method("openConnection",MethodType.methodType(URLConnection.class, URL.class));;
    private static final MethodHandle OPEN_CONNECTION_URL_PROXY = AccessibleClass.of(URLStreamHandler.class).method("openConnection", MethodType.methodType(URLConnection.class, URL.class, Proxy.class));

    public URLStreamHandlerImpl(URLStreamHandler wrapped, Predicate<URL> filter) {
        this.wrapped = wrapped;
        this.filter = filter;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        // do fliter things
        if(filter.test(u)){
            try {
                return (URLConnection) OPEN_CONNECTION_URL.invokeExact(wrapped,u);
            } catch (Throwable e) {
                throw new IOException(e);
            }
        }
        Logger.getGlobal().info("Blocked: "+u);
        throw new IOException("Filtered by UpdateCheckFucker");
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        // do fliter things
        if(filter.test(u)){
            try {
                return (URLConnection) OPEN_CONNECTION_URL_PROXY.invokeExact(wrapped,u,p);
            } catch (Throwable e) {
                throw new IOException(e);
            }
        }
        Logger.getGlobal().info("Blocked: "+u);
        throw new IOException("Filtered by UpdateCheckFucker");
    }
}
