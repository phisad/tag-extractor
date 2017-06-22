package de.phisad.tag.extractor;

import java.io.File;

import org.junit.Test;

public class ImageLabelFileExtractorTest {

    private static final File IMAGE = new File(ImageLabelFileCreatorTest.class.getResource("SUNP0331.JPG").getFile());

    @Test
    public void extract() throws Exception {
        String dirPath = IMAGE.getParentFile().getAbsolutePath();

        new ImageLabelFileExtractor().extract(dirPath);
    }

}
