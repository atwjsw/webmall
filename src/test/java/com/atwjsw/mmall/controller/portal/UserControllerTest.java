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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        User user = new User();
        user.setId(13);
        user.setUsername("geely");
        user.setEmail("geely@happymmall.com");
        user.setPhone("13800138000");
        user.setQuestion("问题");
        user.setAnswer("答案");
        user.setRole(0);
        success = ServerResponse.createBySuccess("登录成功", user);
        when(userService.login("geely", "geely")).thenReturn(success);
        when(userService.login("geely123", "geely")).thenReturn(userNotExist);
        when(userService.login("geely", "geely123")).thenReturn(wrongPassword);
    }

    @Test
    public void shouldNotNull() {
        assertNotNull(userService);
    }

    @Test
    public void login_success() {
        ServerResponse sr = userController.login("geely", "geely", session);
        assertTrue(sr.isSuccess());
        assertEquals("登录成功", sr.getMsg());
        assertEquals(0, sr.getStatus());
        assertThat(sr.getData(), instanceOf(User.class));
    }

    @Test
    public void login_failure_username_not_exists() {
        ServerResponse sr = userController.login("geely123", "geely", session);
        assertFalse(sr.isSuccess());
        assertEquals("用户名不存在", sr.getMsg());
        assertEquals(1, sr.getStatus());
        assertNull(sr.getData());
    }

    @Test
    public void login_failure_password_wrong() {
        ServerResponse sr = userController.login("geely", "geely123", session);
        assertFalse(sr.isSuccess());
        assertEquals("密码错误", sr.getMsg());
        assertEquals(1, sr.getStatus());
        assertNull(sr.getData());
    }

    @Test
    public void get_user_info_success() {
        ServerResponse sr = userController.login("geely", "geely", session);
        assertTrue(sr.isSuccess());
        ServerResponse userInfo = userController.getUserInfo(session);
        assertThat(userInfo.getData(), instanceOf(User.class));
        sr = userController.login("geely", "geely123", session);
        userInfo = userController.getUserInfo(session);
        assertThat(userInfo.getData(), instanceOf(User.class));
    }

    @Test
    public void get_user_info_failure() {
        ServerResponse sr = userController.login("geely", "geely123", session);
        assertFalse(sr.isSuccess());
        ServerResponse userInfo = userController.getUserInfo(session);
        System.out.println(userInfo);
        assertNull(userInfo.getData());
        assertEquals(1, userInfo.getStatus());
        assertEquals("用户未登录,无法获取当前用户信息", userInfo.getMsg());
    }

    @Test
    public void getUserInfo_success() throws Exception {
//        UserController userController = new UserController();
        MockMvc mockMvc = standaloneSetup(userController).build();
        String responseString = mockMvc.perform(post("/user/login.do")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("username", "geely")
                .param("password", "geely"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.status").value(0))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("Return json = " + responseString);
    }
}
