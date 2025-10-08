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

    @Column(name = "user_name", nullable = false)
    private String userName = ""; // Initialize to empty string

    @Column(name = "course_name")
    private String courseName = ""; // Initialize to empty string

    @Column(columnDefinition = "TEXT")
    private String commentary = ""; // Initialize to empty string

    @Column(name = "phone")
    private String phone = ""; // Initialize to empty string

    @Column(name = "is_handled")
    private boolean handled = false; // Set default explicitly

    // Custom constructor for new requests
    public ApplicationRequest(String userName, String courseName, String commentary, String phone) {
        this.userName = userName;
        this.courseName = courseName;
        this.commentary = commentary;
        this.phone = phone;
        this.handled = false;
    }
}