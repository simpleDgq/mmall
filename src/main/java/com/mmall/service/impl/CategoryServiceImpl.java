package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 添加分类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);

        // 插入category
        int resCount = categoryMapper.insert(category);
        if(resCount > 0) {
            return ServerResponse.createBySuccessMessage("品类添加成功");
        }
        return ServerResponse.createByErrorMessage("品类添加失败");
    }

    /**
     * 更新category名字
     *
     * @param id
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse updateCategoryName(Integer id, String categoryName) {
        if(id == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        Category category = new Category();
        category.setId(id);
        category.setName(categoryName);

        //更新
        int resCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resCount > 0) {
            return ServerResponse.createBySuccessMessage("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");

    }

    /**
     * 查找指定的id下面的所有分类信息,并且不递归,保持平级
     *
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(parentId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }
}
