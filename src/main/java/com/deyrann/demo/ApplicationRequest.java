package com.deyrann.demo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "commentary")
    private String commentary;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_handled")
    private boolean handled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Courses course;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Operators> operators;
}