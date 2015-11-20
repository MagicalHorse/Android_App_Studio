package com.shenma.yueba.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.activity.BaijiaBrandListActivity;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagsInfo;
import com.shenma.yueba.util.DialogUtilInter;
import com.shenma.yueba.util.DialogUtils;
import com.shenma.yueba.util.ToolsUtil;

/**
 * 标签自定义控件类
 * 
 * @Description
 * @author zhou wan
 * @date 2015-1-30
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc.
 *             All rights reserved.
 */
public class TagImageView extends RelativeLayout {
	
	
	
	
	private ArrayList<String> tagNameList =new ArrayList<String>();//姓名的集合
	private ArrayList<Integer> tagIdList = new ArrayList<Integer>();//id的集合
	private ArrayList<String> tagTypeList = new ArrayList<String>();//标签类型的集合
	private int i;
	/** 上下文对象 */
	private Context context;
	/** 存放子空间 tag view 的集合 */
	private List<View> tagViewList;
	private List<Map<String, Integer>> positionList = new ArrayList<Map<String,Integer>>();
	private int xx;
	private int yy;
	public TagImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public TagImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public TagImageView(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 添加标签
	 * 
	 * 
	 * @version 1.0
	 * @createTime 2015-1-30,上午11:50:02
	 * @updateTime 2015-1-30,上午11:50:02
	 * @createAuthor zhou wan
	 * @updateAuthor
	 * @updateInfo
	 * @param content
	 * @param tagX int 轴的位置
	 * @param tagY int 轴的位置
	 */
	public void addTextTag(String content, int tagX, int tagY,int tagId,String tagType) {
		if (tagViewList == null)
			tagViewList = new ArrayList<View>();
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.tag, null);
			TextView text = (TextView) view.findViewById(R.id.tag_text);
			RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.tag_layout);
			text.setText(content);
			layout.setTag(i++);
			setTagViewOnTouchListener(layout);
			this.addView(layout);
			setTagViewPosition(layout, tagX, tagY);
			tagViewList.add(layout);
			Map<String, Integer> tagsPositonMap = new HashMap<String, Integer>();
			tagsPositonMap.put("x", tagX);
			tagsPositonMap.put("y", tagY);
			positionList.add(tagsPositonMap);
			tagNameList.add(content);
			tagTypeList.add(tagType);
			tagIdList.add(tagId);
			layout.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					final int tag = (Integer) v.getTag();
					int count = TagImageView.this.getChildCount();
					DialogUtils utils = new DialogUtils();
					utils.alertDialog(getContext(), "提示", "要删除该标签吗？", new DialogUtilInter() {
						@Override
						public void dialogCallBack(int... which) {
							
							clearTagView(tag);
						}
					}, true, "确定", "取消", true, true);
					return false;
				}
			});
	}

	
	
	
	
	/**
	 * 添加标签
	 * 
	 * 
	 * @version 1.0
	 * @createTime 2015-1-30,上午11:50:02
	 * @updateTime 2015-1-30,上午11:50:02
	 * @createAuthor zhou wan
	 * @updateAuthor
	 * @updateInfo
	 * @param content
	 * @param tagX int 轴的位置
	 * @param tagY int 轴的位置
	 * @param obj  Object 数据对象
	 */
	public void addTextTagCanNotMove(String content, int tagX, int tagY,Object obj) {
		if (tagViewList == null)
		{
			tagViewList = new ArrayList<View>();
		}
		
//		boolean isAdd = true;
//		if(tagNameList.contains(content)){
//			for (int i = 0; i < positionList.size(); i++) {
//				if(tagX == positionList.get(i).get("x") && tagY == positionList.get(i).get("x")){
//					isAdd = false;
//					break;
//				}
//			}
//		}
//		if(isAdd){
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.tag, null);
			TextView text = (TextView) view.findViewById(R.id.tag_text);
			text.setTag(obj);
			text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(v.getTag()!=null && v.getTag() instanceof ProductsDetailsTagsInfo)
					{
						ProductsDetailsTagsInfo productsDetailsTagsInfo=(ProductsDetailsTagsInfo)v.getTag();
						if(productsDetailsTagsInfo.getSourceId()>0)
						{
							Intent intent=new Intent(context,BaijiaBrandListActivity.class);
							intent.putExtra("BrandList_type", BaijiaBrandListActivity.BrandList_type.Type_Brand);
							intent.putExtra("BrandId", productsDetailsTagsInfo.getSourceId());
							intent.putExtra("BrandName", productsDetailsTagsInfo.getName());
							context.startActivity(intent);
						}else
						{
							Intent intent=new Intent(context,BaijiaBrandListActivity.class);
							intent.putExtra("BrandList_type", BaijiaBrandListActivity.BrandList_type.Type_Text);
							intent.putExtra("TextName",productsDetailsTagsInfo.getName());
							context.startActivity(intent);
						}
						
					}
				}
			});
//			android.view.ViewGroup.LayoutParams params = text.getLayoutParams();
//			params.height = ToolsUtil.px2dip(getContext(), 25);
//			text.setLayoutParams(params);
			RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.tag_layout);
			text.setText(content);
			layout.setTag(i++);
//			setTagViewOnTouchListener(layout);
			this.addView(layout);
			setTagViewPositionNoMove(layout, tagX, tagY);
			tagViewList.add(layout);
			Map<String, Integer> tagsPositonMap = new HashMap<String, Integer>();
			tagsPositonMap.put("x", tagX);
			tagsPositonMap.put("y", tagY);
			positionList.add(tagsPositonMap);
			tagNameList.add(content);
//		}
	
	}
	
	
	/**
	 * 给标签添加OnTouch监听
	 * 
	 * @version 1.0
	 * @createTime 2015-2-2,上午11:06:11
	 * @updateTime 2015-2-2,上午11:06:11
	 * @createAuthor zhou wan
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param layoutView
	 *            标签View
	 */
	public void setTagViewOnTouchListener(final RelativeLayout layoutView) {
		layoutView.setOnTouchListener(new OnTouchListener() {
			int startx = 0;
			int starty = 0;

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.tag_layout) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startx = (int) event.getRawX();
						starty = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						// 获取手指移动的距离
						int x = (int) event.getRawX();
						int y = (int) event.getRawY();
						int dx = x - startx;
						int dy = y - starty;
						// 更改imageView在窗体的位置
						setTagViewPosition(v, dx, dy);
						// 获取移动后的位置
						startx = (int) event.getRawX();
						starty = (int) event.getRawY();
						
						break;
					case MotionEvent.ACTION_UP:
						Log.i("tag", "actionUp-----"+v.getX()+"-----"+ v.getY());
						for (int i = 0; i < tagViewList.size(); i++) {
								if(i == (Integer)layoutView.getTag()){
									positionList.get(i).put("x", (int)v.getX());
									positionList.get(i).put("y", (int)v.getY());
								}
							}
						break;
					}
				}
				return false;
			}
		});

	}

	
	public void setIToZero(){
		i = 0;
	}
	
	/**
	 * 
	 * 设置标签的位置
	 * 
	 * @version 1.0
	 * @createTime 2015-1-30,上午11:49:33
	 * @updateTime 2015-1-30,上午11:49:33
	 * @createAuthor zhou wan
	 * @updateAuthor
	 * @updateInfo
	 * @param tagView View
	 * @param dx x方向移动距离
	 * @param dy y方向移动的距离
	 */
	private void setTagViewPosition(View tagView, int dx, int dy) {
		
		
		xx = dx;
		yy = dy;
		
		
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();

		if(parentWidth == 0){
			parentWidth = ToolsUtil.getDisplayWidth(context);
		}
		if(parentHeight == 0){
			parentHeight = ToolsUtil.getDisplayWidth(context);
		}
		
		int left = tagView.getLeft() + dx;
		int top = tagView.getTop() + dy;

		if (left < 0) {
			left = 0;
		} else if ((left + tagView.getWidth()) >= parentWidth) {
			left = parentWidth - tagView.getWidth();
		}
		if (top < 0) {
			top = 0;
		} else if ((top + tagView.getHeight()) >= parentHeight) {
			top = parentHeight - tagView.getHeight();
		}
		int right = left + tagView.getWidth();
		int bottom = top + tagView.getHeight();
		tagView.layout(left, top, right, bottom);
		
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tagView.getLayoutParams();
		params.leftMargin = left;
		params.topMargin = top;
		tagView.setLayoutParams(params);
	}

	
	
	/**
	 * 
	 * 设置标签的位置
	 * 
	 * @version 1.0
	 * @createTime 2015-1-30,上午11:49:33
	 * @updateTime 2015-1-30,上午11:49:33
	 * @createAuthor zhou wan
	 * @updateAuthor
	 * @updateInfo
	 * @param tagView view
	 * @param dx
	 *            x方向移动距离
	 * @param dy
	 *            y方向移动的距离
	 */
	private void setTagViewPositionNoMove(View tagView, int dx, int dy) {
		
		
		xx = dx;
		yy = dy;
		
		
		int parentWidth = ToolsUtil.getDisplayWidth(context);
		int parentHeight = parentWidth;

		int left = tagView.getLeft() + dx;
		int top = tagView.getTop() + dy;

		if (left < 0) {
			left = 0;
		} else if ((left + tagView.getWidth()) >= parentWidth) {
			left = parentWidth - tagView.getWidth();
		}
		if (top < 0) {
			top = 0;
		} else if ((top + tagView.getHeight()) >= parentHeight) {
			top = parentHeight - tagView.getHeight();
		}
		int right = left + tagView.getWidth();
		int bottom = top + tagView.getHeight();
		tagView.layout(left, top, right, bottom);
		
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tagView.getLayoutParams();
		params.leftMargin = left;
		params.topMargin = top;
		tagView.setLayoutParams(params);
	}

	
	public void clearViewList(){
		if(tagViewList!=null){
			tagViewList.clear();
		}
	}
	
	/**
	 * @return the tagViewList
	 */
	public List<View> getTagViewList() {
		return tagViewList;
	}

	/**
	 * @param tagViewList
	 *            the tagViewList to set
	 */
	public void setTagViewList(List<View> tagViewList) {
		this.tagViewList = tagViewList;
	}

	
	/**
	 * @return the positionList
	 */
	public List<Map<String, Integer>> getPositionList() {
		return positionList;
	}

	
	

	
	public ArrayList<String> getTagNameList() {
		return tagNameList;
	}

	public void setTagNameList(ArrayList<String> tagNameList) {
		this.tagNameList = tagNameList;
	}
	
	



	public ArrayList<Integer> getTagIdList() {
		return tagIdList;
	}

	public void setTagIdList(ArrayList<Integer> tagIdList) {
		this.tagIdList = tagIdList;
	}

	public ArrayList<String> getTagTypeList() {
		return tagTypeList;
	}

	public void setTagTypeList(ArrayList<String> tagTypeList) {
		this.tagTypeList = tagTypeList;
	}

	/**
	 * 清除所有标签
	 * 
	 * @version 1.0
	 * @createTime 2015-2-2,下午3:10:15
	 * @updateTime 2015-2-2,下午3:10:15
	 * @createAuthor zhou wan
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	public void clearTagView(int tag) {
		if (tagViewList != null && tagViewList.size() > 0) {
			for (int i = 0; i <tagViewList.size(); i++) {
				if((Integer)tagViewList.get(i).getTag() == tag){
					tagViewList.remove(i);
					tagNameList.remove(i);
					tagIdList.remove(i);
					tagTypeList.remove(i);
					positionList.remove(i);
					removeViews(i+1, 1);
					
				}
			}
		}
	}
	
	public void clearTagView() {
		if (tagViewList != null && tagViewList.size() > 0) {
			removeViews(1, tagViewList.size());
			tagViewList.clear();
		}
	}
	
}
