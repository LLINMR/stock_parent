package com.lin.stock.controller;

import com.lin.stock.pojo.entity.SysUser;
import com.lin.stock.service.UserService;
import com.lin.stock.vo.req.LoginReqVo;
import com.lin.stock.vo.resp.LoginRespVo;
import com.lin.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return
     */
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String userName) {
        return userService.getUserByUserName(userName);
    }

    /**
     * 用户登录
     * @param vo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo) {

        R<LoginRespVo> r = userService.login(vo);
        return r;

    }
}
