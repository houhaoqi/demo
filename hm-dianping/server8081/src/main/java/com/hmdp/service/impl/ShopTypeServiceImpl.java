package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryList() {
        // 1.先从redis中查询， 固定前缀+店铺id
        List<String> shopTypes = stringRedisTemplate.opsForList().range(CACHE_SHOP_TYPE_KEY,0,-1);

        // 2.若不为空，全部转为ShopType类型返回
        if (!shopTypes.isEmpty()) {
            List<ShopType> tmp = new ArrayList<>();
            // 循环遍历
            for (String types : shopTypes) {
                ShopType shopType = JSONUtil.toBean(types,ShopType.class);
                tmp.add(shopType);
            }
            return Result.ok(tmp);
        }

        // 3.不存在，去数据库里查找
        List<ShopType> tmp = query().orderByAsc("sort").list();

        if (tmp == null) {
            return Result.fail("店铺名称不存在！");
        }

        // 4.查到了，转为json字符串，并存入redis
        for (ShopType shopType : tmp) {
            String jsonStr = JSONUtil.toJsonStr(shopType);
            shopTypes.add(jsonStr);
        }

        return Result.ok(tmp);
    }
}
