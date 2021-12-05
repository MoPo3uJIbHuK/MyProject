package files.repository;

import files.models.FileDescriptor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDescriptorRepository extends CrudRepository<FileDescriptor,Integer> {
}
