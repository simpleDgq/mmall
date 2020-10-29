package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 添加或者更新商品
     *
     * @param product
     * @return
     */
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null) {
            if(StringUtils.isNotBlank(product.getSubImages())) {
                String[] imagesArray = product.getSubImages().split(",");
                if(imagesArray.length > 0) {
                    product.setMainImage(imagesArray[0]); // 设置main image为sub image的第一个值
                }
            }

            if(product.getId() != null) { // 如果传进来的product中，存在id属性，则是做更新操作，否则做添加操作
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("商品信息更新成功");
                }
                return ServerResponse.createByErrorMessage("商品信息更新失败");
            } else { // 添加商品
                int rowCount = productMapper.insert(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("商品信息添加成功");
                }
                return ServerResponse.createByErrorMessage("商品信息添加失败");
            }
        }
        return ServerResponse.createByErrorMessage("添加或新增商品参数不正确");
    }
}
