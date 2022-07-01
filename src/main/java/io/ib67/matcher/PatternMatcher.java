package io.ib67.matcher;

import io.ib67.Matcher;

import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

public class PatternMatcher implements Matcher {
    private final List<Pattern> patterns;

    public PatternMatcher(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public boolean is(URI uri) {
        return patterns.stream().anyMatch(e->e.matcher(uri.toString()).find());
    }
}
