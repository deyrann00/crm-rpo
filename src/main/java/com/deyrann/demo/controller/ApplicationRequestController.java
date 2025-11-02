package com.deyrann.demo.controller;

import com.deyrann.demo.entity.ApplicationRequest;
import com.deyrann.demo.service.ApplicationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class ApplicationRequestController {

    private final ApplicationRequestService requestService;

    @GetMapping
    public ResponseEntity<List<ApplicationRequest>> getAll() {
        List<ApplicationRequest> list = requestService.getAllRequests();
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationRequest> getById(@PathVariable Long id) {
        ApplicationRequest r = requestService.getRequestById(id);
        if (r == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(r);
    }

    @PostMapping
    public ResponseEntity<ApplicationRequest> create(@RequestBody ApplicationRequest request) {
        ApplicationRequest saved = requestService.createRequest(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationRequest> update(@PathVariable Long id, @RequestBody ApplicationRequest request) {
        ApplicationRequest updated = requestService.updateRequest(id, request);
        if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = requestService.deleteRequest(id);
        if (!deleted) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.noContent().build();
    }
}
