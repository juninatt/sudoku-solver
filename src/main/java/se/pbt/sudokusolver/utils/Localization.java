package se.pbt.sudokusolver.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Manages all user-facing text for the application by loading messages from ResourceBundle files.
 * These files are located in the 'resources/i18n' directory, and their file paths are defined in the
 * {@link Constants.FilePaths} class.
 * This class handles only the text displayed to the user and enables easy language
 * switching without modifying the underlying code.
 */
public class Localization {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(Constants.FilePaths.MESSAGE_BUNDLE_SV);

    /**
     * Retrieves the text for the given key from the ResourceBundle.
     * If additional arguments are provided, they are used to format the message.
     *
     * @param key  the key identifying the message in the ResourceBundle.
     * @param args optional arguments to format the message.
     * @return the localized and formatted message.
     */
    public static String get(String key, Object... args) {
        String message = BUNDLE.getString(key);
        return args.length > 0 ? MessageFormat.format(message, args) : message;
    }
}
