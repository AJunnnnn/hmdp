package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 根据id查询商铺信息
     * @param id 商铺id
     * @return 商铺详情数据
     */
    @Override
    public Result queryById(Long id) {
        //先从redis中查看是否存在
        String jsonString = redisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        //如果存在直接返回
        if (jsonString != null){
            Shop shop = JSONUtil.toBean(jsonString, Shop.class);
            return Result.ok(shop);
        }

        //不存在，从数据库中查询，并写入redis
        Shop shop = getById(id);
        // 数据库中不存在这个shop
        if (shop == null){
            return Result.fail("店铺不存在");
        }
        // 添加到redis中
        String shopJson = JSONUtil.toJsonStr(shop);
        redisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, shopJson);

        return Result.ok(shop);
    }
}
