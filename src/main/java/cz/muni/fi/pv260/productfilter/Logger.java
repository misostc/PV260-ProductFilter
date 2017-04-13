package cz.muni.fi.pv260.productfilter;

public interface Logger {

    /**
     * Toggle severity level. All subsequent messages
     * (until this method is called again) will have this level.
     * @param level supported levels are 'INFO', 'WARN', 'ERROR'
     */
    void setLevel(String level);

    /**
     * @param tag can be any string, log messages can be filtered by tag.
     *            Class of the source is generally a good tag
     */
    void log(String tag, String message);

}
