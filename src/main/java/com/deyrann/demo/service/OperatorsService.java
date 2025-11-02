package com.deyrann.demo.service;

import com.deyrann.demo.repository.*;
import com.deyrann.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OperatorsService {

    @Autowired
    private OperatorsRepository operatorsRepository;

    public List<Operators> getAllOperators() {
        return operatorsRepository.findAll();
    }

    public Operators getOperatorById(Long id) {
        Optional<Operators> opt = operatorsRepository.findById(id);
        return opt.orElse(null);
    }

    public Operators createOperator(Operators operator) {
        return operatorsRepository.save(operator);
    }
}
