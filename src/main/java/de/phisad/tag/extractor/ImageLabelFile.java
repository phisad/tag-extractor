package de.phisad.tag.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * CSV file with file name and label pairs.
 * <p>
 * Notice: Labels are not sorted for now
 * 
 * @author Philipp
 *
 */
public class ImageLabelFile {

    private static final ImageLabelFile NONE = null;

    private static final Comparator<String> PATH_COMPARATOR = new Comparator<String>() {

        @Override
        public int compare(String o1, String o2) {
            String[] path1 = StringUtils.split(o1, File.separator);
            String[] path2 = StringUtils.split(o2, File.separator);
            if (path1.length == path2.length) {
                return o1.compareTo(o2);
            }
            if (path1.length > path2.length) {
                // larger path comes at the end
                return 1;
            }
            // smaller path comes at the beginning
            return -1;
        }
    };

    private final File labelFile;

    private final Map<String, Collection<String>> name2labels = new HashMap<>();

    private final ImageLabelFile parent;

    public ImageLabelFile(File directory) {
        this(directory, NONE);
    }

    public ImageLabelFile(File directory, ImageLabelFile parent) {
        this.parent = parent;
        labelFile = new File(directory, "label.csv");
    }

    public void addLabels(File file, Collection<String> labels) {
        propagate(file, labels, 0);
    }

    void addLabels(String fileName, Collection<String> labels) {
        name2labels.put(fileName, labels);
    }

    private void propagate(File file, Collection<String> labels, int levels) {
        final String absolutePath = file.getAbsolutePath();
        final String fileName = FilenameUtils.getName(absolutePath);

        // attach upper directory names when propagating
        final String path = FilenameUtils.getPath(absolutePath);
        final String[] paths = StringUtils.split(path, File.separator);
        String name = fileName;
        for (int level = 1; level <= levels; level++) {
            final String dirName = paths[paths.length - level];
            name = dirName + File.separator + name;
        }

        if (isRoot()) {
            name2labels.put(name, labels);
        } else {
            name2labels.put(name, labels);
            parent.propagate(file, labels, ++levels);
        }
    }

    public boolean hasKeywords(String... aKeywords) {
        for (String keyword : aKeywords) {
            if (!hasKeyword(keyword)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasKeyword(String keyword) {
        for (Collection<String> labels : name2labels.values()) {
            if (labels.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public void write() throws IOException {
        final Collection<String> lines = createLines();
        FileUtils.writeLines(labelFile, lines);
    }

    Collection<String> createLines() {
        final Collection<String> lines = new ArrayList<>();
        final Map<String, Collection<String>> treeMap = new TreeMap<>(PATH_COMPARATOR);
        treeMap.putAll(name2labels);
        for (Entry<String, Collection<String>> entry : treeMap.entrySet()) {
            final String fileName = entry.getKey();
            final Collection<String> labels = entry.getValue();
            if (labels.isEmpty()) {
                lines.add(fileName);
            } else {
                final String line = StringUtils.join(Arrays.asList(fileName, StringUtils.join(labels, ",")), ",");
                lines.add(line);
            }
        }
        return lines;
    }

    public boolean isRoot() {
        return parent == NONE;
    }

    @Override
    public String toString() {
        return labelFile.getPath() + new HashSet<>(name2labels.values());
    }

}
