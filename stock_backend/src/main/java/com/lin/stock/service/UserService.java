package com.lin.stock.service;

import com.lin.stock.pojo.entity.SysUser;
import com.lin.stock.vo.req.LoginReqVo;
import com.lin.stock.vo.resp.LoginRespVo;
import com.lin.stock.vo.resp.R;

public interface UserService {
    /**
     * 根据用户查询用户信息
     *
     * @param userName 用户名称
     * @return
     */
    SysUser getUserByUserName(String userName);

    /**
     * 用户登录
     *
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);
}
