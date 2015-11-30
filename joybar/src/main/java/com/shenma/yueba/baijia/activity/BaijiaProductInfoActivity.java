package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.widget.FrameLayout;

import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.fragment.ApproveBuyerDetailsFragment;
import com.shenma.yueba.baijia.fragment.ApproveBuyerDetails_ck_Fragment;
import com.shenma.yueba.baijia.modle.CKProductDeatilsInfoBean;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.util.HttpControl;

/**
 * Created by Administrator on 2015/11/30.
 */
public class BaijiaProductInfoActivity extends FragmentActivity {
    FragmentManager fragmentManager;
    HttpControl httpControl=new HttpControl();
    boolean isShow=true;
    int productId=0;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        frameLayout=new FrameLayout(this);
        setContentView(frameLayout);
        fragmentManager=getSupportFragmentManager();
        this.getIntent().getIntExtra("productId", -1);
        if(productId<0)
        {
            MyApplication.getInstance().showMessage(this,"商品id错误");
            finish();
            return;
        }
        requestData();
    }

    void requestData()
    {
        httpControl.getCkrProductDetails(isShow,productId, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RequestCKProductDeatilsInfo bean=(RequestCKProductDeatilsInfo)obj;
                if(bean.getData()==null)
                {
                    http_Fails(400,"无商品信息");
                }else
                {
                    CKProductDeatilsInfoBean cKProductDeatilsInfoBean=bean.getData();
                    //如果是 认证
                    if(cKProductDeatilsInfoBean.getBuyerLeave().equals("8"))
                    {
                        ApproveBuyerDetails_ck_Fragment approveBuyerDetails_ck_fragment=new ApproveBuyerDetails_ck_Fragment();
                        BaijiaProductInfoActivity.this.getIntent().putExtra("ProductInfo",bean);
                        fragmentManager.beginTransaction().add(frameLayout.getId(),approveBuyerDetails_ck_fragment).commit();
                    }else//否则 专柜
                    {
                        ApproveBuyerDetailsFragment approveBuyerDetailsFragment=new ApproveBuyerDetailsFragment();
                        fragmentManager.beginTransaction().add(frameLayout.getId(), approveBuyerDetailsFragment).commit();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(BaijiaProductInfoActivity.this,msg);
                finish();
            }
        },this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }
}
