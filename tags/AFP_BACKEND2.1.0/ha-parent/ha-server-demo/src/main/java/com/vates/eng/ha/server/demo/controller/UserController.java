package com.vates.eng.ha.server.demo.controller;

import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.Preconditions;
import com.vates.eng.ha.persistence.domain.User;
import com.vates.eng.ha.persistence.service.UserService;

@Controller
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public User create(@RequestBody User user) {

        log.info("Creating new user {}", user);

        return userService.create(user);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User read(@PathVariable(value = "id") long id) {

        log.info("Reading user with id {}", id);

        return userService.find(id);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable(value = "id") Long id, @RequestBody User user) {

        log.info("Updating user with id {} with {}", id, user);

        Preconditions.checkArgument(id == user.getId(), "id doesn't match URL userId: {0}", user.getId());

        userService.update(user);

    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "userId") long userId) {

        log.info("Deleting user with id {}", userId);

        userService.delete(userId);

    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> list() {

        log.info("Listing users");

        return userService.findAll();

    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleClientErrors(Exception ex) {

        log.error(ex.getMessage(), ex);

        return ex.getMessage();

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleServerErrors(Exception ex) {

        log.error(ex.getMessage(), ex);

        return ex.getMessage();

    }

}
