package com.atwjsw.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wenda on 6/5/2017.
 */
public class CartVo {

    private List<CartProductVo> cartProductVoList;
    private Boolean allChecked; //是否已经都勾选
    private BigDecimal cartTotalPrice;
    private String imageHost; //图片存成主机

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    @Override
    public String toString() {
        return "CartVo{" +
                "cartProductVoList=" + cartProductVoList +
                ", allChecked=" + allChecked +
                ", cartTotalPrice=" + cartTotalPrice +
                ", imageHost='" + imageHost + '\'' +
                '}';
    }
}
