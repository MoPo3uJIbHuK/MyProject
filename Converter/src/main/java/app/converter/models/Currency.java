package app.converter.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
@Getter@Setter
@ToString
@AllArgsConstructor
public class Currency {
    @Id
    private String id;
    @Column(name = "char_code")
    private String charCode;
    private String name;
}
