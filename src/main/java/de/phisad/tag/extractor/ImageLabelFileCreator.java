package de.phisad.tag.extractor;

import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.prefixFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.suffixFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.lang3.StringUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.iptc.IptcDirectory;

/**
 * Create label files from image meta data.
 * <p>
 * Notice: Does not write the actual files to the file system!
 * 
 * @author Philipp
 *
 */
public class ImageLabelFileCreator extends DirectoryWalker<ImageLabelFile> {

    private static final int INFINITE_DEPTH = -1;

    private ImageLabelFiles labelFiles = new ImageLabelFiles();

    private boolean trace = false;

    private int currentFileCount;

    private int directoryFileCount;

    /**
     * Configuration:
     * 
     * <pre>
     *   - visible directories
     *   - JPG files
     *   - recursive
     * </pre>
     */
    public ImageLabelFileCreator() {
        super(HiddenFileFilter.VISIBLE, suffixFileFilter(".JPG", IOCase.INSENSITIVE), INFINITE_DEPTH);
    }

    /**
     * Configuration:
     * 
     * <pre>
     *   - visible directories
     *   - JPG files
     *   - recursive
     * </pre>
     * 
     * @param aFileName
     *            of a specific image
     */

    public ImageLabelFileCreator(String aFileName) {
        super(
                HiddenFileFilter.VISIBLE,
                and(suffixFileFilter(".JPG", IOCase.INSENSITIVE), prefixFileFilter(aFileName, IOCase.INSENSITIVE)),
                INFINITE_DEPTH);
    }

    /**
     * 
     * @param aDirectory
     *            to start from recursevily
     * @return the list of created label files
     * @throws IOException
     *             in case of problems
     */
    public Collection<ImageLabelFile> createLabelFiles(String aDirectoryPath) throws IOException {
        return createLabelFiles(new File(aDirectoryPath));
    }

    /**
     * 
     * @param aDirectory
     *            to start from recursevily
     * @return the list of created label files
     * @throws IOException
     *             in case of problems
     */
    public Collection<ImageLabelFile> createLabelFiles(File aDirectory) throws IOException {
        Collection<ImageLabelFile> results = new ArrayList<ImageLabelFile>();
        walk(aDirectory, results);
        return results;
    }

    @Override
    protected void handleFile(File file, int depth, Collection<ImageLabelFile> results) throws IOException {
        try {
            if (trace) {
                System.out.println("Handle file '" + file.getName() + "' ...");
            }
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            IptcDirectory iptcDirectory = metadata.getFirstDirectoryOfType(IptcDirectory.class);
            if (iptcDirectory == null) {
                labelFiles.get().addLabel(file, Collections.<String>emptyList());
            } else {
                List<String> keywords = iptcDirectory.getKeywords();
                labelFiles.get().addLabel(file, keywords);
            }
            currentFileCount++;
            displayProgress();
        } catch (ImageProcessingException e) {
            throw new IOException(e);
        }
    }

    private void displayProgressEnd() {
        StringBuilder builder = new StringBuilder("[");
        for (int part = 0; part < 100; part++) {
            builder.append("#");
        }
        builder.append("]");

        String progress = builder.toString();
        System.out.print("\r" + progress + "\n");
    }

    private void displayProgress() {
        double percent = ((float) currentFileCount / directoryFileCount) * 100;
        int parts = (int) percent;
        StringBuilder builder = new StringBuilder("[");
        for (int part = 0; part < parts; part++) {
            builder.append("#");
        }
        int remaining = 100 - parts;
        for (int part = 0; part < remaining; part++) {
            builder.append(" ");
        }
        builder.append("]");

        String progress = builder.toString();
        System.out.print("\r" + progress);
    }

    @Override
    protected void handleDirectoryStart(File directory, int depth, Collection<ImageLabelFile> results)
            throws IOException {
        labelFiles.next(directory);
        System.out.println("Scan directory '" + directory.getName() + "' ...");
        directoryFileCount = directory.listFiles((FileFilter) HiddenFileFilter.VISIBLE).length;
        currentFileCount = 0;
    }

    @Override
    protected void handleDirectoryEnd(File directory, int depth, Collection<ImageLabelFile> results)
            throws IOException {
        results.add(labelFiles.get());
        displayProgressEnd();
    }

    public ImageLabelFiles getLabelFiles() {
        return labelFiles;
    }

}
