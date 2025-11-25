package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 移动端发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}", code);

            //实际应用中会调用云服务提供的短信服务API发送短信
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);

            //将生成的验证码保存到session
            session.setAttribute(phone, code);
            return R.success("验证码发送成功");
        } else {
            return R.error("验证码发送失败");
        }
    }

    /**
     * 移动端登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        log.info(map.toString());
        //获取页面发送的手机号和验证码
        String sendPhone = map.get("phone");
        String sendCode = map.get("code");
        //比对session中保存的验证码
        String saveCode = session.getAttribute(sendPhone).toString();

        if(saveCode != null && saveCode.equals(sendCode)) {
            //登录成功
            //根据手机号判断是否是新用户，如果是则自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, sendPhone);
            User user = userService.getOne(queryWrapper);
            if(user == null) {
                user = new User();
                user.setPhone(sendPhone);
                user.setStatus(1);
                userService.save(user);
            }
            //老用户直接登录成功
            session.setAttribute("user", user.getId());
            return R.success(user);
        } else {
            //登陆失败
            return R.error("验证码错误");
        }
    }
}
