package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 添加购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        // 查询要添加的到购物车的商品是不是已经存在于购物车中
        Cart cart = cartMapper.selectByUserIdProductId(userId, productId);
        if(cart == null) { // 不在购物车中
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);

            cartMapper.insert(cartItem); // 插入到数据库
        } else { // 在购物车中，需要更新数量
            count = cart.getQuantity() + count; // 新的数量加上数据库中存在的数量
            cart.setQuantity(count);
            // 更新购物车数据库中该商品的数量
            cartMapper.updateByPrimaryKey(cart);
        }
        return  this.listProduct(userId);
    }

    /**
     * 购物车核心方法
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectByUserId(userId);// 查询当前userId下，购物车中商品list
        List<CartProductVo> cartProductVoList = new ArrayList<CartProductVo>();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)) {
            for(Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setUserId(cartItem.getUserId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId()); // 查询购物车中每件商品的详细信息
                if(product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    // 判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()) { // 商品库存大于等于购物车商品数
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUMBER_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUMBER_FAIL);
                        // 购物车中该商品的数量更新为有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    // 计算整个购物车中商品的总价，包括勾选的和未勾选的
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),
                            buyLimitCount));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked() == Const.Cart.CHECKED) {
                    // 计算勾选的物品的总价
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),
                            cartProductVo.getProductTotalPrice().doubleValue());
                }

                cartProductVoList.add(cartProductVo);
            }
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setAllChecked(getAllCheckedStatus(userId));
            cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        }
        return cartVo;
    }

    /**
     * 判断购物车的商品是不是全部被选中
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if(userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusBuUserId(userId) == 0; // 全选状态
    }

    /**
     * 更新购物车产品数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        // 查询要更新的商品是不是存在
        Cart cart = cartMapper.selectByUserIdProductId(userId, productId);
        if(cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return  this.listProduct(userId);
    }

    /**
     * 删除购物车中的产品
     * @param userId
     * @param productIds
     * @return
     */
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(',').splitToList(productIds); // guava
        if(CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        // 删除操作
        cartMapper.deleteByUserIdAdnProductIds(userId, productIdList);
        // 从数据库中获取最新的信息
        return  this.listProduct(userId);
    }


    /**
     * 查询购物车中的商品信息
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> listProduct(Integer userId) {
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 全选或不全选购物车中的商品
     * @param userId
     * @param checked
     * @return
     */
    public ServerResponse<CartVo> selectOrUnSelect (Integer userId, Integer productId, Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId, checked);
        return  this.listProduct(userId);
    }

    /**
     * 获取购物车中商品的数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> getCartProductCount (Integer userId){
        if(userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        Integer count = cartMapper.selectCartProductCount(userId);
        return  ServerResponse.createBySuccess(count);
    }

}
