package com.shenma.yueba.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.utils.ToolsUtil;

/**
 * activity的依赖注入基类
 * @author a
 */
public class BaseActivity extends Activity {

    protected Context mContext;// 上下文
    TextView title_layout_left_textview;//标题左侧返回
    TextView  title_layout_titlename_textview;//标题
    TextView  title_layout_right_textview;//标题右侧

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        MyApplication.getInstance().addActivity(this);
        initTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }


    public void initTitle()
    {
        title_layout_left_textview=(TextView)findViewById(R.id.title_layout_left_textview);
        title_layout_titlename_textview=(TextView)findViewById(R.id.title_layout_titlename_textview);
        title_layout_right_textview=(TextView)findViewById(R.id.title_layout_right_textview);
    }


    /*******
     * 设置 标题名称
     * ***/
    public void setTitleName(String str)
    {
        if(title_layout_titlename_textview!=null)
        {
            title_layout_titlename_textview.setText(ToolsUtil.nullToString(str));
        }
    }

    /*******
     * 设置 标题左侧
     * ***/
    public void setTitleLeft(String str)
    {
        if(title_layout_left_textview!=null)
        {
            title_layout_left_textview.setVisibility(View.VISIBLE);
            title_layout_left_textview.setText(ToolsUtil.nullToString(str));
        }
    }

    public void setLeftTextView(View.OnClickListener onClickListener)
    {
        if(title_layout_left_textview!=null && onClickListener!=null)
        {
            title_layout_left_textview.setVisibility(View.VISIBLE);
            title_layout_left_textview.setOnClickListener(onClickListener);
        }
    }



    /****************
     *  页面 跳转 不传递任何值
     *  @param  mclass Class Activity 类名
     *  @param  isCloseSelf boolean 是否关闭当前页面 true 是 false 否
     * *****************/
    public void skip(Class<?> mclass,boolean isCloseSelf)
    {
        Intent intent=new Intent(this,mclass);
        startActivity(intent);
        isCloseActivity(isCloseSelf);
    }



    /****************
     *  页面 跳转 不传递任何值
     *  @param  mclass Class Activity 类名
     *  @param  requestCode int 返回码
     * *****************/
    public void skipForResult(Class<?> mclass,int requestCode)
    {
        Intent intent=new Intent(this,mclass);
        this.startActivityForResult(intent, requestCode);
    }


    /****************
     *  页面 跳转
     *  @param  intent Intent
     *  @param  isCloseSelf boolean 是否关闭当前页面 true 是 false 否
     * *****************/
    public void skip(Intent intent,boolean isCloseSelf)
    {
        if(intent!=null)
        {
            this.startActivity(intent);
            isCloseActivity(isCloseSelf);
        }

    }


    /****************
     *  页面 跳转
     *  @param  intent Intent
     *  @param  requestCode int 返回码
     * *****************/
    public void skipForResult(Intent intent,int requestCode)
    {
        if(intent!=null)
        {
            this.startActivity(intent);
            this.startActivityForResult(intent,requestCode);
        }

    }



    /****************
     *  是否关闭当前页面
     *  @param  isCloseSelf boolean 是否关闭当前页面 true 是 false 否
     * *****************/
    public void isCloseActivity(boolean isCloseSelf)
    {
        if(isCloseSelf)
        {
            this.finish();
        }
    }
}
