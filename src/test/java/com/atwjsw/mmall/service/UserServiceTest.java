package com.atwjsw.mmall.service;

import com.alipay.api.domain.AlipayDataServiceResult;
import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.dao.UserMapper;
import com.atwjsw.mmall.pojo.User;
import com.atwjsw.mmall.service.Impl.UserServiceImpl;
import com.atwjsw.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by wenda on 9/7/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private IUserService iUserService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void shouldNotNull() {
        assertNotNull(userMapper);
    }

    @Before
    public void setup() {
        iUserService = new UserServiceImpl(userMapper);
        when(userMapper.checkUsername("geely")).thenReturn(1);
        when(userMapper.checkUsername("geely123")).thenReturn(0);
        when(userMapper.selectLogin("geely", MD5Util.MD5EncodeUtf8("geely"))).thenReturn(getUser());
        when(userMapper.selectLogin("geely", MD5Util.MD5EncodeUtf8("geely123"))).thenReturn(null);
        when(userMapper.selectByPrimaryKey(13)).thenReturn(getUser());
        when(userMapper.selectByPrimaryKey(31)).thenReturn(null);
    }

    @Test
    public void login() {
        ServerResponse<User> success = iUserService.login("geely", "geely");
        assertEquals(0, success.getStatus());
        assertEquals("登录成功", success.getMsg());
        assertEquals("geely", success.getData().getUsername());
        ServerResponse<User> userNotExists = iUserService.login("geely123", "geely");
        assertEquals(1, userNotExists.getStatus());
        assertEquals("用户名不存在", userNotExists.getMsg());
        assertNull(userNotExists.getData());
        ServerResponse<User> wrongPassword = iUserService.login("geely", "geely123");
        assertEquals(1, wrongPassword.getStatus());
        assertEquals("密码错误", wrongPassword.getMsg());
        assertNull(wrongPassword.getData());
    }

    @Test
    public void getInformation() {
        ServerResponse<User> found = iUserService.getInformation(13);
        assertEquals(0, found.getStatus());
        assertEquals("geely", found.getData().getUsername());
        assertEquals(StringUtils.EMPTY, found.getData().getPassword());
        ServerResponse<User> notFound = iUserService.getInformation(31);
        System.out.println(notFound);
        assertEquals(1, notFound.getStatus());
        assertEquals("找不到当前用户", notFound.getMsg());
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
