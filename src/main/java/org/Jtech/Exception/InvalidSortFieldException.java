package org.Jtech.Exception;

import java.util.Collection;

public class InvalidSortFieldException extends RuntimeException {
    public InvalidSortFieldException(String field, Collection<String> allowed) {
        super("Invalid sort field '" + field + "'. Allowed: " + String.join(", ", allowed));
    }
}
