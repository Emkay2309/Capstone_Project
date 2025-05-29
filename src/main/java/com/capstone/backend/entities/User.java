package com.capstone.backend.entities;


import com.capstone.backend.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    private String imageName;

}
