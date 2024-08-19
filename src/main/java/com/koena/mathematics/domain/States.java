package com.koena.mathematics.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class States {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String user;
    @Column(name = "start_time")
    private LocalDateTime start = LocalDateTime.now();
    @Column(name = "end_time")
    private LocalDateTime end = LocalDateTime.now();
    @Lob
    private byte[] states;


}
