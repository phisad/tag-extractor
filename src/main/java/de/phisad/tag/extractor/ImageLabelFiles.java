package de.phisad.tag.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImageLabelFiles {

    private final Collection<ImageLabelFile> files = new ArrayList<>();

    // Keeping the structure
    private final Map<File, ImageLabelFile> directory2labels = new HashMap<>();

    public void next(File directory) {
        if (isEmpty()) {
            addRoot(directory);
        } else {
            addChild(directory);
        }
    }

    private void addChild(File directory) {
        final ImageLabelFile parentLabelFile = getParent(directory);
        final ImageLabelFile labelFile = new ImageLabelFile(directory, parentLabelFile);
        addLabelFile(directory, labelFile);
    }

    private void addLabelFile(File directory, final ImageLabelFile labelFile) {
        files.add(labelFile);
        directory2labels.put(directory, labelFile);
    }

    private ImageLabelFile getParent(File directory) {
        File parentFile = directory.getParentFile();
        return directory2labels.get(parentFile);
    }

    private void addRoot(File directory) {
        ImageLabelFile labelFile = new ImageLabelFile(directory);
        addLabelFile(directory, labelFile);
    }

    private boolean isEmpty() {
        return files.isEmpty();
    }

    public void addLabels(File file, Collection<String> labels) {
        ImageLabelFile labelFile = getLabelFile(file);
        labelFile.addLabels(file, labels);
    }

    private ImageLabelFile getLabelFile(File file) {
        File directory = file.getParentFile();
        return directory2labels.get(directory);
    }

    public ImageLabelFile getRoot() {
        for (ImageLabelFile imageLabelFile : files) {
            if (imageLabelFile.isRoot()) {
                return imageLabelFile;
            }
        }
        return null;
    }

    public Collection<ImageLabelFile> getFiles() {
        return Collections.unmodifiableCollection(files);
    }

    public void writeLabelFile(File directory) throws IOException {
        directory2labels.get(directory).write();
    }

}
