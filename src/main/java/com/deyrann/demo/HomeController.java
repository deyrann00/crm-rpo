package com.deyrann.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
        model.addAttribute("title", "Все Заявки");
        return "index";
    }

    @GetMapping("/new_requests")
    public String newRequests(Model model) {
        List<ApplicationRequest> allRequests = requestRepository.findAll();

        List<ApplicationRequest> requests = new ArrayList<>();

        for (ApplicationRequest request : allRequests) {
            if (!request.isHandled()) {
                requests.add(request);
            }
        }

        model.addAttribute("requests", requests);
        model.addAttribute("title", "Новые Заявки");
        return "index";
    }

    @GetMapping("/processed_requests")
    public String processedRequests(Model model) {
        List<ApplicationRequest> allRequests = requestRepository.findAll();

        List<ApplicationRequest> requests = new ArrayList<>();

        for (ApplicationRequest request : allRequests) {
            if (request.isHandled()) {
                requests.add(request);
            }
        }

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
    public String addRequest(
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "courseName") String courseName,
            @RequestParam(name = "commentary") String commentary,
            @RequestParam(name = "phone") String phone)
    {
        ApplicationRequest request = new ApplicationRequest();
        request.setUserName(userName);
        request.setCourseName(courseName);
        request.setCommentary(commentary);
        request.setPhone(phone);

        request.setHandled(false);

        requestRepository.save(request);

        return "redirect:/";
    }


    @GetMapping("/details/{id}")
    public String requestDetails(Model model, @PathVariable Long id) {
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
}