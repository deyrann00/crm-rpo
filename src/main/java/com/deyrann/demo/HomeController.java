package com.deyrann.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ApplicationRequestRepository requestRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<ApplicationRequest> requests = requestRepository.findAll();
        model.addAttribute("requests", requests);
        return "index"; // index.html
    }

    @GetMapping("/new_requests")
    public String newRequests(Model model) {
        List<ApplicationRequest> requests = requestRepository.findAllByHandledFalse();
        model.addAttribute("requests", requests);
        return "index";
    }

    @GetMapping("/processed_requests")
    public String processedRequests(Model model) {
        List<ApplicationRequest> requests = requestRepository.findAllByHandledTrue();
        model.addAttribute("requests", requests);
        return "index";
    }

    @GetMapping("/add_request")
    public String addRequestForm(Model model) {
        model.addAttribute("request", new ApplicationRequest());
        return "add_request"; // add_request.html
    }

    @PostMapping("/add_request")
    public String addRequest(@ModelAttribute("request") ApplicationRequest request) {
        request.setHandled(false);
        requestRepository.save(request);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String requestDetails(@PathVariable Long id, Model model) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            model.addAttribute("request", optionalRequest.get());
            return "details";
        }
        return "redirect:/";
    }

    @PostMapping("/handle_request/{id}")
    public String handleRequest(@PathVariable Long id) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            ApplicationRequest request = optionalRequest.get();
            request.setHandled(true);
            requestRepository.save(request);
        }
        return "redirect:/details/" + id;
    }

    @PostMapping("/delete_request/{id}")
    public String deleteRequest(@PathVariable Long id) {
        requestRepository.deleteById(id);
        return "redirect:/";
    }
}
