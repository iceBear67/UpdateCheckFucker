package io.ib67.ucf;

public class BlockerConfig {
    private final boolean onlyMainThread;
    private final Matcher matcher;
    private final boolean verbose;

    public BlockerConfig(boolean onlyMainThread, Matcher matcher, boolean verbose) {
        this.onlyMainThread = onlyMainThread;
        this.matcher = matcher;
        this.verbose = verbose;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public boolean isOnlyMainThread() {
        return onlyMainThread;
    }

    public boolean isVerbose() {
        return verbose;
    }
}
