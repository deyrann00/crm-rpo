package com.deyrann.demo.service;

import com.deyrann.demo.repository.*;
import com.deyrann.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationRequestService {

    @Autowired
    private ApplicationRequestRepository requestRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private OperatorsRepository operatorsRepository;

    public List<ApplicationRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public ApplicationRequest getRequestById(Long id) {
        Optional<ApplicationRequest> opt = requestRepository.findById(id);
        return opt.orElse(null);
    }

    public ApplicationRequest createRequest(ApplicationRequest request) {
        if (request.getCourse() != null && request.getCourse().getId() != null) {
            Optional<Courses> course = coursesRepository.findById(request.getCourse().getId());
            if (course.isPresent()) request.setCourse(course.get());
        }

        if (request.getOperators() != null && !request.getOperators().isEmpty()) {
            List<Long> ids = new ArrayList<>();
            for (Operators o : request.getOperators()) {
                ids.add(o.getId());
            }
            List<Operators> ops = operatorsRepository.findAllById(ids);
            request.setOperators(ops);
        }

        return requestRepository.save(request);
    }

    public ApplicationRequest updateRequest(Long id, ApplicationRequest updated) {
        Optional<ApplicationRequest> opt = requestRepository.findById(id);
        if (opt.isEmpty()) return null;

        ApplicationRequest existing = opt.get();
        existing.setUserName(updated.getUserName());
        existing.setCommentary(updated.getCommentary());
        existing.setPhone(updated.getPhone());
        existing.setHandled(updated.isHandled());

        if (updated.getCourse() != null && updated.getCourse().getId() != null) {
            Optional<Courses> course = coursesRepository.findById(updated.getCourse().getId());
            if (course.isPresent()) existing.setCourse(course.get());
        } else {
            existing.setCourse(null);
        }

        if (updated.getOperators() != null) {
            List<Long> ids = new ArrayList<>();
            for (Operators o : updated.getOperators()) {
                ids.add(o.getId());
            }
            List<Operators> ops = operatorsRepository.findAllById(ids);
            existing.setOperators(ops);
        }

        return requestRepository.save(existing);
    }

    public boolean deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) return false;
        requestRepository.deleteById(id);
        return true;
    }

    public ApplicationRequest assignOperator(Long requestId, Long operatorId) {
        Optional<ApplicationRequest> reqOpt = requestRepository.findById(requestId);
        Optional<Operators> opOpt = operatorsRepository.findById(operatorId);

        if (reqOpt.isEmpty() || opOpt.isEmpty()) return null;

        ApplicationRequest req = reqOpt.get();
        Operators operator = opOpt.get();

        List<Operators> ops = req.getOperators();
        if (ops == null) ops = new ArrayList<>();

        boolean exists = false;
        for (Operators o : ops) {
            if (Objects.equals(o.getId(), operator.getId())) {
                exists = true;
                break;
            }
        }
        if (!exists) ops.add(operator);

        req.setOperators(ops);
        req.setHandled(true);
        return requestRepository.save(req);
    }
}