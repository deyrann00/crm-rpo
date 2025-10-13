package com.deyrann.demo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "crm_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName = "";

    @Column(name = "course_name")
    private String courseName = "";

    @Column(columnDefinition = "TEXT")
    private String commentary = "";

    @Column(name = "phone")
    private String phone = "";

    @Column(name = "is_handled")
    private boolean handled = false;
}