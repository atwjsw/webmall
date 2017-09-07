package com.atwjsw.mmall.service;

import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.vo.CartVo;

/**
 * Created by wenda on 6/5/2017.
 */
public interface ICartService {

    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);
    public ServerResponse<CartVo> list(Integer userId);
    public ServerResponse<CartVo> selectOrUnSelect (Integer userId, Integer productId, Integer checked);
    public ServerResponse<Integer> getCartProductCount(Integer userId);
}
