package com.deyrann.demo.service;

import com.deyrann.demo.repository.*;
import com.deyrann.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoursesService {

    @Autowired
    private CoursesRepository coursesRepository;

    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }

    public Courses getCourseById(Long id) {
        Optional<Courses> opt = coursesRepository.findById(id);
        return opt.orElse(null);
    }

    public Courses createCourse(Courses course) {
        return coursesRepository.save(course);
    }

    public Courses updateCourse(Long id, Courses updated) {
        Optional<Courses> opt = coursesRepository.findById(id);
        if (opt.isEmpty()) return null;

        Courses c = opt.get();
        c.setName(updated.getName());
        c.setDescription(updated.getDescription());
        c.setPrice(updated.getPrice());
        return coursesRepository.save(c);
    }

    public boolean deleteCourse(Long id) {
        if (!coursesRepository.existsById(id)) return false;
        coursesRepository.deleteById(id);
        return true;
    }
}
