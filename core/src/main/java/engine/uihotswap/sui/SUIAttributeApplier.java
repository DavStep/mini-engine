package engine.uihotswap.sui;

@FunctionalInterface
public interface SUIAttributeApplier<T, String> {
    void apply (T target, String value);
}
