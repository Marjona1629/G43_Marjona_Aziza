package org.example.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Card{
    private Integer id;
    private String number;
    private String password;
    private Double balance;
    private Integer user_id;
}