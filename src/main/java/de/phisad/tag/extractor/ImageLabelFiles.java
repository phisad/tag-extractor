package de.phisad.tag.extractor;

import java.io.File;
import java.util.LinkedList;

public class ImageLabelFiles {

    private LinkedList<ImageLabelFile> files = new LinkedList<>();

    public void next(File directory) {
        files.add(new ImageLabelFile(directory));
    }

    public ImageLabelFile get() {
        return files.getLast();
    }


}
