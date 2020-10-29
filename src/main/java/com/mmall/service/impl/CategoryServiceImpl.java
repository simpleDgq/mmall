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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * 查找指定id下面的分类信息，包括递归结果，比如，1 -> 10 -> 100,  如果id为1，返回的结果是10 和100
     *
     * @param categoryId
     * @return
     */
    @Override
    // 使用Set集合进行去重操作，注意需要重写Category的equals方法和hashCode方法
    public ServerResponse<List<Integer>> selectCategoryAndDeepChildrenCategory(Integer categoryId) {
        Set<Category> categorySet = new HashSet<Category>();
        findChildCategory(categorySet,categoryId); // 递归查找所有的子节点信息

        // 拿到所有的id信息返回
        List<Integer> integerList = new ArrayList<Integer>();
        for(Category categoryItem : categorySet) {
            integerList.add(categoryItem.getId());
        }
        return ServerResponse.createBySuccess(integerList);
    }

    /**
     * 递归查找给定的id的所有子节点category信息。
     *
     * @param categorySet
     * @param categoryId
     * @return
     */
    // 这里使用Set集合能够帮助我们去重，但是要注意重写Category的equals方法和hashCode方法
    public Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId); // 查询当前节点
        if(category != null) { // 将当前节点信息放入set集合
            categorySet.add(category);
        }

        // MyBatis返回的集合即使没有数据，也不会是null，MyBatis做了处理，所以不用担心空指针异常
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);// 查询当前节点的第一级子节点
        for (Category categoryItem : categoryList ) { // 递归查询每个节点的子节点信息
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
