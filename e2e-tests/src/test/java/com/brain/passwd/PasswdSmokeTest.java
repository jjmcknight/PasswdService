package com.brain.passwd;

import com.brain.passwd.sdk.api.PasswdControllerApi;
import com.brain.passwd.sdk.invoker.ApiClient;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

public class PasswdSmokeTest {

    private static PasswdControllerApi client;

    @BeforeClass
    public static void setupOnce() {

        String baseUrl = Optional.ofNullable(System.getProperty("passwd.service.url")).orElseGet(() -> "http://localhost:8080");
        client = ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> new RequestSpecBuilder()
                        .setBaseUri(baseUrl))).passwdController();
    }

    @Test
    public void getUsersTest() {

        Assert.assertEquals(21, client.getUsers()
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());
    }

    @Test
    public void getUserTest() {

        Assert.assertEquals("root",
                client.getUser().idPath(0)
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("name"));
    }

    @Test
    public void getUserGroupsTest() {

        Assert.assertEquals(2, client.getUserGroups().idPath(555)
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());
    }

    @Test
    public void filterUsersTest() {

        // name query
        Assert.assertEquals("root", client.filterUsers().nameQuery("root")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("[0].name"));

        // uid query
        Assert.assertEquals("root", client.filterUsers().uidQuery(0)
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("[0].name"));

        // gid query
        Assert.assertEquals("root", client.filterUsers().gidQuery(0)
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("[0].name"));

        // comment query
        Assert.assertEquals("user1", client.filterUsers().commentQuery("test user1")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("[0].name"));

        // home query
        Assert.assertEquals("user2", client.filterUsers().homeQuery("/usr/user2/home")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("[0].name"));

        // shell query
        Assert.assertEquals(3, client.filterUsers().shellQuery("/bin/bash")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());

        // compound query
        Assert.assertEquals(1, client.filterUsers()
                .nameQuery("user1")
                .uidQuery(555)
                .gidQuery(555)
                .commentQuery("test user1")
                .homeQuery("/usr/user1/home")
                .shellQuery("/bin/bash")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());

        // filter values don't match
        Assert.assertEquals(0, client.filterUsers().nameQuery("does_not_exist")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());
    }

    @Test
    public void getGroupsTest() {

        Assert.assertEquals(39, client.getGroups()
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());
    }

    @Test
    public void getGroupTest() {

        Assert.assertEquals("root",
                client.getGroup().idPath(0)
                        .execute(ResponseBody::prettyPeek)
                        .jsonPath()
                        .get("name"));
    }

    @Test
    public void filterGroupsTest() {

        // name query
        Assert.assertEquals("root", client.filterGroups().nameQuery("root")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("[0].name"));

        // gid query
        Assert.assertEquals("root", client.filterGroups().gidQuery(0)
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .get("[0].name"));

        // member query
        Assert.assertEquals(2, client.filterGroups().memberQuery("user1")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());

        // multi-member query
        Assert.assertEquals(1, client.filterGroups().memberQuery("user1").memberQuery("user2")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());

        // compound query
        Assert.assertEquals(1, client.filterGroups()
                .nameQuery("users")
                .gidQuery(100)
                .memberQuery("user1")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());

        // filter values don't match
        Assert.assertEquals(0, client.filterGroups().nameQuery("does_not_exist")
                .execute(ResponseBody::prettyPeek)
                .jsonPath()
                .getList("$")
                .size());
    }

    // TODO: add test that writes to passwd file, verify GET /users includes newly added entry
}
