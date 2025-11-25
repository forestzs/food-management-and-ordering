package com.itheima;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class FileUploadTest {
    @Test
    public void updateFileName() {
        String originalFileName = "132123213.jpg";
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String randomFileName = UUID.randomUUID().toString();
        String newFileName = randomFileName + suffix;
        System.out.println("newFileName = " + newFileName);
    }
}
