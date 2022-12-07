package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.MailUtils;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;
import static com.hmdp.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // redis ==> session 存取验证码
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送短信验证码
     * @param phone
     * @param session
     * @return
     */
    @Override
    public Result sendCode(String phone, HttpSession session){
        // 1.校验手机号
        if (RegexUtils.isPhoneInvalid(phone)){
            // 2.不符合，返回错误信息
            return Result.fail("格式错误！");
        }
        // 3.符合生成验证码
        String code = RandomUtil.randomNumbers(6);

//        // 4.保存验证码到session
//        session.setAttribute("code",code);
        // 4.保存验证码到redis  === 1.用户token+phone 2.发送的验证码 3.code过期时间
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);


        // 5.发送验证码
        log.info("发送验证码：{}",code);
//        MailUtils.sendTestMail(phone,code); //发送到邮箱

        return null;
    }

    /**
     * 登录
     * @param loginForm
     * @param session
     * @return
     */
    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1.获取手机和验证码
        String phone = loginForm.getPhone();
        String code  = loginForm.getCode();

        // 2.校验手机号
        if (RegexUtils.isPhoneInvalid(phone)){
            // 不符合，返回错误信息
            return Result.fail("格式错误！");
        }
        // 3.校验验证码
//        String cacheCode = (String) session.getAttribute("code");  //session
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY+phone);
        if (cacheCode == null || !cacheCode.equals(code)){
            // 验证码错误
            return Result.fail("验证码错误!");
        }

        // 4.根据手机号查询用户
        User user = query().eq("phone",phone).one();

        // 5.一致,判断用户是否存在
        if (user == null){
            // 不存在,调用创建用户方法
            user = createUserWithPhone(phone);
        }

//        // 6.保存用户信息到session
//        session.setAttribute("user", BeanUtil.copyProperties(user, UserDTO.class));
        // 6.保存用户到redis中
        // 6.1随机生成token
        String token = UUID.randomUUID().toString(true);
        // 6.2将User对象转为HashMap存储
        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
        HashMap<String,String> map = new HashMap<>();
        map.put("icon", userDTO.getIcon());
        map.put("id", String.valueOf(userDTO.getId()));
        map.put("nickName", userDTO.getNickName());

        // 6.3存储
        String key = LOGIN_USER_KEY+token;
        stringRedisTemplate.opsForHash().putAll(key,map);
        // 6.4设置token有效期
        stringRedisTemplate.expire(key,30,TimeUnit.MINUTES);
        //7.5 登陆成功则删除验证码信息
        stringRedisTemplate.delete(LOGIN_CODE_KEY + phone);

        return Result.ok(token);
    }

    /**
     *
     * @return 登出用户
     */
    @Override
    public Result logout() {
        String key = UserHolder.getUser().getId().toString();
        stringRedisTemplate.delete(key);
        return Result.ok("登出成功！");
    }

    /**
     * 用户签到功能
     * @return
     */
    @Override
    public Result sign() {
        //1. 获取当前用户
        Long userId = UserHolder.getUser().getId();
        //2. 获取日期
        LocalDateTime now = LocalDateTime.now();
        //3. 拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        //4. 获取今天是当月第几天(1~31)
        int dayOfMonth = now.getDayOfMonth();
        //5. 写入Redis  BITSET key offset 1
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        return Result.ok();
    }

    /**
     * 创建用户
     * @param phone
     * @return
     */
    private User createUserWithPhone(String phone) {

        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        log.info("新增用户User:{}",user);
        save(user);

        return user;
    }


}
