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
    private ApplicationRequestService requestService;


    @GetMapping("/")
    public String index(Model model) {
        List<ApplicationRequest> requests = requestService.getAllRequests();
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Все Заявки");
        return "index";
    }

    @GetMapping("/new_requests")
    public String newRequests(Model model) {
        List<ApplicationRequest> requests = requestService.getNewRequests();
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Новые Заявки");
        return "index";
    }

    @GetMapping("/processed_requests")
    public String processedRequests(Model model) {
        List<ApplicationRequest> requests = requestService.getProcessedRequests();
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Обработанные Заявки");
        return "index";
    }


    @GetMapping("/add_request")
    public String addRequestForm(Model model) {
        model.addAttribute("request", new ApplicationRequest());
        return "add_request";
    }

    @PostMapping("/add_request")
    public String addRequest(@ModelAttribute("request") ApplicationRequest request) {
        requestService.addRequest(request);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String requestDetails(@PathVariable Long id, Model model) {
        Optional<ApplicationRequest> optionalRequest = requestService.getRequest(id);
        if (optionalRequest.isPresent()) {
            model.addAttribute("request", optionalRequest.get());
            return "details";
        }
        return "redirect:/";
    }

    @PostMapping("/handle_request/{id}")
    public String handleRequest(@PathVariable Long id) {
        requestService.handleRequest(id);
        return "redirect:/details/" + id;
    }

    @PostMapping("/delete_request/{id}")
    public String deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return "redirect:/";
    }
}