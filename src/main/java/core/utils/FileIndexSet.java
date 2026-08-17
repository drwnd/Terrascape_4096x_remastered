package core.utils;

public final class FileIndexSet<T> {

    public FileIndexSet(T[] values, ObjectToFileNameMapper<T> mapper, String suffix) {
        this.values = values;
        this.suffix = suffix;
        this.mapper = mapper;
    }

    public String getFileName(int index) {
        return mapper.getFileNameFor(values[index]) + suffix;
    }

    public int getCount() {
        return values.length;
    }

    private final T[] values;
    private final String suffix;
    private final ObjectToFileNameMapper<T> mapper;

    public interface ObjectToFileNameMapper<T> {
        String getFileNameFor(T object);
    }
}
