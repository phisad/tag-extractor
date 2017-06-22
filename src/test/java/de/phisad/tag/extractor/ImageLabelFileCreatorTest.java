package de.phisad.tag.extractor;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

public class ImageLabelFileCreatorTest {

    private static final File IMAGE = new File(ImageLabelFileCreatorTest.class.getResource("test.JPG").getFile());

    @Test
    public void createLabelFile_WithOneFile_IsNotEmpty() throws Exception {
        String dirPath = IMAGE.getParentFile().getAbsolutePath();

        ImageLabelFileCreator extractor = new ImageLabelFileCreator("test");
        Collection<ImageLabelFile> files = extractor.createLabelFiles(dirPath);
        assertThat(files).hasSize(1);

        ImageLabelFiles labelFiles = extractor.getLabelFiles();
        ImageLabelFile labelFile = labelFiles.get();
        labelFile.hasKeywords("Night", "Left_to_right", "Hare", "Alone", "Run");
    }

}
