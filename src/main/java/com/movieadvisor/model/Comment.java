package com.movieadvisor.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    String comment;
    int vote;
    LocalDateTime commentedAt;
}
