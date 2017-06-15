package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.services.ICategoryService;
import com.mmall.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Liuzy on 2017/6/15.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){

        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){

            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }else {
            if (iUserService.checkAdminRole(user).isSuccess()){

                return iCategoryService.addCategory(categoryName,parentId);
            }else {
                return ServerResponse.createByErrorErrorMessage("无权限,需要管理员");
            }
        }
    }

    @RequestMapping("set_categoryName.do")
    @ResponseBody
    public ServerResponse setCategotyName(HttpSession session,Integer categoryId,String categoryName){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){

            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }else {
            if (iUserService.checkAdminRole(user).isSuccess()) {

                return iCategoryService.updateCategory(categoryName, categoryId);
            } else {
                return ServerResponse.createByErrorErrorMessage("无权限,需要管理员");
            }
        }
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getSubcategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") int categoryId){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){

            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }else {
            if (iUserService.checkAdminRole(user).isSuccess()) {

                return iCategoryService.getSubCategoriesById(categoryId);
            } else {
                return ServerResponse.createByErrorErrorMessage("无权限,需要管理员");
            }
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndSubId(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }else {
            if (iUserService.checkAdminRole(user).isSuccess()) {

                return iCategoryService.getCategoryAndSub(categoryId);
            } else {
                return ServerResponse.createByErrorErrorMessage("无权限,需要管理员");
            }
        }
    }
}
