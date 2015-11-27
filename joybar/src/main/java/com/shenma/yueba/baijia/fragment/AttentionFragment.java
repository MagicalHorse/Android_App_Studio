package com.shenma.yueba.baijia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.newmodel.BuyerProductsBackBean;
import com.shenma.yueba.baijia.modle.newmodel.FavBuyersBackBean;
import com.shenma.yueba.baijia.modle.newmodel.OtherBuyersBackBean;
import com.shenma.yueba.baijia.modle.newmodel.RecommondBuyerlistBackBean;
import com.shenma.yueba.baijia.modle.newmodel.SearchMarketBackBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.yangjia.adapter.MarketForSearchAdapter;

import config.PerferneceConfig;

/**
 * 找导购--关注
 * Created by a on 2015/11/23.
 */
public class AttentionFragment extends BaseFragment{
    private int page = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getAttentionBuyer();
       // getBuyersProducts("30944");
       // getOtherStoreBuyers("30944");
       // getRecommondBuyerlist("30944");
        touch("30944");
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    /**
     *获取关注的买手
     */
    public void getAttentionBuyer(){
        HttpControl httpControl = new HttpControl();
        httpControl.getFavBuyers(page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                FavBuyersBackBean bean = (FavBuyersBackBean) obj;


            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }

    /**
     *获取买手的产品数据
     */
    public void getBuyersProducts(String buyerId){
        HttpControl httpControl = new HttpControl();
        httpControl.getBuyersProducts(buyerId, page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                BuyerProductsBackBean bean = (BuyerProductsBackBean) obj;


            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }


    /**
     *同商场的其他买手
     */
    public void getOtherStoreBuyers(String buyerId){
        HttpControl httpControl = new HttpControl();
        httpControl.getOtherStoreBuyers(buyerId, page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                OtherBuyersBackBean bean = (OtherBuyersBackBean) obj;


            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }


    /**
     *推荐的买手/导购
     */
    public void getRecommondBuyerlist(String buyerId){
        HttpControl httpControl = new HttpControl();
        httpControl.getRecommondBuyerlist(buyerId, page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RecommondBuyerlistBackBean bean = (RecommondBuyerlistBackBean) obj;


            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }


    /**
     *戳一下
     */
    public void touch(String buyerId){
        HttpControl httpControl = new HttpControl();
        httpControl.touch(buyerId, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                BaseRequest bean = (BaseRequest) obj;
                if(bean.isSuccessful()){
                    Toast.makeText(getActivity(),"提醒成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"提醒失败！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }

}
