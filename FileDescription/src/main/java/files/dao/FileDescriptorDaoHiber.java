package files.dao;

import files.models.FileDescriptor;
import files.repository.FileDescriptorRepository;
import files.utils.ListFileDescriptorHiber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component("filesDaoHiber")
public class FileDescriptorDaoHiber implements FileDescriptorDao {
    private FileDescriptorRepository fileRepository;
    private String pathFolder;
    private ListFileDescriptorHiber listFileDescriptorHiber;

    @Autowired
    public FileDescriptorDaoHiber(FileDescriptorRepository fileRepository, @Value("${folder.path}") String pathFolder, ListFileDescriptorHiber listFileDescriptorHiber) {
        this.fileRepository = fileRepository;
        this.pathFolder = pathFolder;
        this.listFileDescriptorHiber=listFileDescriptorHiber;
    }

    @Override
    public List<FileDescriptor> getFiles() {
        List<FileDescriptor> listFiles = listFileDescriptorHiber
                .getFiles();
        return listFiles;
    }

    @Override
    public FileDescriptor getFile(Integer id) {
        return getFiles().stream().filter(file -> file.getId().equals(id)).findAny().get();
    }

    @Override
    public void deleteFile(Integer id) {
        FileDescriptor fileDesc = getFile(id);
        String fullName = pathFolder + fileDesc.getFileName() + (fileDesc.getExtension().equals("none") ? "" : "." + fileDesc.getExtension());
        Path pathFile = Path.of(fullName);
        try {
            Files.deleteIfExists(pathFile);
            fileRepository.delete(fileDesc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFile(MultipartFile file) {
        if (!file.isEmpty()) {
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pathFolder + file.getOriginalFilename()))) {
                byte[] bytes = file.getBytes();
                bos.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
