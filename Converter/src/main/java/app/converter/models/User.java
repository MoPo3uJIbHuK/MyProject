package app.converter.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "username")
    @Size(min=3, max = 64)
    private String username;
    @Column(name = "password")
    @Size(min = 3,max=64)
    private String password;
    private String role;

}
