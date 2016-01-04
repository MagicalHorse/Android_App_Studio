package com.shenma.yueba.baijia.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.activity.BaiJiaOrderDetailsActivity;
import com.shenma.yueba.baijia.activity.MainActivityForBaiJia;
import com.shenma.yueba.baijia.modle.PayResponseFormBean;

/**
 * @author gyj
 * @version 创建时间：2015-6-24 上午11:44:52
 *          程序的简单说明
 */

public class OrderPaySearchDialog extends AlertDialog implements DialogInterface.OnKeyListener {
    View ll;
    Context context;
    Button orderpay_dialog_layout_queryorder_button;//查询订单
    ProgressBar orderpay_dialog_layout_progressbar;
    TextView orderpay_dialog_layout_textview;
    TextView orderpay_dialog_layout_sucess_textview;
    OrderPayOnClick_Listener orderPayOnClick_Listener;
    Button orderpay_dialog_layout_sucess_button;
    LinearLayout loading_linearlayout;

    PayResponseFormBean payResponseFormBean;
    LinearLayout orderpay_dialog_layout_sucess_linearlayout;
    boolean cancelsttaus = true;

    public OrderPaySearchDialog(PayResponseFormBean payResponseFormBean, Context context, OrderPayOnClick_Listener orderPayOnClick_Listener, boolean cancelsttaus) {
        //super(context, R.style.MyDialog);
        super(context);
        this.context = context;
        this.payResponseFormBean = payResponseFormBean;
        this.orderPayOnClick_Listener = orderPayOnClick_Listener;
        this.cancelsttaus = cancelsttaus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ll = RelativeLayout.inflate(context, R.layout.orderpaysearch_layout, null);
        setContentView(ll);
        initView();
        setOnKeyListener(this);
    }

    public void showDialog() {
        show();
        /*DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = width - (width / 3);
        params.height = width - (width / 3);
        this.getWindow().setAttributes(params);*/
    }

    void initView() {
        loading_linearlayout=(LinearLayout)ll.findViewById(R.id.loading_linearlayout);
        orderpay_dialog_layout_queryorder_button = (Button) ll.findViewById(R.id.orderpay_dialog_layout_queryorder_button);
        orderpay_dialog_layout_queryorder_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BaiJiaOrderDetailsActivity.class);
                intent.putExtra("ORDER_ID", payResponseFormBean.getOrderNo());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
                dismiss();
                if (orderPayOnClick_Listener != null) {
                    //查询订单
                    orderPayOnClick_Listener.on_Click();
                }
            }
        });
        orderpay_dialog_layout_progressbar = (ProgressBar) ll.findViewById(R.id.orderpay_dialog_layout_progressbar);
        orderpay_dialog_layout_textview = (TextView) ll.findViewById(R.id.orderpay_dialog_layout_textview);
        orderpay_dialog_layout_sucess_textview = (TextView) ll.findViewById(R.id.orderpay_dialog_layout_sucess_textview);
        orderpay_dialog_layout_sucess_button = (Button) ll.findViewById(R.id.orderpay_dialog_layout_sucess_button);
        orderpay_dialog_layout_sucess_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (orderPayOnClick_Listener != null) {
                    //返回首页
                    orderPayOnClick_Listener.on_Click();
                    Intent intent = new Intent(context, MainActivityForBaiJia.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        });
        orderpay_dialog_layout_sucess_linearlayout = (LinearLayout) ll.findViewById(R.id.orderpay_dialog_layout_sucess_linearlayout);
    }

    /***
     * 显示查询中
     **/
    public void showLoading() {
        showDialog();
        loading_linearlayout.setVisibility(View.VISIBLE);
        orderpay_dialog_layout_sucess_linearlayout.setVisibility(View.INVISIBLE);
    }

    /***
     * 显示支付成功
     **/
    public void showSucess() {
        showDialog();
        orderpay_dialog_layout_sucess_textview.setText("支付成功");
        orderpay_dialog_layout_sucess_linearlayout.setVisibility(View.VISIBLE);
        loading_linearlayout.setVisibility(View.INVISIBLE);
    }

    /***
     * 显示支付失败
     **/
    public void showFails() {
        showDialog();
        orderpay_dialog_layout_sucess_textview.setText("查询支付结果失败");
        orderpay_dialog_layout_sucess_linearlayout.setVisibility(View.VISIBLE);
        loading_linearlayout.setVisibility(View.INVISIBLE);
    }


    public interface OrderPayOnClick_Listener {
        void on_Click();
    }


    public void dismiss() {

    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                /*if (cancelsttaus) {
                    dismiss();
                    if (orderPayOnClick_Listener != null) {
                        orderPayOnClick_Listener.on_Click();
                    }
                }*/
                return false;
            }
        }
        return true;
    }
}
