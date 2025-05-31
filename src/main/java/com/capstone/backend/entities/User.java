package com.capstone.backend.entities;


import com.capstone.backend.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email" , unique = true )
    private String email;

    @Column(length = 10)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(length = 1000)
    private String about;

    @Column(name = "user_imageName")
    private String imageName;

}
