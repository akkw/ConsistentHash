package com.akka.consistenthash;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CreateKey {
    private String path = System.getProperty("user.dir") + "/src/test/resources/";


    public CreateKey() throws IOException {
    }

    @Test
    public void createKeyLength5Size1000() throws IOException {
        Set<String> keys = new HashSet<>();
        for (int i = 0; keys.size() < 1000 ; i++) {
            keys.add(RandomCharData.createRandomCharData(5));
        }
        String fileName = path + "key_5_1000.txt";
        FileWriter fileWriter = new FileWriter(fileName, false);
        IOUtils.writeLines(keys, "\n", fileWriter);
        fileWriter.flush();
    }
    @Test
    public void createKeyLength10Size1000() throws IOException, InterruptedException {
        Set<String> keys = new HashSet<>();
        for (int i = 0; keys.size() < 1000 ; i++) {
            keys.add(RandomCharData.createRandomCharData(10));
        }
        String fileName = path + "key_10_1000.txt";
        FileWriter fileWriter = new FileWriter(fileName, false);
        IOUtils.writeLines(keys, "\n", fileWriter);
        fileWriter.flush();
    }

    @Test
    public void createKeyLength15Size1000() throws IOException {
        Set<String> keys = new HashSet<>();
        for (int i = 0; keys.size() < 1000 ; i++) {
            keys.add(RandomCharData.createRandomCharData(15));
        }
        String fileName = path + "key_15_1000.txt";
        FileWriter fileWriter = new FileWriter(fileName, false);
        IOUtils.writeLines(keys, "\n", fileWriter);
        fileWriter.flush();
    }

    @Test
    public void createKeyLength20Size1000() throws IOException {
        Set<String> keys = new HashSet<>();
        for (int i = 0; keys.size() < 1000 ; i++) {
            keys.add(RandomCharData.createRandomCharData(20));
        }
        String fileName = path + "key_20_1000.txt";
        FileWriter fileWriter = new FileWriter(fileName, false);
        IOUtils.writeLines(keys, "\n", fileWriter);
        fileWriter.flush();
    }
}
