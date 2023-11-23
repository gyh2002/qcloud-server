package com.orange.qcloud.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class FileUtils {
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            return file.createNewFile();
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public static boolean createDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            return dir.mkdirs();
        } else {
            return false;
        }
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    public static boolean deleteDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file.getAbsolutePath());
                    } else {
                        file.delete();
                    }
                }
            }
            return dir.delete();
        } else {
            return false;
        }
    }

    public static boolean rename(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (oldFile.exists()) {
            return oldFile.renameTo(newFile);
        } else {
            return false;
        }
    }

    public static boolean updateFileContent(String filePath, String content) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public static String readFileContent(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getFileHash(String filePath) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] bytesBuffer = new byte[1024];
            try (InputStream inputStream = java.nio.file.Files.newInputStream(Paths.get(filePath))) {
                int bytesRead;
                while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                    digest.update(bytesBuffer, 0, bytesRead);
                }
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        byte[] hashedBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static String getFileNameByStringPath(String path) {
        int index = Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/"));
        return path.substring(index + 1);
    }
}
