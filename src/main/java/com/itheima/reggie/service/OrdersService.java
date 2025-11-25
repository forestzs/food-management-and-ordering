package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;
import org.springframework.stereotype.Service;

@Service
public interface OrdersService extends IService<Orders> {

    /**
     * 购物车下单业务接口
     * @param orders
     * @return
     */
    void submit(Orders orders);
}
