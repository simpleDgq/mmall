package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailsVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加或者更新商品
     *
     * @param product
     * @return
     */
    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
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

    /**
     * 设置商品的status信息
     *
     * @param productId
     * @param status
     * @return
     */
    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null) { // 参数不正确
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        // 设置值
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0) {
            return ServerResponse.createBySuccessMessage("设置商品status成功");
        }
        return ServerResponse.createByErrorMessage("设置商品status失败");
    }

    /**
     * 获取商品详细信息
     *
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailsVo> manageProductDetail(Integer productId) {
        if(productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product != null) {
            ProductDetailsVo productDetailsVo =  assembleProductDetailsVo(product);
            return ServerResponse.createBySuccess("获取商品信息成功", productDetailsVo);
        }
        return ServerResponse.createByErrorMessage("产品已下架或删除");

    }

    /**
     * 填充ProductDetailsVo对象, 使用VO给前端返回数据
     *
     * @param product
     * @return
     */
    private ProductDetailsVo assembleProductDetailsVo(Product product){
        ProductDetailsVo productDetailVo = new ProductDetailsVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        // 设置FTP服务器地址
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认是根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        // 将时间戳转换为可视时间，返回给前端
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }


    /**
     * 使用MyBatis pageHelper分页查询商品列表信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {

        // start helper page
        PageHelper.startPage(pageNum, pageSize);
        // 调用自己的sql语句
        List<Product> productList = productMapper.selectProductList();
        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
        for (Product productItem : productList) { // 填充productListVo对象
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        // pageHelper收尾
        PageInfo pageInfo = new PageInfo(productList); // pageHelper进行分页计算
        pageInfo.setList(productListVoList); // 设置返回的list

        return ServerResponse.createBySuccess(pageInfo);// 返回数据pageInfo
    }


    /**
     * 填充ProductListVo对象
     *
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        // 设置FTP服务器地址
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        return productListVo;
    }

    /**
     * 根据productName和productId搜索商品信息
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> productSearch(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        // start helper page
        PageHelper.startPage(pageNum, pageSize);
        if(StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        // 调用自己的sql语句
        List<Product> productList = productMapper.selectProductByNameAndId(productName, productId);

        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
        for (Product productItem : productList) { // 填充productListVo对象
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        // pageHelper收尾
        PageInfo pageInfo = new PageInfo(productList); // pageHelper进行分页计算
        pageInfo.setList(productListVoList); // 设置返回的list

        return ServerResponse.createBySuccess(pageInfo);// 返回数据pageInfo

    }
}
