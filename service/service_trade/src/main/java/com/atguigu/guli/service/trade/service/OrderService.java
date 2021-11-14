package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Helen
 * @since 2021-10-18
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String id);
    Order getByOrderId(String orderId, String memberId);

    Boolean isBuyByCourseId(String courseId, String memberId);
}
