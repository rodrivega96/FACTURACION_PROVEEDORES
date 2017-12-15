package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.domain.UserView;

@Data
public class UserResponseDTO {

    private int status;
    private String message;
    private User user;
    private List<UserView> userList;
    private UserView userView;
    
}
