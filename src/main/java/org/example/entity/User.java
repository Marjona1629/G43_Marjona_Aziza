package org.example.entity;

import lombok.*;
import org.example.utils.States;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class User {
    private Long id;
    private String name;
    private States state;
}