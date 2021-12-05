package files.dao;

import files.models.FileDescriptor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component("filesDao")
public interface FileDescriptorDao {

    List<FileDescriptor> getFiles();

    FileDescriptor getFile(Integer id);

    void deleteFile(Integer id);

    void addFile(MultipartFile file);

}
