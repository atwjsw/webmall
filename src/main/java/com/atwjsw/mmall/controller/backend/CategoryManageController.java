package com.atwjsw.mmall.controller.backend;

import com.atwjsw.mmall.common.Const;
import com.atwjsw.mmall.common.ResponseCode;
import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.User;
import com.atwjsw.mmall.service.ICategoryService;
import com.atwjsw.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by wenda on 6/1/2017.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;

//    parentId(default=0)
//    categoryName
    @RequestMapping(value = "add_category.do", method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(@RequestParam(value="parentId", defaultValue="0") Integer parentId, String categoryName, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if ( user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录， 请登录");
        };

        if(!iUserService .checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限添加品类操作，需管理员权限");
        };

        ServerResponse response = iCategoryService.addCategory(parentId, categoryName);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value = "set_category_name.do", method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(Integer categoryId, String categoryName, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if ( user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录， 请登录");
        };

        if(!iUserService .checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限添加品类操作，需管理员权限");
        };

        ServerResponse response = iCategoryService.setCategoryName(categoryId, categoryName);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value = "get_category.do", method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(@RequestParam(value="categoryId",defaultValue="0") Integer categoryId, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if ( user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录， 请登录");
        };

        if(!iUserService .checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限读取品类操作，需管理员权限");
        };

        ServerResponse response = iCategoryService.getChildrenParallelCategory(categoryId);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value = "get_deep_category.do", method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value="categoryId",defaultValue="0") Integer categoryId, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if ( user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录， 请登录");
        };

        if(!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限读取品类操作，需管理员权限");
        };

        ServerResponse response = iCategoryService.selectCategoryAndChildrenById(categoryId);
        System.out.println(response);
        return response;
    }

}
