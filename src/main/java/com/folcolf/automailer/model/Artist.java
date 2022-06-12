package com.folcolf.automailer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Artist {

    private String name;
    private String label;
    private String email;
    private String gender;

}
