package com.atwjsw.mmall.controller.portal;

import com.atwjsw.mmall.common.Const;
import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.User;
import com.atwjsw.mmall.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by wenda on 9/7/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private MockHttpSession session = new MockHttpSession();
    //    private IUserService userService = mock(IUserService.class);
    @Mock
    private IUserService userService;
    private UserController userController;
    ServerResponse userNotExist;
    ServerResponse wrongPassword;
    ServerResponse success;

    @Before
    public void setup() {
        userController = new UserController(userService);
        userNotExist = ServerResponse.createByErrorMessage("用户名不存在");
        wrongPassword = ServerResponse.createByErrorMessage("密码错误");
        success = ServerResponse.createBySuccess("登录成功", getUser());
        when(userService.login("geely", "geely")).thenReturn(success);
        when(userService.login("geely123", "geely")).thenReturn(userNotExist);
        when(userService.login("geely", "geely123")).thenReturn(wrongPassword);
    }

    @Test
    public void shouldNotNull() {
        assertNotNull(userService);
    }

    @Test
    public void login() {
        ServerResponse<User> success = userController.login("geely", "geely", session);
        assertTrue(success.isSuccess());
        assertEquals("登录成功", success.getMsg());
        assertEquals(0, success.getStatus());
        assertEquals("geely", success.getData().getUsername());
        ServerResponse userNameNotExists = userController.login("geely123", "geely", session);
        assertFalse(userNameNotExists.isSuccess());
        assertEquals("用户名不存在", userNameNotExists.getMsg());
        assertEquals(1, userNameNotExists.getStatus());
        assertNull(userNameNotExists.getData());
        ServerResponse wrongPassword = userController.login("geely", "geely123", session);
        assertFalse(wrongPassword.isSuccess());
        assertEquals("密码错误", wrongPassword.getMsg());
        assertEquals(1, wrongPassword.getStatus());
        assertNull(wrongPassword.getData());
    }

    @Test
    public void getUserInfo() {
        ServerResponse loginSuccess = userController.login("geely", "geely", session);
        assertTrue(loginSuccess.isSuccess());
        ServerResponse<User> userInfo = userController.getUserInfo(session);
        assertEquals("geely", userInfo.getData().getUsername());
        session.clearAttributes();
        ServerResponse loginFailure = userController.login("geely", "geely123", session);
        ServerResponse<User> nullUser = userController.getUserInfo(session);
        assertNull(nullUser.getData());
        assertEquals(1, nullUser.getStatus());
        assertEquals("用户未登录,无法获取当前用户信息", nullUser.getMsg());
    }

    @Test
    public void getUserInfoMvc() throws Exception {
        MockMvc mockMvc = standaloneSetup(userController).build();
        String success = mockMvc.perform(post("/user/login.do")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("username", "geely")
                .param("password", "geely"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.msg").value("登录成功"))
                .andExpect(jsonPath("$..username").value("geely"))
//                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("Return json = " + success);
        String wrongPassword = mockMvc.perform(post("/user/login.do")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("username", "geely")
                .param("password", "geely123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.msg").value("密码错误"))
//                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("Return json = " + wrongPassword);

        String userNotExists = mockMvc.perform(post("/user/login.do")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("username", "geely123")
                .param("password", "geely"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.msg").value("用户名不存在"))
//                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("Return json = " + userNotExists);
    }

    private User getUser() {
        User user = new User();
        user.setId(13);
        user.setUsername("geely");
        user.setEmail("geely@happymmall.com");
        user.setPhone("13800138000");
        user.setQuestion("问题");
        user.setAnswer("答案");
        user.setRole(0);
        return user;
    }

}
