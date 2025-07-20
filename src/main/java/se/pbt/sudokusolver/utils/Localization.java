package se.pbt.sudokusolver.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import static se.pbt.sudokusolver.utils.Constants.PathConstants.BUNDLE_SV;

/**
 * Loads and manages localized messages using resource bundles based on the selected locale.
 * Supports dynamic switching between languages like Swedish, English, and Spanish.
 */
public class Localization {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_SV);
    private static ResourceBundle bundle = BUNDLE;

    /**
     * Sets the current locale using a {@link Locale} object.
     *
     * @param locale The locale to apply (e.g., new Locale("sv") for Swedish).
     */
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    /**
     * Retrieves the localized string for the specified key, optionally formatting it with arguments.
     *
     * @param key  The message key defined in the bundle.
     * @param args Optional values to insert into the message string.
     * @return The localized and formatted message.
     */
    public static String get(String key, Object... args) {
        String message = bundle.getString(key);
        return args.length > 0 ? MessageFormat.format(message, args) : message;
    }
}
