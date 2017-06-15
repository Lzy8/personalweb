package com.mmall.services.impl;

import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.services.IUserService;
import com.mmall.util.MD5Util;
import com.sun.tools.internal.xjc.reader.Const;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.invoke.empty.Empty;

import java.util.UUID;

/**
 * Created by Liuzy on 2017/6/8.
 */
@Service("iUserService")
public class UserServiceimpl implements IUserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);

        if (resultCount == 0){
            return  ServerResponse.createByErrorErrorMessage("用户名不存在");
        }

        //todo pwd login md5

        String md5String = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username,md5String);

        if (user == null){
            return ServerResponse.createByErrorErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    public ServerResponse<String> register(User user){

        ServerResponse response = this.checkValid(user.getUsername(), com.mmall.common.Const.username);

        if (!response.isSuccess()){
            return response;
        }

        response = this.checkValid(user.getEmail(), com.mmall.common.Const.email);

        if (!response.isSuccess()){
            return response;
        }
        user.setRole(com.mmall.common.Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user); //数据库操作

        if (resultCount == 0){

            return ServerResponse.createByErrorErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {

        if (StringUtils.isNotBlank(type)){
            if (type.equals(com.mmall.common.Const.username)){
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorErrorMessage("用户已存在"); //response 响应操作
                }
            }
            if (type.equals(com.mmall.common.Const.email)){
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorErrorMessage("邮箱已存在"); //response 响应操作
                }
            }
        }else {
            return ServerResponse.createByErrorErrorMessage("参数不合法");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> findPwdByQuestion(String name){

        ServerResponse response = this.checkValid(name, com.mmall.common.Const.username);

        if (!response.isSuccess()){

            String result = userMapper.checkPwdQuestion(name);

            return ServerResponse.createBySuccess(result);
        }
        return ServerResponse.createByErrorErrorMessage("用户名不存在");

    }

    public ServerResponse<String> checkPwdAnswer(String username,String question,String answer){

        int resultCount = userMapper.checkPwdAnswer(username, question, answer);
        if (resultCount > 0){

            String token = UUID.randomUUID().toString();
            TokenCache.setKey("token_"+username,token);

            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorErrorMessage("答案不正确滴");

    }

    public ServerResponse<String> resetPassword(String username,String newPassword,String forgetToken){

        ServerResponse response = this.checkValid(username, com.mmall.common.Const.username);

        if (response.isSuccess()){

            return response;
        }

        if(!StringUtils.isBlank(forgetToken)){

            String token = TokenCache.getValueForKey("token_"+username);

            if (forgetToken.equals(token)){

                String md5pwd = MD5Util.MD5EncodeUtf8(newPassword);

                int resultCount = userMapper.updatePassword(username,md5pwd);

                if (resultCount > 0){
                    return ServerResponse.createBySuccessMessage("密码修改成功");
                }else {
                    return ServerResponse.createByErrorErrorMessage("插入数据库时出错了");
                }

            }else {
                return ServerResponse.createByErrorErrorMessage("token不正确");
            }

        }else { //token 为空

            return ServerResponse.createByErrorErrorMessage("token已失效");
        }
    }

    public ServerResponse<String> resetPasswordOnLogin(User user,String originalPassword,String newPassword){

        String md5OriginalPassword = MD5Util.MD5EncodeUtf8(originalPassword);
        int resultCount = userMapper.checkPassword(user.getUsername(),md5OriginalPassword);

        if (resultCount > 0){
            user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
            resultCount = userMapper.updateByPrimaryKeySelective(user);
            if (resultCount > 0){
                return ServerResponse.createBySuccessMessage("密码修改成功");
            }else {
                return ServerResponse.createByErrorErrorMessage("插入数据库时出错了,吗的");
            }
        }else {
            return ServerResponse.createByErrorErrorMessage("密码不正确");
        }
    }

    public ServerResponse<User> getInformation(Integer id){

        User user = userMapper.selectByPrimaryKey(id);
        if (user != null){

            user.setPassword(StringUtils.EMPTY);
            return ServerResponse.createBySuccess("成功",user);
        }else {
            return ServerResponse.createByErrorErrorMessage("用户不存在");
        }
    }


    //backend

    public ServerResponse checkAdminRole(User user){
        if (user != null && user.getRole().intValue() == com.mmall.common.Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByError();
        }
    }

}
