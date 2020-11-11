package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

public interface ICartService {
    /**
     * 添加商品到购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车商品数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    /**
     * 删除购物车中的商品
     * @param userId
     * @param productIds
     * @return
     */
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    /**
     * 查询购物车中的商品信息
     * @param userId
     * @return
     */
    ServerResponse<CartVo> listProduct(Integer userId);

    /**
     * 全选或者不全选购物车中的商品
     * @param userId
     * @param checked
     * @return
     */
    ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId, Integer checked);

    /**
     * 获取购物车中商品的数量
     * @param userId
     * @return
     */
    ServerResponse<Integer> getCartProductCount (Integer userId);
}
