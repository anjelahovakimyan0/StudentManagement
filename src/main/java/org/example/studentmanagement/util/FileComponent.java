package org.example.studentmanagement.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@UtilityClass
public class FileComponent {

    public static byte[] getImage(String picName, String uploadDirectory) throws IOException {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }
}
