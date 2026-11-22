package dev.tauri.jsgtransporters.common.helpers;
import java.lang.UnsupportedOperationException;

public class UntransportableBlockException extends UnsupportedOperationException {
    public UntransportableBlockException(String message) {
        super(message);
    }
}
