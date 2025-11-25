package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入口味，操作dish和dish_flavor的业务接口
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品及口味信息的业务接口
     * @return DishDto
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品及口味信息
     * @return dishDto
     */
    DishDto updateWithFlavor(DishDto dishDto);

    /**
     * 删除菜品
     * @param ids
     */
    void removeDish(List<Long> ids);
}
