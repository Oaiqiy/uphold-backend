package dev.oaiqiy.uphold.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LocalFileService implements FileService{
    private final String root = "/uphold";


    @Override
    public boolean save(MultipartFile file, String filename, String... paths) {
        Path path = Path.of(root,paths);

        File folder = path.toFile();
        folder.mkdir();

        path = path.resolve(filename);
        File target = path.toFile();

        target.delete();

        try {
            file.transferTo(target);
        } catch (IOException e) {
            return false;
        }

        return  true;

    }

    @Override
    public boolean delete(String... path) {
        File file = Path.of(root,path).toFile();
        if(!file.exists())
            return false;

        if(file.isFile()){
            file.delete();
        }else {
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(File::delete);
        }

        return true;
    }




    @Override
    public Resource load( String name,String... path) {
        File file = Path.of(root,path).resolve(name).toFile();

        try {
            return new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
           return null;
        }
    }

    @Override
    public List<Resource> loadAll(String... path) {
        File dir = Path.of(root,path).toFile();


        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).map(f -> {
            try {
                return new UrlResource(f.toURI());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

    }

    @Override
    public String getFileExtension(String filename) {
        int index = filename.lastIndexOf(".");
        if(index != -1 && index != 0)
            return filename.substring(index+1);
        else
            return null;
    }

    @Override
    public String changeFilename(String filename) {
        String extension = getFileExtension(filename);
        if(extension != null)
            return filename+extension;
        else
            return filename;
    }
}
