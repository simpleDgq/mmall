package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface IOrderService {

    /**
     * 支付订单
     * @param userId
     * @param orderNo
     * @param path
     * @return
     */
    ServerResponse pay(Integer userId, Long orderNo, String path);
}
