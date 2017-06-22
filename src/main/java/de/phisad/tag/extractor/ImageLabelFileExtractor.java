package de.phisad.tag.extractor;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ImageLabelFileExtractor {

    public void extract(String aDirectory) throws IOException {
        final Collection<ImageLabelFile> labelFiles = new ImageLabelFileCreator().createLabelFiles(aDirectory);
        for (ImageLabelFile labelFile : labelFiles) {
            labelFile.write();
        }
    }

    public static void main(String[] args) throws Exception {

        final Options options = new Options();
        options.addOption(Option.builder("dir").argName("directory").required().hasArg().build());

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);

        final String dir = cmd.getOptionValue("dir");
        new ImageLabelFileExtractor().extract(dir);
    }

}
