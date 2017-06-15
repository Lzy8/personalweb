package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.services.IUserService;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Liuzy on 2017/6/8.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){

        ServerResponse<User> response = iUserService.login(username,password);

        if (response.isSuccess()){

            session.setAttribute(Const.CURRENT_USER,response.getData());
        }

        return response;

    }

    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout (HttpSession session){

        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){

        return iUserService.checkValid(str,type);

    }

    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorErrorMessage("用户未登陆,无法获取信息");
    }

    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> findPwdByQuestion(String name){
        return iUserService.findPwdByQuestion(name);
    }

    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkPwdAnswer(String username,String question,String answser){
        return iUserService.checkPwdAnswer(username, question, answser);
    }

    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String username,String newPassword,String forgetToken){

        return iUserService.resetPassword(username, newPassword, forgetToken);
    }
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPasswordOnLogin(HttpSession session,String originalPassword,String newPassword){

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorErrorMessage("用户还木有登录哦");
        }

        return iUserService.resetPasswordOnLogin(user,originalPassword,newPassword);
    }

    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorErrorMessage("未登录");
        }

        return iUserService.getInformation(user.getId());
    }



}
