package com.shenma.yueba.util;

/************************************************************************************** 
* [Project] 
*       MyProgressDialog 
* [Package] 
*       com.lxd.widgets 
* [FileName] 
*       CustomProgressDialog.java 
* [Copyright] 
*       Copyright 2012 LXD All Rights Reserved. 
* [History] 
*       Version          Date              Author                        Record 
*-------------------------------------------------------------------------------------- 
*       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create 
**************************************************************************************/
      
  
  
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shenma.yueba.R;
public class CustomProgressDialog extends Dialog { 
    private Context context = null; 
    private static CustomProgressDialog customProgressDialog = null; 
      
    public CustomProgressDialog(Context context){ 
        super(context); 
        this.context = context; 
    } 
      
    public CustomProgressDialog(Context context, int theme) { 
        super(context, theme); 
    } 
      
    public static CustomProgressDialog createDialog(Context context){ 
    	try {
    		   customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog); 
		} catch (Exception e) {
			e.printStackTrace();
		}
     
        customProgressDialog.setContentView(R.layout.customer_progress_dialog); 
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER; 
        return customProgressDialog; 
    } 
   
    public CustomProgressDialog setTitile(String strTitle){ 
        return customProgressDialog; 
    } 
      
    public CustomProgressDialog setMessage(String strMessage){ 
        TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg); 
          FontManager.changeFonts(context,tvMsg);
        if (tvMsg != null){
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(strMessage); 
        } 
          
        return customProgressDialog; 
    } 
}