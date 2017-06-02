package com.atwjsw.mmall.service;

import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.Category;

import java.util.List;

/**
 * Created by wenda on 6/1/2017.
 */
public interface ICategoryService {
    public ServerResponse addCategory (Integer parentId, String categoryName);
    public ServerResponse setCategoryName (Integer categoryId, String categoryName);
    public ServerResponse<List<Category>> getChildrenParallelCategory (Integer categoryId);
    public ServerResponse<List<Category>> selectCategoryAndChildrenById (Integer categoryId);

}
