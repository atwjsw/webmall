package com.atwjsw.mmall.service;

import com.atwjsw.mmall.common.ServerResponse;
import com.atwjsw.mmall.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Created by wenda on 6/8/2017.
 */
public interface IOrderService {
    public ServerResponse pay(Long orderNo, Integer userId, String path);
    public ServerResponse aliCallback(Map<String, String> params);
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
    public ServerResponse createOrder(Integer userId, Integer shippingId);
    public ServerResponse<String> cancelOrder(Integer userId, Long orderNo);
    public ServerResponse getOrderCartProduct(Integer userId);
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    //backend
    public ServerResponse<PageInfo> manageList(int pageNum, int pageSize);
    public ServerResponse<OrderVo> manageDetail(Long orderNo);
    public ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);
    public ServerResponse<String> manageSendGoods(Long orderNo);
}
