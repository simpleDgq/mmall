package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {

    /**
     * 添加或者更新商品
     * @param product
     * @return
     */
    ServerResponse<String> saveOrUpdateProduct(Product product);

    /**
     * 设置商品的status信息
     *
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
}
