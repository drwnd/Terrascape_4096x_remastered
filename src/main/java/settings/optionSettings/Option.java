package settings.optionSettings;

/**
 * Must only be implemented by an Enum.
 *
 * @param <T> The Enum implementing this.
 */
public interface Option<T> {

    // Side note for my future self : WHY and HOW the actual fuck does this work?

    default Option<T> next() {
        Enum<?> thisEnum = (Enum<?>) this;
        Option<T>[] enumConstants = getClass().getEnumConstants();

        return enumConstants[(thisEnum.ordinal() + 1) % enumConstants.length];
    }

    default Option<T> value(String name) {
        Enum<?> thisEnum = (Enum<?>) this;

        return (Option<T>) Enum.valueOf(thisEnum.getClass(), name);
    }

    default String name() {
        return ((Enum<?>) this).name();
    }
}
