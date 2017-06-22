package de.phisad.tag.extractor;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ImageLabelFiles {

    // Keeping the order
    private final LinkedList<ImageLabelFile> files = new LinkedList<>();

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

}
