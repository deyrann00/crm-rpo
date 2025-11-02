package com.deyrann.demo.controller;

import com.deyrann.demo.entity.ApplicationRequest;
import com.deyrann.demo.entity.Operators;
import com.deyrann.demo.service.ApplicationRequestService;
import com.deyrann.demo.service.OperatorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operators")
@RequiredArgsConstructor
public class OperatorsController {

    private final OperatorsService operatorsService;
    private final ApplicationRequestService requestService;

    @GetMapping
    public ResponseEntity<List<Operators>> getAll() {
        List<Operators> list = operatorsService.getAllOperators();
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Operators> create(@RequestBody Operators operator) {
        Operators saved = operatorsService.createOperator(operator);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{operatorId}/assign/{requestId}")
    public ResponseEntity<ApplicationRequest> assignOperator(@PathVariable Long operatorId, @PathVariable Long requestId) {
        ApplicationRequest updated = requestService.assignOperator(requestId, operatorId);
        if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(updated);
    }
}
