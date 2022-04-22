package dev.oaiqiy.uphold.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.List;
@Component
@Slf4j
public class LocalFileService implements FileService{
    private final String root = "/uphold";

    @Override
    public boolean save(String pathString, File file) {
        return save(pathString,file, file.getName());
    }

    @Override
    public boolean save(String pathString, File file, String name) {
        Path path = Path.of(root);
        path.resolve(pathString);
        File folder = path.toFile();
        path.resolve(name);
        File target = path.toFile();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(target);
            fileOutputStream.write(fileInputStream.readAllBytes());
            fileOutputStream.close();
            fileInputStream.close();

        } catch (IOException e) {
            log.warn("save file : " + target.getPath() + "error.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean save(String pathString, InputStream fileInputStream, String name) {
        Path path = Path.of(root);
        path = path.resolve(pathString).resolve(name);

        File target = path.toFile();
        if(target.exists())
            target.delete();


        try {
            target.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(target);
            fileOutputStream.write(fileInputStream.readAllBytes());
            fileOutputStream.close();
            fileInputStream.close();

        } catch (IOException e) {
            log.warn("save file : " + target.getPath() + "error.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean save(String pathString, MultipartFile file, String name) {
        Path path = Path.of(root);
        path = path.resolve(pathString).resolve(name);
        File folder = path.toFile();
        folder.mkdir();

        for(var f : folder.listFiles())
            f.delete();

        path = path.resolve(file.getOriginalFilename());
        File target = path.toFile();

        if(target.exists())
            target.delete();

        try {
            file.transferTo(target);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  true;

    }

    @Override
    public File load(String path, String name) {
        return null;
    }

    @Override
    public List<File> loadAll(String path) {
        return null;
    }
}
