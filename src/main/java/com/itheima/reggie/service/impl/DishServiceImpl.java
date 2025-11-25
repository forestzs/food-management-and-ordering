package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时插入口味，操作dish和dish_flavor的业务实现
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品数据
        this.save(dishDto);
        //获取菜品的id
        Long dishId = dishDto.getId();
        //根据菜品的id赋值菜品的口味
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        dishFlavorList = dishFlavorList.stream().peek((item) ->
                item.setDishId(dishId)).collect(Collectors.toList());

        //保存菜品口味数据(集合)
        dishFlavorService.saveBatch(dishFlavorList);
    }

    /**
     * 根据id查询菜品及口味信息的业务实现
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品信息，from dish table
        Dish dish = this.getById(id);
        //查询口味信息,from dish_flavor table
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);

        //拷贝公共属性
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //设置私有属性
        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    /**
     * 更新菜品
     * @param dishDto
     * @return
     */
    @Override
    public DishDto updateWithFlavor(DishDto dishDto) {
        //更新菜品表
        this.updateById(dishDto);
        //清楚当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加修改后的菜品口味信息
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        dishFlavorList = dishFlavorList.stream().peek((item) ->
                item.setDishId(dishDto.getId())).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishFlavorList);
        return null;
    }

    /**
     * 删除菜品
     * @param ids
     */
    public void removeDish(List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, 1);
        if(this.count(queryWrapper) > 0) {
            throw new CustomException("存在启售中的菜品，无法删除"); //不能删除，直接抛出业务异常
        } else {
            this.removeByIds(ids);
        }
    }
}
