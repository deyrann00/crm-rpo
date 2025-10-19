package com.deyrann.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ApplicationRequestRepository requestRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private OperatorsRepository operatorsRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<ApplicationRequest> requests = requestRepository.findAll();
        List<Courses> courses = coursesRepository.findAll();
        model.addAttribute("requests", requests);
        model.addAttribute("courses", courses);
        model.addAttribute("title", "Все Заявки");
        return "index";
    }

    @GetMapping("/new_requests")
    public String newRequests(Model model) {
        List<ApplicationRequest> allRequests = requestRepository.findAll();
        List<ApplicationRequest> requests = new ArrayList<>();
        for (ApplicationRequest request : allRequests) {
            if (!request.isHandled()) requests.add(request);
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
            if (request.isHandled()) requests.add(request);
        }
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Обработанные Заявки");
        return "index";
    }

    @GetMapping("/add_request")
    public String addRequestForm(Model model) {
        model.addAttribute("request", new ApplicationRequest());
        model.addAttribute("courses", coursesRepository.findAll());
        return "add_request";
    }

    @PostMapping("/add_request")
    public String addRequest(
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "commentary", required = false) String commentary,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "course", required = false) Long courseId // <-- id курса из select
    ) {
        ApplicationRequest request = new ApplicationRequest();
        request.setUserName(userName);
        request.setCommentary(commentary);
        request.setPhone(phone);
        request.setHandled(false);

        if (courseId != null) {
            coursesRepository.findById(courseId).ifPresent(request::setCourse);
        }

        requestRepository.save(request);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String requestDetails(Model model, @PathVariable Long id) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isEmpty()) {
            return "redirect:/";
        }

        ApplicationRequest request = optionalRequest.get();

        List<Operators> allOperators = operatorsRepository.findAll();

        List<Operators> assignedOperators = request.getOperators();

        model.addAttribute("request", request);
        model.addAttribute("operators", allOperators);
        model.addAttribute("assignedOperators", assignedOperators);

        return "details";
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

    @GetMapping("/edit/{id}")
    public String editRequestForm(Model model, @PathVariable Long id) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            ApplicationRequest request = optionalRequest.get();
            model.addAttribute("request", request);
            model.addAttribute("courses", coursesRepository.findAll());
            return "edit";
        }
        return "redirect:/";
    }

    @PostMapping("/edit/{id}")
    public String editRequest(
            @PathVariable Long id,
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "commentary", required = false) String commentary,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "handled", defaultValue = "false") boolean handled,
            @RequestParam(name = "course", required = false) Long courseId
    ) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            ApplicationRequest request = optionalRequest.get();
            request.setUserName(userName);
            request.setCommentary(commentary);
            request.setPhone(phone);
            request.setHandled(handled);

            if (courseId != null) {
                coursesRepository.findById(courseId).ifPresent(request::setCourse);
            }

            requestRepository.save(request);
            return "redirect:/details/" + id;
        }
        return "redirect:/";
    }

    @GetMapping("/assign_operators/{id}")
    public String showAssignOperatorsForm(@PathVariable Long id, Model model) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isEmpty()) {
            return "redirect:/";
        }

        ApplicationRequest request = optionalRequest.get();
        List<Operators> allOperators = operatorsRepository.findAll();

        model.addAttribute("request", request);
        model.addAttribute("operators", allOperators);
        return "assign_operators";
    }

    @PostMapping("/assign_operators/{id}")
    public String assignOperators(
            @PathVariable Long id,
            @RequestParam(value = "operatorIds", required = false) List<Long> operatorIds) {

        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent() && operatorIds != null) {
            ApplicationRequest request = optionalRequest.get();

            List<Operators> selectedOperators = operatorsRepository.findAllById(operatorIds);
            request.setOperators(selectedOperators);
            request.setHandled(true);

            requestRepository.save(request);
        }

        return "redirect:/details/" + id;
    }

    @PostMapping("/remove_operator/{requestId}/{operatorId}")
    public String removeOperator(@PathVariable Long requestId, @PathVariable Long operatorId) {
        Optional<ApplicationRequest> optionalRequest = requestRepository.findById(requestId);
        Optional<Operators> optionalOperator = operatorsRepository.findById(operatorId);

        if (optionalRequest.isPresent() && optionalOperator.isPresent()) {
            ApplicationRequest request = optionalRequest.get();
            request.getOperators().remove(optionalOperator.get());
            requestRepository.save(request);
        }

        return "redirect:/details/" + requestId;
    }
}