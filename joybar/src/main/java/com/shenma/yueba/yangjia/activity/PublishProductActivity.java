package com.shenma.yueba.yangjia.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.baijia.modle.RequestUploadProductDataBean;
import com.shenma.yueba.baijia.modle.newmodel.ProductDetailBackBean;
import com.shenma.yueba.camera2.ActivityCapture;
import com.shenma.yueba.util.CustomProgressDialog;
import com.shenma.yueba.util.DialogUtilInter;
import com.shenma.yueba.util.DialogUtils;
import com.shenma.yueba.util.FileUtils;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ProductImagesBean;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.SizeBean;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.modle.StateBean;
import com.shenma.yueba.yangjia.modle.TagCacheBean;
import com.shenma.yueba.yangjia.modle.TagListBean;
import com.shenma.yueba.yangjia.modle.TagsBean;
import com.umeng.analytics.MobclickAgent;

/**
 * 发布商品
 * 
 * @author a
 * 
 */
public class PublishProductActivity extends BaseActivityWithTopView implements
		OnClickListener {
	private int pictureCount = 3;// 图片个数
	private Bitmap bitmap0, bitmap1, bitmap2;
	public String pic1, pic2, pic3, pic;
	private LinearLayout ll_guige_container;
	private TextView tv_product_number;
	private EditText et_product_number;
	private TextView tv_price_title;
	private EditText et_price;
	private TextView tv_yuan;
	private EditText et_introduce;
	private TextView tv_retain;
	private EditText et_guige;
	private EditText et_kucun;
	private TextView tv_add_guige;
	private TextView tv_publish;
	private LinearLayout ll_pictures_container;
	private List<TagsBean> tagList;
	private List<View> littleImageViewList = new LinkedList<View>();
	private List<StateBean> dataList = new ArrayList<StateBean>();
	private List<View> GuigeViewList = new LinkedList<View>();
	private int index;
	private List<SizeBean> Sizes = new LinkedList<SizeBean>();
	private CustomProgressDialog progressDialog;
	private int upPicProgress = 0;
	private int maxSize = 100;
	private boolean onePicLoadFinished, twoPicLoadFinished, ThreePicFinished;
	private String productId;
	private String from;
	RequestUploadProductDataBean detailBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);// 加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.publish_product);
		super.onCreate(savedInstanceState);
		initView();
		getIntentData();
	}

	private void getIntentData() {
		from = getIntent().getStringExtra("from");
		TagListBean tagListBean = (TagListBean) getIntent()
				.getSerializableExtra("tagListBean");
		productId = getIntent().getStringExtra("id");
		if (TextUtils.isEmpty(productId)//发布商品
				&& TextUtils.isEmpty(MyApplication.getInstance()
						.getPublishUtil().getBean().getId())) {
			tv_publish.setText("发布");
			if (tagListBean != null) {
				tagList = tagListBean.getTagList();
				if (tagList != null && tagList.size() > 0) {
					int index = Integer.valueOf(MyApplication.getInstance()
							.getPublishUtil().getIndex());
					List<ProductImagesBean> images = MyApplication.getInstance()
							.getPublishUtil().getBean().getImages();
					int currentLength = images.size();
					if (currentLength < index + 1) {
						for (int i = 0; i < index - currentLength + 1; i++) {
							ProductImagesBean productImagesBean = new ProductImagesBean();
							MyApplication.getInstance().getPublishUtil().getBean()
									.getImages().add(productImagesBean);
						}
					}
					MyApplication.getInstance().getPublishUtil().getBean()
							.getImages().get(index).setTags(tagList);
				}
			}
			setImageView();
		} else {
			if (productId != null) {
				MyApplication.getInstance().getPublishUtil().getBean()
						.setId(productId);
			} else {
				productId = MyApplication.getInstance().getPublishUtil()
						.getBean().getId();
			}
			tv_publish.setText("修改");
			setTitle("修改商品");
			setTopRightTextView("");// 取消右上角按钮
			setLeftTextView(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyApplication.getInstance().finishActivity(
							EditPicActivity.class);
					MyApplication.getInstance().finishActivity(
							ActivityCapture.class);
					MyApplication.getInstance().finishActivity(
							PublishProductActivity.class);
				}
			});
			getProductDetail();
		}
	}

	private void initView() {
		setTitle("发布商品");
		// setLeftTextView(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// PublishProductActivity.this.finish();
		// }
		// });
		setTopRightTextView("取消发布", new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyApplication.getInstance().finishActivity(
						EditPicActivity.class);
				MyApplication.getInstance().finishActivity(
						ActivityCapture.class);
				MyApplication.getInstance().finishActivity(
						PublishProductActivity.class);
			}
		});
		ll_pictures_container = getView(R.id.ll_pictures_container);
		ll_guige_container = getView(R.id.ll_guige_container);
		tv_product_number = getView(R.id.tv_product_number);
		et_product_number = getView(R.id.et_product_number);
		tv_price_title = getView(R.id.tv_price_title);
		et_price = getView(R.id.et_price);
		tv_yuan = getView(R.id.tv_yuan);
		et_introduce = getView(R.id.et_introduce);
		tv_retain = getView(R.id.tv_retain);
		tv_retain.setText(maxSize + "字");
		et_introduce.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_retain.setText(maxSize - s.length() + "字");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		et_guige = getView(R.id.et_guige);
		et_kucun = getView(R.id.et_kucun);
		tv_add_guige = getView(R.id.tv_add_guige);
		tv_publish = getView(R.id.tv_publish);
		tv_add_guige.setOnClickListener(this);
		tv_publish.setOnClickListener(this);
		FontManager.changeFonts(mContext, tv_top_title, tv_top_right,
				tv_product_number, et_product_number, tv_price_title, et_price,
				tv_yuan, et_introduce, tv_retain, et_guige, et_kucun,
				tv_add_guige, tv_publish);
		progressDialog = new CustomProgressDialog(mContext)
				.createDialog(mContext);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

	}

	private void setImageView() {
		for (int i = 0; i < pictureCount; i++) {
			StateBean bean = new StateBean();
			View view = View.inflate(mContext, R.layout.tag_imageview, null);
			final ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			final ImageView iv_delete = (ImageView) view
					.findViewById(R.id.iv_delete);
			iv_delete.setTag(i);
			iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int deletePosition = (Integer) iv_delete.getTag();// 获取删除属于哪个view的标记
					// 以下是清空缓存的标签
					ArrayList<List<TagCacheBean>> tagList = MyApplication
							.getInstance().getPublishUtil().getTagCacheList();
					if (tagList.size() > deletePosition) {
						MyApplication
								.getInstance()
								.getPublishUtil()
								.getTagCacheList()
								.set(deletePosition,
										new ArrayList<TagCacheBean>());
					}
					// 以下是清除缓存的图片
					List<ProductImagesBean> imagesList = MyApplication
							.getInstance().getPublishUtil().getBean()
							.getImages();
					if (imagesList.size() > deletePosition) {
						MyApplication.getInstance().getPublishUtil().getBean()
								.getImages()
								.set(deletePosition, new ProductImagesBean());
					}
					iv_pic.setImageBitmap(null);
					iv_pic.setBackgroundResource(R.drawable.add_pic_default);
					iv_delete.setVisibility(View.GONE);
					StateBean bean = (StateBean) iv_pic.getTag();
					bean.setSetPic(false);
					iv_pic.setTag(bean);
					File file = null;

					if (deletePosition == 0) {
						if (new File(FileUtils.getRootPath() + "/tagPic/"
								+ "tagPic" + SharedUtil.getUserId(mContext)
								+ "0.png").exists()) {
							file = new File(FileUtils.getRootPath()
									+ "/tagPic/" + "tagPic"
									+ SharedUtil.getUserId(mContext) + "0.png");
							pic1 = "";
						}
					}
					if (deletePosition == 1) {
						if (new File(FileUtils.getRootPath() + "/tagPic/"
								+ "tagPic" + SharedUtil.getUserId(mContext)
								+ "1.png").exists()) {
							file = new File(FileUtils.getRootPath()
									+ "/tagPic/" + "tagPic"
									+ SharedUtil.getUserId(mContext) + "1.png");
							pic2 = "";
						}
					}
					if (deletePosition == 2) {
						if (new File(FileUtils.getRootPath() + "/tagPic/"
								+ "tagPic" + SharedUtil.getUserId(mContext)
								+ "2.png").exists()) {
							file = new File(FileUtils.getRootPath()
									+ "/tagPic/" + "tagPic"
									+ SharedUtil.getUserId(mContext) + "2.png");
							pic3 = "";
						}
					}
					if (file != null && file.exists()) {
						file.delete();
					}
				}
			});
			iv_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToolsUtil.hideSoftKeyboard(mContext, et_product_number);// 隐藏软键盘
					StateBean bean = (StateBean) v.getTag();
					if (dataList.contains(bean)) {
						index = dataList.indexOf(bean);
						if (bean.isSetPic()) {
							Intent intent = new Intent(
									PublishProductActivity.this,
									EditPicActivity.class);
							MyApplication.getInstance().getPublishUtil()
									.setFrom("publish");
							if (!("" + index).equals(MyApplication
									.getInstance().getPublishUtil().getIndex())) {
								MyApplication.getInstance().getPublishUtil()
										.setOtherPic(true);
							} else {
								MyApplication.getInstance().getPublishUtil()
										.setOtherPic(false);
							}
							MyApplication.getInstance().getPublishUtil()
									.setIndex(index + "");
							startActivity(intent);
							PublishProductActivity.this.finish();

						} else {
							Intent intent = new Intent(
									PublishProductActivity.this,
									ActivityCapture.class);
							MyApplication.getInstance().getPublishUtil()
									.setFrom("publish");
							MyApplication.getInstance().getPublishUtil()
									.setIndex(index + "");
							MyApplication.getInstance().finishActivity(
									EditPicActivity.class);
							startActivity(intent);
							PublishProductActivity.this.finish();
						}

					}
				}
			});

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.height = ToolsUtil.getDisplayWidth(mContext) / 3
					- ToolsUtil.dip2px(mContext, 15);
			params.width = ToolsUtil.getDisplayWidth(mContext) / 3
					- ToolsUtil.dip2px(mContext, 15);
			iv_pic.setLayoutParams(params);
			bean.setPosition(i);
			File files = new File(FileUtils.getRootPath() + "/tagPic/"
					+ "tagPic" + SharedUtil.getUserId(mContext) + i + ".png");
			if (files.exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				switch (i) {
				case 0:
					if (bitmap0 != null) {
						bitmap0 = null;
					}
					bitmap0 = BitmapFactory.decodeFile(
							FileUtils.getRootPath() + "/tagPic/" + "tagPic"
									+ SharedUtil.getUserId(mContext) + i
									+ ".png", options);
					iv_pic.setImageBitmap(bitmap0);
					iv_delete.setVisibility(View.VISIBLE);
					bean.setSetPic(true);
					break;
				case 1:
					if (bitmap1 != null) {
						bitmap1 = null;
					}
					bitmap1 = BitmapFactory.decodeFile(
							FileUtils.getRootPath() + "/tagPic/" + "tagPic"
									+ SharedUtil.getUserId(mContext) + i
									+ ".png", options);
					iv_pic.setImageBitmap(bitmap1);
					iv_delete.setVisibility(View.VISIBLE);
					bean.setSetPic(true);
					break;
				case 2:
					if (bitmap2 != null) {
						bitmap2 = null;
					}
					bitmap2 = BitmapFactory.decodeFile(
							FileUtils.getRootPath() + "/tagPic/" + "tagPic"
									+ SharedUtil.getUserId(mContext) + i
									+ ".png", options);
					iv_pic.setImageBitmap(bitmap2);
					iv_delete.setVisibility(View.VISIBLE);
					bean.setSetPic(true);
					break;
				default:
					break;
				}

			} else {
				iv_pic.setBackgroundResource(R.drawable.add_pic_default);
			}
			if (tagList != null && tagList.size() > 0) {
				bean.setSetTag(true);
			}
			iv_pic.setTag(bean);
			dataList.add(bean);
			ll_pictures_container.addView(view);

		}

		for (int i = 0; i < ll_pictures_container.getChildCount(); i++) {
			littleImageViewList.add(ll_pictures_container.getChildAt(i));
		}
		// for (int i = 0; i < littleImageViewList.size(); i++) {
		// littleImageViewList.get(i).findViewById(R.id.iv_pic).setOnClickListener(new
		// OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if(((StateBean)littleImageViewList.get(i).findViewById(R.id.iv_pic).getTag()).getPosition()!=littleImageViewList.size()){
		//
		// }
		//
		// }
		// });
		//
		// }

	}



	private void getProductDetail(){
		HttpControl httpControl = new HttpControl();
		httpControl.getProductDetail(new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				ProductDetailBackBean bean = (ProductDetailBackBean)obj;
				detailBean =  bean.getData();
				if (detailBean != null) {
					List<ProductImagesBean> images = detailBean.getImages();
					MyApplication.getInstance().getPublishUtil()
							.setBean(detailBean);
				} else {
					detailBean = MyApplication.getInstance().getPublishUtil()
							.getBean();
				}
// 修改商品
				if (detailBean != null) {
					if ("editPic".equals(from)) {

					} else if ("productManager".equals(from)) {
						FileUtils.delAllFile(FileUtils.getRootPath() + "/tagPic/");
					}
					List<ProductImagesBean> imagesList = MyApplication.getInstance()
							.getPublishUtil().getBean().getImages();
					if (imagesList != null && imagesList.size() > 0) {
						String imageUrl = imagesList.get(0).getImageUrl();
						if (!TextUtils.isEmpty(imageUrl)) {// 缓存有网络图片地址
							saveBitmapToFile(0, imageUrl);
						} else {// 说明图片已经本地修改的时候删除完，所以没有了图片
							setImageView();
						}
					}
					MyApplication.getInstance().getPublishUtil().setBean(detailBean);
					MyApplication.getInstance().getPublishUtil().getBean()
							.setId(productId);

					MyApplication.getInstance().getPublishUtil().getTagCacheList()
							.clear();
					ArrayList<List<TagCacheBean>> tagCacheList = MyApplication
							.getInstance().getPublishUtil().getTagCacheList();
					for (int i = 0; i < detailBean.getImages().size(); i++) {
						if (detailBean.getImages().get(i).getTags().size() > 0) {
							List<TagsBean> tagsBean = detailBean.getImages().get(i)
									.getTags();
							for (int j = 0; j < tagsBean.size(); j++) {
								TagCacheBean tagcacheBean = new TagCacheBean();
								tagcacheBean.setName(tagsBean.get(j).getName());
								tagcacheBean.setId(Integer.valueOf(tagsBean.get(j)
										.getSourceId()));
								tagcacheBean.setType(tagsBean.get(j).getSourceType());
								tagcacheBean.setX((int) (ToolsUtil
										.getDisplayWidth(mContext) * Double
										.parseDouble(tagsBean.get(j).getPosX())));
								tagcacheBean.setY((int) (ToolsUtil
										.getDisplayWidth(mContext) * Double
										.parseDouble(tagsBean.get(j).getPosY())));
								tagCacheList.get(i).add(tagcacheBean);
							}
						}
					}
					MyApplication.getInstance().getPublishUtil()
							.setTagCacheList(tagCacheList);

					et_product_number.setText(ToolsUtil.nullToString(MyApplication
							.getInstance().getPublishUtil().getBean().getSku_Code()));
					et_price.setText(ToolsUtil.nullToString(MyApplication.getInstance()
							.getPublishUtil().getBean().getPrice()));
					et_introduce.setText(ToolsUtil.nullToString(MyApplication
							.getInstance().getPublishUtil().getBean().getDesc()));
					List<SizeBean> sizeList = MyApplication.getInstance()
							.getPublishUtil().getBean().getSizes();
					if (sizeList != null) {
						for (int i = 0; i < sizeList.size(); i++) {
							if (i == 0) {
								et_guige.setText(sizeList.get(i).getName());
								et_kucun.setText(sizeList.get(i).getInventory());
							} else {
								View view = View.inflate(mContext, R.layout.guige_item,
										null);
								EditText et_guige = (EditText) view
										.findViewById(R.id.et_guige);
								EditText et_kucun = (EditText) view
										.findViewById(R.id.et_kucun);
								FontManager.changeFonts(mContext, et_guige, et_kucun);
								et_guige.setText(sizeList.get(i).getName());
								et_kucun.setText(sizeList.get(i).getInventory());
								ll_guige_container.addView(view);
							}
							ll_guige_container.setVisibility(View.VISIBLE);

						}
					}
					return;
				}

			}

			@Override
			public void http_Fails(int error, String msg) {

			}
		},mContext,productId);
	}

	private void publishProduct() {
		HttpControl httpControl = new HttpControl();
		List<ProductImagesBean> imageList = MyApplication.getInstance()
				.getPublishUtil().getBean().getImages();// 获取所有缓存的图片
		List<ProductImagesBean> cacheImageList = new ArrayList<ProductImagesBean>();
		for (int i = 0; i < imageList.size(); i++) {
			if (!TextUtils.isEmpty(imageList.get(i).getImageUrl())) {
				cacheImageList.add(imageList.get(i));
			}
		}
		MyApplication.getInstance().getPublishUtil().getBean()
				.setImages(cacheImageList);

		String id = MyApplication.getInstance().getPublishUtil().getBean()
				.getId();
		if (TextUtils.isEmpty(id)) {
			httpControl.createBuyerProductInfo(MyApplication.getInstance()
					.getPublishUtil().getBean(), new HttpCallBackInterface() {
				@Override
				public void http_Success(Object obj) {
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
					// FileUtils.delAllFile(FileUtils.getRootPath() +
					// "/tagPic/");
					PublishProductActivity.this.finish();
					MyApplication.getInstance().getPublishUtil().setBean(null);
					MyApplication.getInstance().getPublishUtil().setIndex("0");
					MyApplication.getInstance().getPublishUtil().setFrom("");
					MyApplication.getInstance().getPublishUtil().setUri(null);
					MyApplication.getInstance().finishActivity(
							EditPicActivity.class);
					MyApplication.getInstance().finishActivity(
							PublishProductActivity.class);
				}

				@Override
				public void http_Fails(int error, String msg) {
					Toast.makeText(mContext, msg, 1000).show();
				}
			}, PublishProductActivity.this);
		} else {
			httpControl.updateBuyerProductInfo(MyApplication.getInstance()
					.getPublishUtil().getBean(), new HttpCallBackInterface() {
				@Override
				public void http_Success(Object obj) {
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
					// FileUtils.delAllFile(FileUtils.getRootPath() +
					// "/tagPic/");
					setResult(101);
					PublishProductActivity.this.finish();
					MyApplication.getInstance().getPublishUtil().setBean(null);
					MyApplication.getInstance().getPublishUtil().setIndex("0");
					MyApplication.getInstance().getPublishUtil().setFrom("");
					MyApplication.getInstance().getPublishUtil().setUri(null);
					MyApplication.getInstance().finishActivity(
							EditPicActivity.class);
				}

				@Override
				public void http_Fails(int error, String msg) {
					Toast.makeText(mContext, msg, 1000).show();
				}
			}, PublishProductActivity.this);

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add_guige:// 添加规格
			for (int i = 0; i < ll_guige_container.getChildCount(); i++) {
				ll_guige_container.getChildAt(i).setTag(i);
			}
			View view = View.inflate(mContext, R.layout.guige_item, null);
			EditText et_guige = (EditText) view.findViewById(R.id.et_guige);
			EditText et_kucun = (EditText) view.findViewById(R.id.et_kucun);
			view.setTag(ll_guige_container.getChildCount());
			final ImageView iv_delete = (ImageView) view
					.findViewById(R.id.iv_delete);
			iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = (Integer) ((View) iv_delete.getParent()).getTag();
					for (int i = 0; i < ll_guige_container.getChildCount(); i++) {
						if (tag == (Integer) ll_guige_container.getChildAt(i)
								.getTag()) {
							ll_guige_container.removeViewAt(i);

						}
					}

				}
			});
			FontManager.changeFonts(mContext, et_guige, et_kucun);
			ll_guige_container.addView(view);
			break;
		case R.id.tv_publish:// 发布商品
			// MyApplication.getInstance().getPublishUtil().getBean().setImages(null);
			upPicProgress = 0;
			uploadImage();
			break;
		default:
			break;
		}

	}

	private void uploadImage() {
		if (new File(FileUtils.getRootPath() + "/tagPic/" + "tagPic"
				+ SharedUtil.getUserId(mContext) + "0.png").exists()) {
			pic1 = FileUtils.getRootPath() + "/tagPic/" + "tagPic"
					+ SharedUtil.getUserId(mContext) + "0.png";
		}
		if (new File(FileUtils.getRootPath() + "/tagPic/" + "tagPic"
				+ SharedUtil.getUserId(mContext) + "1.png").exists()) {
			pic2 = FileUtils.getRootPath() + "/tagPic/" + "tagPic"
					+ SharedUtil.getUserId(mContext) + "1.png";
		}
		if (new File(FileUtils.getRootPath() + "/tagPic/" + "tagPic"
				+ SharedUtil.getUserId(mContext) + "2.png").exists()) {
			pic3 = FileUtils.getRootPath() + "/tagPic/" + "tagPic"
					+ SharedUtil.getUserId(mContext) + "2.png";
		}
		if (TextUtils.isEmpty(pic1) && TextUtils.isEmpty(pic2)
				&& TextUtils.isEmpty(pic3)) {
			Toast.makeText(mContext, "商品图片不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(et_product_number.getText().toString().trim())) {
			Toast.makeText(mContext, "货号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(et_price.getText().toString().trim())) {
			Toast.makeText(mContext, "价格不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(et_introduce.getText().toString().trim())) {
			Toast.makeText(mContext, "商品介绍不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		Sizes.clear();
		SizeBean bean1 = new SizeBean();
		String guige = et_guige.getText().toString().trim();
		if (TextUtils.isEmpty(guige)) {
			Toast.makeText(mContext, "规格不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			bean1.setName(guige);
		}
		String kucun = et_kucun.getText().toString().trim();
		if (TextUtils.isEmpty(kucun)) {
			Toast.makeText(mContext, "库存不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			bean1.setInventory(kucun);
			Sizes.add(bean1);
		}

		for (int i = 0; i < ll_guige_container.getChildCount(); i++) {
			EditText et_guige = (EditText) ll_guige_container.getChildAt(i)
					.findViewById(R.id.et_guige);
			EditText et_kucun = (EditText) ll_guige_container.getChildAt(i)
					.findViewById(R.id.et_kucun);
			if (TextUtils.isEmpty(et_guige.getText().toString().trim())
					|| TextUtils.isEmpty(et_kucun.getText().toString().trim())) {
				Toast.makeText(mContext, "请将库存规格填写完整", Toast.LENGTH_SHORT).show();
				return;
			}
			SizeBean bean = new SizeBean();
			if (!TextUtils.isEmpty(et_guige.getText().toString().trim())) {
				bean.setName(et_guige.getText().toString().trim());
			}

			bean.setInventory(et_kucun.getText().toString().trim());
			Sizes.add(bean);
		}
		RequestUploadProductDataBean bean = MyApplication.getInstance()
				.getPublishUtil().getBean();
		bean.setPrice(et_price.getText().toString().trim());
		bean.setSizes(Sizes);
		bean.setDesc(et_introduce.getText().toString().trim());
		bean.setSku_Code(et_product_number.getText().toString().trim());

		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		HttpControl httpControl = new HttpControl();
		if (upPicProgress == 0) {
			if (!TextUtils.isEmpty(pic1)) {// 第一张图片不为空，上传第一张图片
				pic = pic1;
			} else {// 第一张图片为空，设置标记位，开始上传第二张
				upPicProgress = 1;
			}

		}
		if (upPicProgress == 1) {// 如果第二张不为空，上传第二张图片
			if (!TextUtils.isEmpty(pic2)) {
				pic = pic2;
			} else {// 第二张为空，设置标记位，上传第三张图片
				upPicProgress = 2;
			}
		}
		if (upPicProgress == 2) {
			if (!TextUtils.isEmpty(pic3)) {// 如果第三章不为空，上传第三章
				pic = pic3;
			}
		}
		httpControl.syncUpload(pic, new SaveCallback() {

			@Override
			public void onProgress(String arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(String arg0, OSSException arg1) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}

			@Override
			public void onSuccess(String arg0) {
				switch (upPicProgress) {
				case 0:// 第一张图片上传完毕
					if (new File(FileUtils.getRootPath() + "/tagPic/"
							+ "tagPic" + SharedUtil.getUserId(mContext)
							+ "0.png").exists()) {
						String imageName1 = pic1.substring(
								pic1.lastIndexOf("/") + 1, pic1.length());
						MyApplication.getInstance().getPublishUtil().getBean()
								.getImages().get(0).setImageUrl(imageName1);
					}
					if (!TextUtils.isEmpty(pic2)) {// 第二张图片不为空
						upPicProgress = 1;
						uploadImage();
					} else {
						if (!TextUtils.isEmpty(pic3)) {// 第三张图片不为空
							upPicProgress = 2;
							uploadImage();
						} else {
							runOnUiThread(new Runnable() {
								public void run() {
									publishProduct();
								}
							});
						}
					}
					break;
				case 1:// 第二张上传完毕
					if (new File(FileUtils.getRootPath() + "/tagPic/"
							+ "tagPic" + SharedUtil.getUserId(mContext)
							+ "1.png").exists()) {
						String imageName2 = pic2.substring(
								pic2.lastIndexOf("/") + 1, pic2.length());
						List<ProductImagesBean> aa = MyApplication
								.getInstance().getPublishUtil().getBean()
								.getImages();
						MyApplication.getInstance().getPublishUtil().getBean()
								.getImages().get(1).setImageUrl(imageName2);
					}
					if (!TextUtils.isEmpty(pic3)) {
						upPicProgress = 2;
						uploadImage();
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								publishProduct();
							}
						});
					}
					break;
				case 2:// 第三张上传完毕
					if (new File(FileUtils.getRootPath() + "/tagPic/"
							+ "tagPic" + SharedUtil.getUserId(mContext)
							+ "2.png").exists()) {
						String imageName3 = pic3.substring(
								pic3.lastIndexOf("/") + 1, pic3.length());
						MyApplication.getInstance().getPublishUtil().getBean()
								.getImages().get(2).setImageUrl(imageName3);
					}
					// MyApplication.getInstance().getPublishUtil().setBean(null);
					runOnUiThread(new Runnable() {
						public void run() {
							publishProduct();
						}
					});
				default:
					break;
				}

			}
		});
	}

	private void saveBitmapToFile(final int index, String imageUrl) {
		File dir = new File(FileUtils.getRootPath() + "/tagPic/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "tagPic" + SharedUtil.getUserId(mContext)
				+ index + ".png");
		File file2 = new File(dir, "tagPic_yuan"
				+ SharedUtil.getUserId(mContext) + index + ".png");
		if (!file.exists() && !file2.exists()) {
			try {
				final FileOutputStream out = new FileOutputStream(file);
				final FileOutputStream out2 = new FileOutputStream(file2);

				MyApplication.getInstance().getImageLoader()
						.loadImage(imageUrl, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String arg0, View arg1) {

							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {

							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap bitmap) {
								try {
									bitmap.compress(Bitmap.CompressFormat.PNG,
											100, out);
									out.flush();
									out.close();

									bitmap.compress(Bitmap.CompressFormat.PNG,
											100, out2);
									out2.flush();
									out2.close();
								} catch (Exception e) {
									// TODO: handle exception
								}

								if (index < MyApplication.getInstance()
										.getPublishUtil().getBean().getImages()
										.size() - 1) {
									saveBitmapToFile(index + 1,
											MyApplication.getInstance()
													.getPublishUtil().getBean()
													.getImages().get(index + 1)
													.getImageUrl());
								} else {
									setImageView();
								}

							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {

							}
						});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			setImageView();
		}

	}

	@Override
	protected void onDestroy() {
		if (bitmap0 != null) {
			bitmap0.recycle();
		}
		if (bitmap1 != null) {
			bitmap1.recycle();
		}
		if (bitmap2 != null) {
			bitmap2.recycle();
		}
		MyApplication.getInstance().removeActivity(this);
		super.onDestroy();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& KeyEvent.ACTION_DOWN == event.getAction()) {
			if ("发布".equals(tv_publish.getText().toString().trim())) {
				DialogUtils utils = new DialogUtils();
				utils.alertDialog(mContext, "提示", "您确定要取消发布商品吗？",
						new DialogUtilInter() {
							@Override
							public void dialogCallBack(int... which) {
								MyApplication.getInstance().finishActivity(
										EditPicActivity.class);
								MyApplication.getInstance().finishActivity(
										ActivityCapture.class);
								MyApplication.getInstance().finishActivity(
										PublishProductActivity.class);
							}
						}, true, "确定", "取消", false, true);
			} else {// 修改
				MyApplication.getInstance().finishActivity(
						EditPicActivity.class);
				MyApplication.getInstance().finishActivity(
						ActivityCapture.class);
				MyApplication.getInstance().finishActivity(
						PublishProductActivity.class);
			}

		}
		return super.onKeyDown(keyCode, event);
	}

}
