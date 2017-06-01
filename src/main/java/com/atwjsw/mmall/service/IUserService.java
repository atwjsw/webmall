package com.atwjsw.mmall.service;

import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * Created by wenda on 5/28/2017.
 */
public interface IUserService {
    public ServerResponse<User> login(String username, String password);
    public ServerResponse<String> register(User user);
    public ServerResponse<String> checkValid(String str, String type);
    public ServerResponse<String> selectQuestion(String username);
    public ServerResponse<String> checkAnswer(String username, String question, String answer);
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);
    public ServerResponse<User> updateInformation(User user);
    public ServerResponse<User> getInformation(Integer id);
}
