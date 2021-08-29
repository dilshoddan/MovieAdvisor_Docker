package com.movieadvisor.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long starId;
    @NotNull
    @NotBlank(message = "Movie Star name is required!")
    private String name;

    public Star(@NotNull @NotBlank(message = "Movie Star name is required!") String name) {
        this.name = name;
    }
}
