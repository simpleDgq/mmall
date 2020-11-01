package com.mmall.service;

import com.github.pagehelper.PageInfo;
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

    /**
     * 使用MyBatis pageHelper分页查询商品列表信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    /**
     * 根据productName和productId搜索商品信息
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> productSearch(String productName, Integer productId, Integer pageNum, Integer pageSize);

}
