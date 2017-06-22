package de.phisad.tag.extractor;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class ImageLabelFileTest {

    @Test
    public void createLine() {
        ImageLabelFile labelFile = new ImageLabelFile(new File(""));
        labelFile.addLabel("Name", Arrays.asList("Label1", "Label2"));

        Collection<String> lines = labelFile.createLines();
        assertThat(lines).hasSize(1);
        assertThat(lines).contains("Name,Label1,Label2");
    }

}
