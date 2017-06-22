package de.phisad.tag.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
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

    private final File file;

    private final Map<String, Collection<String>> name2labels = new HashMap<>();

    public ImageLabelFile(File directory) {
        file = new File(directory, "label.csv");
    }

    public void addLabel(File file, List<String> labels) {
        addLabel(file.getName(), labels);
    }

    public void addLabel(String fileName, List<String> labels) {
        name2labels.put(fileName, labels);
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
        return name2labels.values().contains(keyword);
    }

    public void write() throws IOException {
        final Collection<String> lines = createLines();
        FileUtils.writeLines(file, lines);
    }

    Collection<String> createLines() {
        final Collection<String> lines = new ArrayList<>();
        for (Entry<String, Collection<String>> entry : name2labels.entrySet()) {
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

}
