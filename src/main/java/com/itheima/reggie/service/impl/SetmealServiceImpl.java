package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时保存套餐与菜品关联关系的业务实现
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的信息，Setmeal表执行insert
        this.save(setmealDto);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList = setmealDishList.stream().peek((item) -> item.setSetmealId(setmealDto.getId())).collect(Collectors.toList());

        //保存套餐和菜品关联信息，Setmeal_dish表执行insert
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * 删除套餐时同时删除套餐下的菜品业务实现
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐是否可以删除，可售状态不可删除
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.in(Setmeal::getId, ids);
        setmealQueryWrapper.eq(Setmeal::getStatus, 1);

        if(this.count(setmealQueryWrapper) > 0) {
            throw new CustomException("存在启售中的套餐，无法删除"); //不能删除，直接抛出业务异常
        } else {
            //可以删除先删除setmeal表数据
            this.removeByIds(ids);
            //继续删除套餐菜品关系表setmeal_dish数据
            LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishQueryWrapper.in(SetmealDish::getSetmealId, ids);
            setmealDishService.remove(setmealDishQueryWrapper);
        }
    }
}
