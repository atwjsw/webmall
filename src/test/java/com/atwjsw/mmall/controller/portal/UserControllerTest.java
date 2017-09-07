package com.atwjsw.mmall.controller.portal;

import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.User;
import com.atwjsw.mmall.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        success = ServerResponse.createBySuccess("登录成功", new User());
    }

    @Test
    public void shouldNotNull() {
        assertNotNull(userService);
    }

    @Test
    public void login_success() {
        when(userService.login("geely", "geely")).thenReturn(success);
        ServerResponse sr = userController.login("geely", "geely", session);
        assertTrue(sr.isSuccess());
        assertEquals("登录成功", sr.getMsg());
        assertEquals(0, sr.getStatus());
        assertThat(sr.getData(), instanceOf(User.class));
    }

    @Test
    public void login_failure_username_not_exists() {
        when(userService.login("geely123", "geely")).thenReturn(userNotExist);
        ServerResponse sr = userController.login("geely123", "geely", session);
        assertFalse(sr.isSuccess());
        assertEquals("用户名不存在", sr.getMsg());
        assertEquals(1, sr.getStatus());
        assertNull(sr.getData());
    }

    @Test
    public void login_failure_password_wrong() {
        when(userService.login("geely", "geely123")).thenReturn(wrongPassword);
        ServerResponse sr = userController.login("geely", "geely123", session);
        assertFalse(sr.isSuccess());
        assertEquals("密码错误", sr.getMsg());
        assertEquals(1, sr.getStatus());
        assertNull(sr.getData());
    }




}
