package com.atwjsw.mmall.controller.backend;

import com.atwjsw.mmall.common.Const;
import com.atwjsw.mmall.common.ResponseCode;
import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.Product;
import com.atwjsw.mmall.pojo.User;
import com.atwjsw.mmall.service.IFileService;
import com.atwjsw.mmall.service.IProductService;
import com.atwjsw.mmall.service.IUserService;
import com.atwjsw.mmall.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by wenda on 6/2/2017.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value="save.do")
    @ResponseBody
    public ServerResponse productSave(Product product, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }

        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限添加商品操作，需管理员权限");
        }

        ServerResponse response = iProductService.saveOrUpdateProudct(product);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value="set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(Integer productId, Integer status, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }

        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限");
        }

        ServerResponse response = iProductService.setSaleStatus(productId, status);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value="detail.do")
    @ResponseBody
    public ServerResponse getDetail(Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }

        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限");
        }

        ServerResponse response = iProductService.manageProductDetail(productId);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value="list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum, @RequestParam(value="pageSize", defaultValue="10") Integer pageSize, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }

        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限");
        }

        ServerResponse response = iProductService.getProductList(pageNum, pageSize);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value="search.do")
    @ResponseBody
    public ServerResponse search(String productName,Integer productId, @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
                                  @RequestParam(value="pageSize", defaultValue="10") Integer pageSize,
                                 HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }

        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限");
        }

        ServerResponse response = iProductService.searchProduct(productName, productId, pageNum, pageSize);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value="upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限");
        }
        //获取Tomcat服务器的上传文件路径。
        String path = request.getSession().getServletContext().getRealPath("upload");
        //上传文件到Tomcat服务器Path路径
        String targetFileName = iFileService.upload(file, path);
        //返回上传文件位置
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping(value="richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求，我们使用的是simditor, 以下语法只针对Simditor这个富文本插件
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
        //获取Tomcat服务器的上传文件路径。
        String path = request.getSession().getServletContext().getRealPath("upload");
        //上传文件
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        //返回上传文件位置
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
