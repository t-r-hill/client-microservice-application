package com.example.clientmicroserviceapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javassist.compiler.ast.ASTree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @JsonProperty("itemId")
    private Long id;
    private String name;
    private String description;
}