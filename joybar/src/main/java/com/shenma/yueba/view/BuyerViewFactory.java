package com.shenma.yueba.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.util.ToolsUtil;

/**
 * Created by a on 2015/11/17.
 */
public class BuyerViewFactory {


    private LinearLayout imagesLayout;
    private  int screenWith;
    private BuyerViewFactory instance;

    private  ImageView iv_one, iv_two, iv_three, iv_four;
    private  View view;



    public LinearLayout getImageLayout(Context ctx, int count) {
        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        screenWith = ToolsUtil.getDisplayWidth(ctx);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWith, screenWith);
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(screenWith / 2, screenWith / 2);
        switch (count) {
            case 1://一张图片
                view = View.inflate(ctx, R.layout.one_pic_layout, null);
                iv_one = (ImageView) view.findViewById(R.id.iv_one);
                iv_one.setLayoutParams(params);
                layout.addView(view);
                break;
            case 2://两张
                view = View.inflate(ctx, R.layout.two_pic_layout, null);
                iv_one = (ImageView) view.findViewById(R.id.iv_one);
                iv_two = (ImageView) view.findViewById(R.id.iv_two);
                iv_one.setLayoutParams(params2);
                iv_two.setLayoutParams(params2);
                layout.addView(view);
                break;
            case 3://三张
                view = View.inflate(ctx, R.layout.three_pic_layout, null);
                ImageView iv_one = (ImageView) view.findViewById(R.id.iv_one);
                ImageView iv_two = (ImageView) view.findViewById(R.id.iv_two);
                ImageView iv_three = (ImageView) view.findViewById(R.id.iv_three);
                iv_one.setLayoutParams(params2);
                iv_two.setLayoutParams(params2);
                iv_three.setLayoutParams(params2);
                layout.addView(view);
                break;
            case 4://四张
                view = View.inflate(ctx, R.layout.four_pic_layout, null);
                iv_one = (ImageView) view.findViewById(R.id.iv_one);
                iv_two = (ImageView) view.findViewById(R.id.iv_two);
                iv_three = (ImageView) view.findViewById(R.id.iv_three);
                iv_four = (ImageView) view.findViewById(R.id.iv_four);
                iv_one.setLayoutParams(params2);
                iv_one.setLayoutParams(params2);
                iv_one.setLayoutParams(params2);
                iv_four.setLayoutParams(params2);
                layout.addView(view);
                break;


            default:
                break;

        }
        return layout;
    }



    public ImageView getImageOne(){
        return iv_one;
    }
    public ImageView getImageTwo(){
        return iv_two;
    }
    public ImageView getImageThree(){
        return iv_three;
    }
    public ImageView getImageFour(){
        return iv_four;
    }
}
