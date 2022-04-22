package dev.oaiqiy.uphold.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface FileService {
    boolean save(String path, File file);
    boolean save(String path, File file, String name);
    boolean save(String path, InputStream inputStream,String name);
    boolean save(String pathString, MultipartFile file,String name);
    File load(String path,String name);
    List<File> loadAll(String path);
}
