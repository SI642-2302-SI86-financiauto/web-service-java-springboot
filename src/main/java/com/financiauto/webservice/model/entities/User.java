package com.financiauto.webservice.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "first_name" )
    private String firstName;
    @NotNull
    @Column(name = "last_name" )
    private String lastName;
    @NotNull
    @Column(name = "email" )
    private String email;
    @NotNull
    @Column(name = "password")
    private String password;
    @Column(name = "date_of_birth" )
    private Date dateOfBirth;
}
