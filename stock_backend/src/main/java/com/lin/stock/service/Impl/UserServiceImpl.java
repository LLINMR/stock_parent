package com.lin.stock.service.Impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import com.alibaba.druid.sql.visitor.functions.If;
import com.lin.stock.constant.StockConstant;
import com.lin.stock.mapper.SysUserMapper;
import com.lin.stock.pojo.entity.SysUser;
import com.lin.stock.service.UserService;

import com.lin.stock.utils.IdWorker;
import com.lin.stock.vo.req.LoginReqVo;
import com.lin.stock.vo.resp.LoginRespVo;
import com.lin.stock.vo.resp.R;
import com.lin.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 定义用户服务实现
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 分布式环境保证生成id唯一
     */
    @Autowired
    private IdWorker idWorker;


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
        //2.校验验证码和session是否有效
        if (StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }

        //3.根据Rkey从redis中获取缓存的校验码(比较时忽略大小写)
        String rCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + vo.getSessionId());

        //判断获取的验证码是否存在，以及是否与输入的验证码相同
        if (StringUtils.isBlank(rCode) ) {
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        if (!rCode.equalsIgnoreCase(vo.getCode())) {
            //验证码输入有误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //4.根据用户名去数据库查询用户信息，获取密码的密文
        SysUser user = this.sysUserMapper.findByUserName(vo.getUsername());


        //5.判断用户是否存在，存在则密码校验
        if (user == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //6.如果存在，则获取密文密码，然后传入的明文进行匹配,判断是否匹配成功
        if (!passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //组装登录成功数据
        LoginRespVo respVo = new LoginRespVo();
        //属性名称与类型必须相同，否则属性值无法copy LoginRespVo与SysUser对象属性名称和类型一致
        BeanUtils.copyProperties(user, respVo);
        return R.ok(respVo);
    }

    /**
     * 登录校验码生成服务方法
     *
     * @return
     */
    @Override
    public R<Map> getCaptchaCode() {
        //1.生成图片验证码
        //参数分别是宽、高、验证码长度、干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色清灰
        captcha.setBackground(Color.lightGray);

//        captcha.setGenerator(new CodeGenerator() {
//            @Override
//            public String generate() {
//                //自定义校验码生成的逻辑
//                return RandomStringUtils.randomNumeric(4);
//            }
//
//            @Override
//            public boolean verify(String code, String userInputCode) {
//                //匹配校验码逻辑
//                return code.equalsIgnoreCase(userInputCode);
//            }
//        });

        //获取验证码
        String checkCode = captcha.getCode();
        //获取经过base64处理的图片数据
        String imageData = captcha.getImageBase64();
        //2.生成sessionId 转化成String,避免前端精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的图片校验码:{},会话id:{}", checkCode, sessionId);
        //3.将sessionId和校验码保存在redis下，并设置缓存中数据存活时间一分钟 使用redis模拟session的行为，通过过期时间设置
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX + sessionId, checkCode, 5, TimeUnit.MINUTES);
        //4.组装数据
        HashMap<String, String> info = new HashMap<>();
        info.put("sessionId", sessionId);
        info.put("imageData", imageData);

        //设置响应数据格式
        return R.ok(info);
    }

}
