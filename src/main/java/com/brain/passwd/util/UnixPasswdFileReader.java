package com.brain.passwd.util;

import com.brain.passwd.model.Group;
import com.brain.passwd.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnixPasswdFileReader implements PasswdFileReader {

    @Override
    public List<User> getUserEntries(String path) throws IOException {
        Stream<String> stream = Files.lines(Paths.get(path));
        return stream
                .filter(line -> !line.startsWith("#"))
                .map(line -> new User(line.split(":")))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Group> getGroupEntries(String path) throws IOException {
        Stream<String> stream = Files.lines(Paths.get(path));
        return stream
                .filter(line -> !line.startsWith("#"))
                .map(line -> new Group(line.split(":")))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
