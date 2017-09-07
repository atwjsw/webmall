package com.atwjsw.mmall.controller.portal;

import com.atwjsw.mmall.common.Const;
import com.atwjsw.mmall.common.ResponseCode;
import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.User;
import com.atwjsw.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by wenda on 5/28/2017.
 */
@Controller
@RequestMapping(value="/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        System.out.println("in userController/login");
        ServerResponse<User> response = iUserService.login(username, password);
        System.out.println(response);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        System.out.println("in userController/logout");
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        ServerResponse<String> response = iUserService.register(user);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        ServerResponse<String> response = iUserService.checkValid(str, type);
        System.out.println(response);
        return response;
    }

//    getUserInfo(User)
    @RequestMapping(value= "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        }
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value= "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        ServerResponse<String> response = iUserService.selectQuestion(username);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value= "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        ServerResponse<String> response = iUserService.checkAnswer(username, question, answer);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value= "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        ServerResponse response = iUserService.forgetResetPassword(username, passwordNew, forgetToken);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value= "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        ServerResponse response = iUserService.resetPassword(passwordOld, passwordNew, user);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value= "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(User user, HttpSession session) {

        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            //todo response.getData().setRole(currentUser.getRole());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value= "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,无法获取当前用户信息,status=10,强制登录");
        }
        ServerResponse<User> response = iUserService.getInformation(currentUser.getId());
        System.out.println(response);
        return response;
    }
}