package com.deyrann.demo.controller;

import com.deyrann.demo.entity.Courses;
import com.deyrann.demo.service.CoursesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CoursesController {

    private final CoursesService coursesService;

    @GetMapping
    public ResponseEntity<List<Courses>> getAll() {
        List<Courses> list = coursesService.getAllCourses();
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courses> getById(@PathVariable Long id) {
        Courses course = coursesService.getCourseById(id);
        if (course == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(course);
    }

    @PostMapping
    public ResponseEntity<Courses> create(@RequestBody Courses course) {
        Courses saved = coursesService.createCourse(course);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Courses> update(@PathVariable Long id, @RequestBody Courses course) {
        Courses updated = coursesService.updateCourse(id, course);
        if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = coursesService.deleteCourse(id);
        if (!deleted) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.noContent().build();
    }
}
