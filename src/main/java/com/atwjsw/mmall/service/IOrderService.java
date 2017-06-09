package com.atwjsw.mmall.service;

import com.atwjsw.mmall.common.ServerResponse;

import java.util.Map;

/**
 * Created by wenda on 6/8/2017.
 */
public interface IOrderService {
    public ServerResponse pay(Long orderNo, Integer userId, String path);
    public ServerResponse aliCallback(Map<String, String> params);
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
