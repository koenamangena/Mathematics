package com.koena.mathematics.controller;

import com.koena.mathematics.models.ExpessionDto;
import com.koena.mathematics.models.StatesDto;
import com.koena.mathematics.service.InterpreterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/states")
public class StateController {

    @Autowired
    private InterpreterService interpreterService;

    @GetMapping
    public Stream<Map<String, Double>> getStates(HttpServletRequest request, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<StatesDto> states = interpreterService.getState(request.getRemoteAddr(), start, end);
        return states.stream().map(StatesDto::getStates);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatesDto saveState(HttpServletRequest request, @RequestBody ExpessionDto expession) {
        return interpreterService.evaluate(expession.getExpression(), request.getRemoteAddr());
    }
}

