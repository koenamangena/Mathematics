package com.koena.mathematics.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class StatesDto {
    private Long id;
    private String user;
    private LocalDateTime start;
    private LocalDateTime end;
    private Map<String, Double> states;

}
