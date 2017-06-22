package de.phisad.tag.extractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

public class ImageLabelFileCreatorTest {

    private static final File IMAGE = new File(ImageLabelFileCreatorTest.class.getResource("test.JPG").getFile());

    @Test
    @Ignore
    public void createLabelFile_WithOneFile_IsNotEmpty() throws Exception {
        String dirPath = IMAGE.getParentFile().getAbsolutePath();

        ImageLabelFileCreator extractor = new ImageLabelFileCreator("test");
        extractor.createLabelFiles(dirPath);
        Collection<ImageLabelFile> files = extractor.getLabelFiles().getFiles();
        assertThat(files).hasSize(3); // empty ones are also included

        ImageLabelFiles labelFiles = extractor.getLabelFiles();
        ImageLabelFile labelFile = labelFiles.getRoot();
        assertTrue(labelFile.hasKeywords("tag1", "tag2", "tag3"));
    }

    @Test
    public void createLabelFile_WithDeepStructure_IsNotEmpty() throws Exception {
        String dirPath = IMAGE.getParentFile().getAbsolutePath();

        ImageLabelFileCreator extractor = new ImageLabelFileCreator();
        extractor.createLabelFiles(dirPath);
        Collection<ImageLabelFile> files = extractor.getLabelFiles().getFiles();
        assertThat(files).hasSize(3);

        ImageLabelFiles labelFiles = extractor.getLabelFiles();
        ImageLabelFile labelFile = labelFiles.getRoot();
        assertTrue(labelFile.hasKeywords("tag1", "tag2", "tag3", "deep1", "deep2"));
    }

}
