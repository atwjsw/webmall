package com.atwjsw.mmall.service;

import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.pojo.Product;
import com.atwjsw.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * Created by wenda on 6/2/2017.
 */
public interface IProductService {
    public ServerResponse saveOrUpdateProudct(Product product);
    public ServerResponse setSaleStatus(Integer productId, Integer status);
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}