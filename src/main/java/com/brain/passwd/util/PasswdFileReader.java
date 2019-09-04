package com.brain.passwd.util;

import com.brain.passwd.model.Group;
import com.brain.passwd.model.User;

import java.io.IOException;
import java.util.List;

public interface PasswdFileReader {

    List<User> getUserEntries(String path) throws IOException;
    List<Group> getGroupEntries(String path) throws IOException;
}
