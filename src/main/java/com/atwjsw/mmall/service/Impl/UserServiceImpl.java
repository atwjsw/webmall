package com.atwjsw.mmall.service.Impl;

import com.atwjsw.mmall.common.Const;
import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.common.TokenCache;
import com.atwjsw.mmall.dao.UserMapper;
import com.atwjsw.mmall.pojo.User;
import com.atwjsw.mmall.service.IUserService;
import com.atwjsw.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by wenda on 5/28/2017.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

//      System.out.println("in UserServiceImpl/login");
        //check if user exists.
        int resultCount = userMapper.checkUsername(username);
//      System.out.println(count);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        System.out.println("MD5Util.MD5EncodeUtf8(password)" + MD5Util.MD5EncodeUtf8(password));
        User user = userMapper.selectLogin(username, MD5Util.MD5EncodeUtf8(password));
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //密码MD5加密 todo salt
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (type.equals(Const.USERNAME)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (type.equals(Const.EMAIL)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (!StringUtils.isNotBlank(question)) {
            return ServerResponse.createByErrorMessage("该用户未设置找回密码问题");
        }
        return ServerResponse.createBySuccess(question);
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("问题答案错误");
        }
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
        return ServerResponse.createBySuccess(forgetToken);
    }

    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if (!StringUtils.equals(forgetToken, token)) {
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);

        int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("修改密码操作失效");
        }

        return ServerResponse.createBySuccessMessage("修改密码成功");

    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {

        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码输入错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));

        int rowCount = userMapper.updateByPrimaryKeySelective(user);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("修改密码失败");
        }

        return ServerResponse.createBySuccessMessage("修改密码成功");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //username是不能更新的
        //email也要进行一个校验，校验新的email是不是已经存在，如果email存在并且相同，不能是我们当前的这个用户的。
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已被使用，请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount == 0) {
            return ServerResponse.createByErrorMessage("用户信息更新失败");
        }
        return ServerResponse.createBySuccess("个人信息更新成功", updateUser);
    }

    public ServerResponse<User> getInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user==null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //backend

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse<String> checkAdminRole(User user) {
        if ( user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

}

