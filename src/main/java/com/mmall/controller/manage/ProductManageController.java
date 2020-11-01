package com.mmall.controller.manage;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {


    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    /**
     * 管理员添加商品
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "product_save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        // 校验用户是否登录
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }

        // 验证用户权限
        ServerResponse response = iUserService.checkAdminRole(user);
        if(!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

        // 更新或者添加商品
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 设置商品的status
     *
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        // 校验用户是否登录
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }

        // 验证用户权限
        ServerResponse response = iUserService.checkAdminRole(user);
        if(!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

        return iProductService.setSaleStatus(productId, status);
    }


    /**
     * 获取商品详细信息
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "get_product_detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductDetail(HttpSession session, Integer productId) {
        // 校验用户是否登录
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }

        // 验证用户权限
        ServerResponse response = iUserService.checkAdminRole(user);
        if(!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

        return iProductService.manageProductDetail(productId);
    }


    /**
     * 分页查询商品列表信息
     *
     * @param session
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return
     */
    @RequestMapping(value = "get_product_list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        // 校验用户是否登录
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }

        // 验证用户权限
        ServerResponse response = iUserService.checkAdminRole(user);
        if(!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

        return iProductService.getProductList(pageNum, pageSize);
    }


    /**
     * 搜索商品信息
     *
     * @param session
     * @param productName 根据productName搜索
     * @param productId 根据productId搜索
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "product_search.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        // 校验用户是否登录
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }

        // 验证用户权限
        ServerResponse response = iUserService.checkAdminRole(user);
        if(!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

        return iProductService.productSearch(productName, productId, pageNum, pageSize);
    }

}
