package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    /**
     * 添加分类
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse addCategory(String categoryName, Integer parentId);

    /**
     * 更新category名字
     * @param id
     * @param categoryName
     * @return
     */
    ServerResponse updateCategoryName(Integer id, String categoryName);

    /**
     * 查找指定的parentId下面的所有分类信息,并且不递归,保持平级
     * @param parentId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);
}
