package com.lin.stock.service.Impl;

import com.lin.stock.mapper.SysUserMapper;
import com.lin.stock.pojo.entity.SysUser;
import com.lin.stock.service.UserService;
import com.lin.stock.vo.req.LoginReqVo;
import com.lin.stock.vo.resp.LoginRespVo;
import com.lin.stock.vo.resp.R;
import com.lin.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 定义用户服务实现
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public SysUser getUserByUserName(String userName) {
        SysUser user = sysUserMapper.findByUserName(userName);
        return user;
    }

    /**
     * 用户登录
     *
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //l.判断用户参数是否和合法
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //2.根据用户名去数据库查询用户信息，获取密码的密文
        SysUser user = this.sysUserMapper.findByUserName(vo.getUsername());
        //判断用户是否存在，存在则密码校验
        if (user == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }

        if (!passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //组装登录成功数据
        LoginRespVo respVo = new LoginRespVo();
        //属性名称与类型必须相同，否则属性值无法copy LoginRespVo与SysUser对象属性名称和类型一致
        BeanUtils.copyProperties(user, respVo);
        return R.ok(respVo);
    }

}
