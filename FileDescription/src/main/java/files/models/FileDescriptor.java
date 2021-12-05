package files.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDescriptor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "file_name")
    private String fileName;
    private String extension;
    private Long size;
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    public FileDescriptor(String fileName, String extension, Long size, LocalDateTime createdTime) {
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDescriptor that = (FileDescriptor) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(extension, that.extension)
                && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, extension, size, createdTime);
    }
}
