package com.mmall.services;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by Liuzy on 2017/6/8.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse<String> findPwdByQuestion(String name);
    ServerResponse<String> checkPwdAnswer(String username,String question,String answer);
    ServerResponse<String> resetPassword(String username,String newPassword,String forgetToken);
    ServerResponse<String> resetPasswordOnLogin(User user,String originalPassword,String newPassword);
    ServerResponse<User> getInformation(Integer id);
    ServerResponse checkAdminRole(User user);
}
