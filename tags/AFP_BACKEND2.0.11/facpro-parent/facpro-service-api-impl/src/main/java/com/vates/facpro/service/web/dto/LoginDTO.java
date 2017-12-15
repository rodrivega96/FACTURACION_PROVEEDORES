package com.vates.facpro.service.web.dto;

import lombok.Data;


@Data
public class LoginDTO {
    private String userName;
    private String userPassword;
    private String userMail;
    private Long userDomain;
}
