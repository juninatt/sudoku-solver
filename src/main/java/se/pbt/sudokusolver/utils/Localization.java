package se.pbt.sudokusolver.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Localization {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(Constants.FilePaths.MESSAGE_BUNDLE_SV);

    public static String get(String key, Object... args) {
        String message = BUNDLE.getString(key);
        return args.length > 0 ? MessageFormat.format(message, args) : message;
    }
}
