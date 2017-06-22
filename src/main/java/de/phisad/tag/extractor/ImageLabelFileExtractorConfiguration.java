package de.phisad.tag.extractor;

// Note in use for now
public class ImageLabelFileExtractorConfiguration {

    private boolean writeImmediately = false;
    private boolean writeTreeLabels = false;
    private boolean writeDirectoryLabels = false;

    /**
     * Setting to write label files immediately after scanning the directory.
     */
    public void writeImmediately() {
        writeImmediately = true;
    }

    /**
     * 
     * @return if to write immediately
     * @see #writeImmediately()
     */
    public boolean isWriteImmediately() {
        return writeImmediately;
    }

    /**
     * Setting to write tree label files containing also labels from
     * sub-directories into each traversed top-level directory.
     */
    public void writeTreeLabels() {
        writeTreeLabels = true;
    }

    /**
     * 
     * @return if to write tree label files
     * @see #isWriteTreeLabels()
     */
    public boolean isWriteTreeLabels() {
        return writeTreeLabels;
    }

    /**
     * Setting to write flat label files into each traversed directory.
     */
    public void writeDirectoryLabels() {
        writeDirectoryLabels = true;
    }

    /**
     * 
     * @return if to write directory labels
     * @see #writeDirectoryLabels()
     */
    public boolean isWriteDirectoryLabels() {
        return writeDirectoryLabels;
    }

}
