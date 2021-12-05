package files.utils;

import files.models.FileDescriptor;
import files.repository.FileDescriptorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class ListFileDescriptorHiber {
    private FileDescriptorRepository fileRepository;
    private String pathFolder;

    @Autowired
    public ListFileDescriptorHiber(FileDescriptorRepository fileRepository,@Value("${folder.path}") String pathFolder){
        this.fileRepository=fileRepository;
        this.pathFolder = pathFolder;
    }

    public List<FileDescriptor> getFiles(){
        Iterable<FileDescriptor> iterable = fileRepository.findAll();
        List<FileDescriptor> files = new ArrayList<>();
        iterable.forEach(files::add);
        try {
            if (!Files.exists(Path.of(pathFolder))) {
                Files.createDirectories(Path.of(pathFolder));
            }
            Files.walk(Path.of(pathFolder)).filter(Files::isRegularFile).forEach(p -> {
                String extension = getExtension(p.toString());
                String fullName = p.getFileName().toString();
                String name = !extension.equals("none") ? fullName.substring(0, fullName.lastIndexOf(extension) - 1) : fullName;
                Long size = getSize(p);
                LocalDateTime createdTime = getDateCreate(p);
                FileDescriptor file = new FileDescriptor(name, extension, size, createdTime);
                if (!files.contains(file)) {
                    fileRepository.save(file);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    private String getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("none");
    }

    private Long getSize(Path file) {
        try {
            return Files.size(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    private LocalDateTime getDateCreate(Path file) {
        try {
            FileTime fileTime = (FileTime) Files.getAttribute(file, "creationTime");
            return LocalDateTime
                    .ofInstant(fileTime.toInstant(), ZoneId.of("Europe/Moscow"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LocalDateTime.now().minusYears(2000);
    }

}
