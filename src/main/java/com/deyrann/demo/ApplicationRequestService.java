package com.deyrann.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationRequestService {

    @Autowired
    private ApplicationRequestRepository requestRepository;

    public List<ApplicationRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<ApplicationRequest> getNewRequests() {
        return requestRepository.findAllByHandledFalse();
    }

    public List<ApplicationRequest> getProcessedRequests() {
        return requestRepository.findAllByHandledTrue();
    }

    public Optional<ApplicationRequest> getRequest(Long id) {
        return requestRepository.findById(id);
    }

    public void addRequest(ApplicationRequest request) {
        request.setHandled(false);
        requestRepository.save(request);
    }

    public boolean handleRequest(Long id) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            ApplicationRequest request = optionalRequest.get();
            request.setHandled(true);
            requestRepository.save(request);
            return true;
        }
        return false;
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}