package com.brain.passwd.controller;

import com.brain.passwd.model.Group;
import com.brain.passwd.model.User;
import com.brain.passwd.service.PasswdService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class PasswdController {

    @Autowired
    private PasswdService service;

    @GetMapping("/users")
    @ApiOperation(value = "", nickname = "getUsers")
    public List<User> getUsers() throws IOException {
        service.reloadUserAndGroupData();
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    @ApiOperation(value = "", nickname = "getUser")
    public ResponseEntity<User> getUser(@PathVariable int id) throws IOException {
        service.reloadUserAndGroupData();
        return Optional.ofNullable(service.getUser(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{id}/groups")
    @ApiOperation(value = "", nickname = "getUserGroups")
    public ResponseEntity<List<Group>> getUserGroups(@PathVariable int id) throws IOException {
        service.reloadUserAndGroupData();
        return Optional.ofNullable(service.getUserGroups(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/query")
    @ApiOperation(value = "", nickname = "filterUsers")
    public List<User> filterUsers(@RequestParam(required = false) String name, @RequestParam(required = false) String uid,
                                 @RequestParam(required = false) String gid, @RequestParam(required = false) String comment,
                                 @RequestParam(required = false) String home, @RequestParam(required = false) String shell)
                                 throws IOException {
        Integer userId = Optional.ofNullable(uid).map(Integer::parseInt).orElse(null);
        Integer groupId = Optional.ofNullable(gid).map(Integer::parseInt).orElse(null);
        service.reloadUserAndGroupData();
        return service.filterUsers(name, userId, groupId, comment, home, shell);
    }

    @GetMapping("/groups")
    @ApiOperation(value = "", nickname = "getGroups")
    public List<Group> getGroups() throws IOException {
        service.reloadUserAndGroupData();
        return service.getGroups();
    }

    @GetMapping("/groups/{id}")
    @ApiOperation(value = "", nickname = "getGroup")
    public ResponseEntity<Group> getGroup(@PathVariable int id) throws IOException {
        service.reloadUserAndGroupData();
        return Optional.ofNullable(service.getGroup(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/groups/query")
    @ApiOperation(value = "", nickname = "filterGroups")
    public List<Group> filterGroups(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String gid,
                                   @RequestParam(required = false) List<String> member) throws IOException {
        Integer groupId = Optional.ofNullable(gid).map(Integer::parseInt).orElse(null);
        service.reloadUserAndGroupData();
        return service.filterGroups(name, groupId, member);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArugumentExceptionHandler(IllegalArgumentException exception) {
        return new ResponseEntity<>("IllegalArgumentException: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<Object> iOExceptionHandler(IOException exception) {
        return new ResponseEntity<>("IOException: a" + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
