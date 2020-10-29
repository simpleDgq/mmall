package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailsVo;

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

    /**
     * 获取商品详细信息
     *
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailsVo> manageProductDetail(Integer productId);

}
