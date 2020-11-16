package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

public interface IOrderService {

    /**
     * 支付订单
     * @param userId
     * @param orderNo
     * @param path
     * @return
     */
    ServerResponse pay(Integer userId, Long orderNo, String path);

    /**
     * alipay回调验签成功后，执行的函数
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String,String> params);

    /**
     * 查询订单支付状态
     * @param userId
     * @param orderId
     * @return
     */
    ServerResponse queryOrderPayStatus(Integer userId, Long orderId);
}
