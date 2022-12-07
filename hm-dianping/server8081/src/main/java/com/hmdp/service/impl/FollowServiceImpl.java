package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserService userService;


    /**
     * 实现取关/关注
     * @param followUserId 博主id
     * @param isFollow 关注或取关行为
     * @return
     */
    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        // 1.获取当前用户id
        Long userId = UserHolder.getUser().getId();
        String key = "follows:" + userId;
        // 2.判断是否关注
        if (isFollow){
            // 关注，保存信息到数据库
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            // 保存成功
            boolean isSuccess = save(follow);
            if (isSuccess){
                stringRedisTemplate.opsForSet().add(key,followUserId.toString());
            }
        } else {
            // 取关，将信息从数据库移除
            LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Follow::getUserId,userId).eq(Follow::getFollowUserId,followUserId);
            // 取关成功
            boolean isSuccess = remove(lambdaQueryWrapper);
            // 将数据写入redis
            if (isSuccess){
                stringRedisTemplate.opsForSet().add(key,followUserId.toString());
            }
        }
        // 3.返回
        return Result.ok();
    }

    /**
     * 判断当前用户是否关注了该博主
     * @param followUserId 查看是否关注
     * @return
     */
    @Override
    public Result isFollow(Long followUserId) {
        // 1.获取当前用户id
        Long userId = UserHolder.getUser().getId();
        LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 2.查询当前用户是否关注该博主
        lambdaQueryWrapper.eq(Follow::getUserId,userId).eq(Follow::getFollowUserId,followUserId);
        // 3.只需根据count的值即可判断 count>0
        int count = this.count(lambdaQueryWrapper);

        return Result.ok(count > 0);
    }

    @Override
    public Result followCommons(Long id) {
        // 获取自身userId 和传入的用户详情页id
        Long userId = UserHolder.getUser().getId();
        String key1 = "follows:"+userId;
        String key2 = "follows:"+id;
        //对当前用户和博主用户的关注列表取交集
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key1, key2);
        if (intersect == null || intersect.isEmpty()) {
            //无交集就返回个空集合
            return Result.ok(Collections.emptyList());
        }
        //将结果转为list
        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        //之后根据ids去查询共同关注的用户，封装成UserDto再返回
        List<UserDTO> userDTOS = userService.listByIds(ids).stream().map(user ->
                BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());
        return Result.ok(userDTOS);
    }
}
