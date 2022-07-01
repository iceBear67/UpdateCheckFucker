package io.ib67;

import io.ib67.exception.UpdateException;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class UpdateErrorFilter implements Filter {
    private final Filter anotherFilter;

    public UpdateErrorFilter(Filter anotherFilter) {
        this.anotherFilter = anotherFilter == null ? r -> true : anotherFilter;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        if (record.getThrown() instanceof UpdateException || record.getMessage().contains(UpdateException.class.getName())) {
            return false;
        }
        return anotherFilter.isLoggable(record);
    }
}
