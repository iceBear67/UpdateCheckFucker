package io.ib67.ucf;

import io.ib67.ucf.exception.UpdateException;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

public class DelegatedProxySelector extends ProxySelector {
    private final ProxySelector selector;
    private final BlockerConfig config;

    public DelegatedProxySelector(ProxySelector selector, BlockerConfig config) {
        this.selector = selector;
        this.config = config;
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (uri.toString().startsWith("socket")) {
            return selector.select(uri);
        }
        if (config.isVerbose()) Bukkit.getLogger().warning("URL: " + uri);
        if (config.getMatcher().is(uri)) {
            if (config.isVerbose()) Bukkit.getLogger().warning("Checked");
            if (config.isOnlyMainThread()) {
                if (Bukkit.isPrimaryThread()) throw new UpdateException("Connection is blocked by UpdateCheckerFucker, url:"+uri);
            } else {
                throw new UpdateException("Connection is blocked by UpdateCheckerFucker, url: "+uri);
            }
        }
        return selector.select(uri);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        selector.connectFailed(uri, sa, ioe);
    }
}
