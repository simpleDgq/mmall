package com.mmall.service;

import com.github.pagehelper.PageInfo;
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

    /**
     * 更新地址
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse update(Integer userId, Shipping shipping);

    /**
     * 查询地址信息
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    /**
     * 分页查询地址信息
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
