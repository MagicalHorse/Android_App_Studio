package com.shenma.yueba.baijia.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.GalleryAdapter;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.newmodel.BuyerInfo;
import com.shenma.yueba.baijia.modle.newmodel.BuyerProductsBackBean;
import com.shenma.yueba.baijia.modle.newmodel.FavBuyersBackBean;
import com.shenma.yueba.baijia.modle.newmodel.OtherBuyersBackBean;
import com.shenma.yueba.baijia.modle.newmodel.RecommondBuyerlistBackBean;
import com.shenma.yueba.util.HttpControl;

import java.util.ArrayList;
import java.util.List;

/**
 * 找导购--关注
 * Created by a on 2015/11/23.
 */
public class AttentionFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    private int page = 1;
    private GalleryAdapter galleryAdapter;
    private List<BuyerInfo> mList = new ArrayList<BuyerInfo>();
    private ImageView iv_arrow_left, iv_arrow_right;
    private Gallery gallery;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.attention_layout, null);
        gallery = (Gallery) view.findViewById(R.id.gallery);
        iv_arrow_left = (ImageView) view.findViewById(R.id.iv_arrow_left);
        iv_arrow_right = (ImageView) view.findViewById(R.id.iv_arrow_right);
        iv_arrow_left.setOnClickListener(this);
        iv_arrow_right.setOnClickListener(this);
        gallery.setSpacing(10);
        gallery.setOnItemSelectedListener(this);
        galleryAdapter = new GalleryAdapter(getActivity(), mList);
        gallery.setAdapter(galleryAdapter);
        getAttentionBuyer();
        // getBuyersProducts("30944");
        // getOtherStoreBuyers("30944");
        // getRecommondBuyerlist("30944");
       // touch("30944");
        return view;

    }


    /**
     * 获取关注的买手
     */
    public void getAttentionBuyer() {
        HttpControl httpControl = new HttpControl();
        httpControl.getFavBuyers(page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                FavBuyersBackBean bean = (FavBuyersBackBean) obj;
                if (bean != null && bean.getData() != null) {
                    List<BuyerInfo> dataList = bean.getData().getBuyers();
                    if (dataList != null) {
                        mList.addAll(dataList);
                       galleryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }

    /**
     * 获取买手的产品数据
     */
    public void getBuyersProducts(String buyerId) {
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
     * 同商场的其他买手
     */
    public void getOtherStoreBuyers(String buyerId) {
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
     * 推荐的买手/导购
     */
    public void getRecommondBuyerlist(String buyerId) {
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
     * 戳一下
     */
    public void touch(String buyerId) {
        HttpControl httpControl = new HttpControl();
        httpControl.touch(buyerId, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                BaseRequest bean = (BaseRequest) obj;
                if (bean.isSuccessful()) {
                    Toast.makeText(getActivity(), "提醒成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "提醒失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        galleryAdapter.setSelectItem(position);  //当滑动时，事件响应，调用适配器中的这个方法。
        AttentionFragment.this.position = position;
        if(position == mList.size()-1) {
            page++;
            getAttentionBuyer();
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {//抽象方法，必须实现
        // TODO Auto-generated method stub

    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.iv_arrow_left://左箭头
               if(position>0){
                   gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
               }
               break;
           case R.id.iv_arrow_right://右箭头
               if(position<mList.size()-1){
                   gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
               }
               break;
           default:
               break;
       }
    }
}
