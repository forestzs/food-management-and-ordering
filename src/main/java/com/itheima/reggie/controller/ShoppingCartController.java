package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 增加菜品 & 套餐到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info(shoppingCart.toString());
        //设置点餐者id
        Long customerId = BaseContext.getCurrentId();
        shoppingCart.setUserId(customerId);
        //查询添加的菜品/套餐在购物车中是否已存在
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, customerId);
        if(shoppingCart.getDishId() != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //如果购物车中已存在，设置购物车中umber + 1
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);
        if(shoppingCartOne != null) {
            Integer number = shoppingCartOne.getNumber() + 1;
            shoppingCartOne.setNumber(number);
            shoppingCartService.updateById(shoppingCartOne);
        } else {
            //不存在则新添到购物车，设置number = 1
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartOne = shoppingCart;
        }
        return R.success(shoppingCartOne);
    }

    /**
     * 减少菜品 & 套餐到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> subtract(@RequestBody ShoppingCart shoppingCart) {
        log.info(shoppingCart.toString());
        //设置点餐者id
        Long customerId = BaseContext.getCurrentId();
        shoppingCart.setUserId(customerId);
        //查询添加的菜品/套餐在购物车中是否已存在
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, customerId);
        if(shoppingCart.getDishId() != null) {
            //需要减少的是购物车的菜品
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //需要减少的是购物车的套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //如果购物车中菜品或套餐数量大于等于1，设置购物车中umber - 1
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);
        if(shoppingCartOne.getNumber() >= 1) {
            Integer number = shoppingCartOne.getNumber() - 1;
            shoppingCartOne.setNumber(number);
            shoppingCartService.updateById(shoppingCartOne);
            return R.success(shoppingCartOne);
        } else {
            //不存在则新添到购物车，设置number = 0
            shoppingCart.setNumber(0);
            shoppingCartService.removeById(shoppingCart);
            shoppingCartOne = shoppingCart;
        }
        return R.success(shoppingCartOne);
    }

    /**
     * 根据userId查看当前用户的购物车菜品 & 套餐
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        return R.success(shoppingCartService.list(queryWrapper));
    }

    /**
     * 清空购物车所有菜品 & 套餐
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("购物车已清空");
    }
}
