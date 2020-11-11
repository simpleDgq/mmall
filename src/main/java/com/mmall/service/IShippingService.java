package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {
    /**
     * 添加地址
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<String> del(Integer userId,Integer shippingId);

}
