package com.brain.passwd.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "GroupEntity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    public Group(String[] properties) {

        if (!(properties.length == 4 || properties.length == 3)) {  // Array length will be 3 if group has no members
            throw new IllegalArgumentException("Error reading group file: Need 4 items from file and there's only " + properties.length);
        }

        this.name = properties[0];
        this.gid = Integer.parseInt(properties[2]);
        if (properties.length == 4) {
            this.members = properties[3].split(",");
        } else {
            this.members = new String[] {};
        }
    }

    @Getter
    @Setter
    private String name;

    @Id
    @Getter
    @Setter
    private int gid;

    @Getter
    @Setter
    private String[] members;
}
