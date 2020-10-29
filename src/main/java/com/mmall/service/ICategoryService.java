package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

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


    /**
     * 查找指定id下面的分类信息，包括递归结果，比如，1 -> 10 -> 100,  如果id为1，返回的结果是10 和100
     * @param categoryId
     * @return
     */
    // 使用Set集合进行去重操作，注意需要重写Category的equals方法和hashCode方法
    ServerResponse<List<Integer>> selectCategoryAndDeepChildrenCategory(Integer categoryId);
}
