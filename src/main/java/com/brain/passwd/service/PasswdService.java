package com.brain.passwd.service;

import com.brain.passwd.db.GroupRepository;
import com.brain.passwd.db.UserRepository;
import com.brain.passwd.model.Group;
import com.brain.passwd.util.PasswdFileFormats;
import com.brain.passwd.model.User;
import com.brain.passwd.util.UnixPasswdFileReader;
import com.brain.passwd.util.PasswdFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PasswdService {

    private final String passwdFilePath;
    private final String groupFilePath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    private PasswdFileReader passwdFileReader;

    public PasswdService(@Value("${passwd.file.path}") String passwdFilePath,
                         @Value("${group.file.path}") String groupFilePath,
                         @Value("${passwd.file.format}") String passwdFileFormat) throws Exception {

        this.passwdFilePath = passwdFilePath;
        this.groupFilePath = groupFilePath;

        if (!passwdFilePath.isEmpty() && !groupFilePath.isEmpty()) {
            if (PasswdFileFormats.valueOf(passwdFileFormat) == PasswdFileFormats.UNIX) {
                this.passwdFileReader = new UnixPasswdFileReader();
            } else {
                throw new Exception("Invalid passwd file format: " + passwdFileFormat);
            }
        } else {
            throw new Exception("Required 'passwd.file.path' and 'group.file.path' properties are not set");
        }
    }

    public List<User> getUsers() { return userRepository.findAll(); }

    public User getUser(int id) { return userRepository.findByUid(id); }

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public Group getGroup(int id) {
        return groupRepository.findByGid(id);
    }

    public List<Group> getUserGroups(int id) {
        User user = userRepository.findByUid(id);
        if (user == null) {
            return null;
        }
        return groupRepository.findAll().stream()
                .filter(group -> Arrays.asList(group.getMembers()).contains(user.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<User> filterUsers(String name, Integer uid, Integer gid, String comment, String home, String shell) {
        return userRepository.findAll().stream()
                .filter(user -> Optional.ofNullable(name).isEmpty() || name.equals(user.getName()))
                .filter(user -> Optional.ofNullable(uid).isEmpty() || uid.equals(user.getUid()))
                .filter(user -> Optional.ofNullable(gid).isEmpty() || gid.equals(user.getGid()))
                .filter(user -> Optional.ofNullable(comment).isEmpty() || comment.equals(user.getComment()))
                .filter(user -> Optional.ofNullable(home).isEmpty() || home.equals(user.getHome()))
                .filter(user -> Optional.ofNullable(shell).isEmpty() || shell.equals(user.getShell()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Group> filterGroups(String name, Integer gid, List<String> member) {
        return groupRepository.findAll().stream()
                .filter(group -> Optional.ofNullable(name).isEmpty() || name.equals(group.getName()))
                .filter(group -> Optional.ofNullable(gid).isEmpty() || gid.equals(group.getGid()))
                .filter(group -> Optional.ofNullable(member).isEmpty() || Arrays.asList(group.getMembers()).containsAll(member))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public void reloadUserAndGroupData() throws IOException {
        userRepository.deleteAllInBatch();
        groupRepository.deleteAllInBatch();
        userRepository.flush();
        groupRepository.flush();
        passwdFileReader.getUserEntries(passwdFilePath).forEach(userRepository::saveAndFlush);
        passwdFileReader.getGroupEntries(groupFilePath).forEach(groupRepository::saveAndFlush);
    }
}
