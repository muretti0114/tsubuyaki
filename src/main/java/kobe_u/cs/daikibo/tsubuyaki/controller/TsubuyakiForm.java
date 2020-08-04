package kobe_u.cs.daikibo.tsubuyaki.controller;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TsubuyakiForm {
    String name; //投稿者
    @NotBlank
    String comment; //つぶやき（省略不可）
}