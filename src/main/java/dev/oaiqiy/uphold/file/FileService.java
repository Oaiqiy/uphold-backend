package dev.oaiqiy.uphold.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileService {

    boolean save(MultipartFile file,String filename, String... paths);

    boolean delete(String... path);

    Resource load(String name, String... path);

    List<Resource> loadAll(String... path);

    String getFileExtension(String filename);
    String changeFilename(String filename);
}
