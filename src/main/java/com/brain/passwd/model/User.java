package com.brain.passwd.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    public User(String[] properties) {

        if (properties.length != 7) {
            throw new IllegalArgumentException("Error reading passwd file: Need 7 items from file and there's only " + properties.length);
        }

        this.name = properties[0];
        this.uid = Integer.parseInt(properties[2]);
        this.gid = Integer.parseInt(properties[3]);
        this.comment = properties[4];
        this.home = properties[5];
        this.shell = properties[6];
    }

    @Getter
    @Setter
    private String name;

    @Id
    @Getter
    @Setter
    private int uid;

    @Getter
    @Setter
    private int gid;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private String home;

    @Getter
    @Setter
    private String shell;
}
