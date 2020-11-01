package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailsVo> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }
}
