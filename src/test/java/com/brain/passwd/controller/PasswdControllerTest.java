package com.brain.passwd.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brain.passwd.model.Group;
import com.brain.passwd.model.User;
import com.brain.passwd.service.PasswdService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class PasswdControllerTest {

    private MockMvc mockMvc;

    @Mock
    PasswdService passwdService;

    @InjectMocks
    private PasswdController passwdController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(passwdController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getAllUsers_returnsValidResponse() throws Exception {

        User user = User.builder()
                .name("test")
                .uid(0)
                .gid(0)
                .comment("test user")
                .home("/home")
                .shell("/bin/sh")
                .build();

        when(passwdService.getUsers())
                .thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$[0].name").value("test"));
    }

    @Test
    public void getUser_returnsValidResponse() throws Exception {

        User user = User.builder()
                .name("test")
                .uid(0)
                .gid(0)
                .comment("test user")
                .home("/home")
                .shell("/bin/sh")
                .build();

        when(passwdService.getUser(0))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/0"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    public void getUser_returnsNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/0"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getUserGroups_returnsValidResponse() throws Exception {

        Group group = Group.builder()
                .name("test")
                .gid(0)
                .members(new String[] {"user"})
                .build();

        when(passwdService.getUserGroups(0))
                .thenReturn(Collections.singletonList(group));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/0/groups"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$[0].name").value("test"));
    }

    @Test
    public void getUserGroups_returnsNotFound() throws Exception {

        when(passwdService.getUserGroups(0))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/0/groups"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getAllGroups_returnsValidResponse() throws Exception {

        Group group = Group.builder()
                .name("test")
                .gid(0)
                .members(new String[] {"user"})
                .build();

        when(passwdService.getGroups())
                .thenReturn(Collections.singletonList(group));

        mockMvc.perform(MockMvcRequestBuilders.get("/groups"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$[0].name").value("test"));
    }

    @Test
    public void getGroup_returnsValidResponse() throws Exception {

        Group group = Group.builder()
                .name("test")
                .gid(0)
                .members(new String[] {"user"})
                .build();

        when(passwdService.getGroup(0))
                .thenReturn(group);

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/0"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    public void getGroup_returnsNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/0"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void queryUsers_returnsValidResponse() throws Exception {

        User user = User.builder()
                .name("test")
                .uid(0)
                .gid(0)
                .comment("test user")
                .home("/home")
                .shell("/bin/sh")
                .build();

        when(passwdService.filterUsers("test", null, null, null, null, null))
                .thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/query?name=test"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$[0].name").value("test"));
    }

    @Test
    public void queryGroups_returnsValidResponse() throws Exception {

        Group group = Group.builder()
                .name("test")
                .gid(0)
                .members(new String[] {"user"})
                .build();

        when(passwdService.filterGroups("test", null, null))
                .thenReturn(Collections.singletonList(group));

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/query?name=test"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$[0].name").value("test"));
    }
}
