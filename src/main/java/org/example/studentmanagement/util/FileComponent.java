package org.example.studentmanagement.util;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FileComponent {

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    public byte[] getImage(String picName) throws IOException {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }
}
