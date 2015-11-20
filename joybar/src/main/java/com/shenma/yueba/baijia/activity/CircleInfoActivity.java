package com.shenma.yueba.baijia.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.MyCircleInfoAdapter;
import com.shenma.yueba.baijia.dialog.QRCodeShareDialog;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.Base64Coder;
import com.shenma.yueba.util.CustomProgressDialog;
import com.shenma.yueba.util.FileUtils;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.PhotoUtils;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.MyGridView;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.view.SelectePhotoType;
import com.shenma.yueba.yangjia.activity.ModifyCircleNameActivity;
import com.shenma.yueba.yangjia.modle.CircleDetailBackBean;
import com.shenma.yueba.yangjia.modle.CircleDetailBean;
import com.shenma.yueba.yangjia.modle.KaiXiaoPiaoBackBean;
import com.shenma.yueba.yangjia.modle.Users;
import com.shenma.yueba.yangjia.modle.Users.User_Type;
import com.shenma.yueba.yangjia.modle.kaixiaoPiaoBean;
import com.umeng.analytics.MobclickAgent;

/**
 * 圈子信息
 * 
 * @author a
 * 
 */

public class CircleInfoActivity extends BaseActivityWithTopView implements
		OnClickListener {

	private TextView tv_cirlce_head_title;
	private TextView tv_cirlce_name_title;
	private TextView tv_circle_title;
	private TextView tv_circle_name;
	private ImageView imageView1, imageView2;
	private RoundImageView riv_circle_head;
	private MyGridView gv_circle;
	private MyCircleInfoAdapter adapter;
	private List<Users> mList = new ArrayList<Users>();
	private RelativeLayout rl_head;
	private RelativeLayout rl_circle_name;
	private RelativeLayout rl_qrcode;
	private String cricleId;
	private String littlePicPath;//小图路径
	private String littlePicPath_cache;//裁剪后图片存储的路径
	private TextView tv_top_title_below;//标题下面的人数
	private Button bt_action;//分为 退出圈子，加入圈子，删除圈子三种状态
	private String from;//来自哪里  1表示来自养家的圈子管理，2表示来自推荐的圈子，3表示来自已经加入的圈子

	TextView tv_cirlce_head_erweima_title;//圈子二维码文本信息
	ImageView riv_circle_head_erweima_imageview;
	HttpControl httpControl = new HttpControl();
	private CustomProgressDialog customerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.circle_info_layout);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		initView();
		getIntentData();
	}

	private void getIntentData() {
		int _id = getIntent().getIntExtra("circleId", -1);
		cricleId = Integer.toString(_id);
		if (_id < 0) {
			MyApplication.getInstance().showMessage(this, "参数错误");
			finish();
			return;

		}
		from = getIntent().getStringExtra("from");
		getCircleDetail(cricleId, mContext, true);
	}

	private void initView() {
		setTitle("圈子信息");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		customerDialog = new CustomProgressDialog(mContext).createDialog(mContext);
		imageView1 = getView(R.id.imageView1);
		imageView2 = getView(R.id.imageView2);
		gv_circle = getView(R.id.gv_circle);
		riv_circle_head = getView(R.id.riv_circle_head);
		tv_top_title_below = getView(R.id.tv_top_title_below);
		tv_cirlce_head_title = getView(R.id.tv_cirlce_head_title);
		tv_cirlce_name_title = getView(R.id.tv_cirlce_name_title);
		tv_circle_title = getView(R.id.tv_circle_title);
		tv_circle_name = getView(R.id.tv_circle_name);
		bt_action = getView(R.id.bt_action);
		bt_action.setOnClickListener(this);
		rl_head = getView(R.id.rl_head);
		rl_head.setOnClickListener(this);
		rl_circle_name = getView(R.id.rl_circle_name);
		rl_circle_name.setOnClickListener(this);
		rl_qrcode = getView(R.id.rl_qrcode);
		rl_qrcode.setOnClickListener(this);
		//mList.add(new Users());
		//mList.add(new Users());
		//adapter = new MyCircleInfoAdapter(mContext, mList,cricleId);
		//gv_circle.setAdapter(adapter);
		tv_cirlce_head_erweima_title=(TextView)findViewById(R.id.tv_cirlce_head_erweima_title);
		FontManager.changeFonts(mContext, tv_cirlce_head_title,
				tv_cirlce_name_title, tv_circle_title, tv_circle_name, bt_action, tv_top_title,tv_cirlce_head_erweima_title);

		//二维码
		riv_circle_head_erweima_imageview = (ImageView) findViewById(R.id.riv_circle_head_erweima_imageview);
		riv_circle_head_erweima_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QRCodeShareDialog qrCodeShareDialog = new QRCodeShareDialog(CircleInfoActivity.this,null);
				qrCodeShareDialog.show();
			}
		});

	}


	/**
	 * 设置可见或者不可见
	 *
	 * @param isVisible
	 */
	private void setVisibleState(boolean isVisible) {
		imageView1.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
		imageView2.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_head:// 更改头像
				ToolsUtil.hideSoftKeyboard(mContext, tv_circle_name);
				showBottomDialog();
				break;
			case R.id.rl_circle_name:// 更改姓名
				Intent modifyCircleNameIntent = new Intent(this,
						ModifyCircleNameActivity.class);
				modifyCircleNameIntent.putExtra("circleId", cricleId);
				modifyCircleNameIntent.putExtra("name", tv_circle_name.getText()
						.toString().trim());
				startActivityForResult(modifyCircleNameIntent,
						Constants.REQUESTCODE);
				break;
			case R.id.bt_action:
				if ("删除圈子".equals(bt_action.getText().toString().trim())) {
					deleteCircle();
				} else if ("退出圈子".equals(bt_action.getText().toString().trim())) {
					exitCircle();
				} else if ("加入圈子".equals(bt_action.getText().toString().trim())) {
					addCircle();
				}
				break;

//		case R.id.rl_qrcode:// 二维码查看
//			/*
//			 * ArrayList<String> urlList = new ArrayList<String>();
//			 * if(urlList.size() == 0){ Toast.makeText(mContext, "暂无大图展示",
//			 * 1000).show(); return ; }
//			 */
//			QRCodeShareDialog dialog = new QRCodeShareDialog(
//					CircleInfoActivity.this, null);
//			dialog.show();
//			/*
//			 * Intent imageShowIntent = new Intent(mContext,
//			 * ImageShowActivity.class);
//			 * imageShowIntent.putStringArrayListExtra(
//			 * ImageShowActivity.BIGIMAGES,urlList);
//			 * imageShowIntent.putExtra(ImageShowActivity.IMAGE_INDEX, 0);
//			 * startActivity(imageShowIntent);
//			 */
//			break;

			default:
				break;
		}
	}


	/*****
	 * 退出圈子
	 ****/
	void exitCircle() {
		httpControl.exitCircle(cricleId, true, new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				//刷新圈子信息
				getCircleDetail(cricleId, mContext, true);
			}

			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(CircleInfoActivity.this, msg);
			}
		}, CircleInfoActivity.this);
	}

	/*****
	 * 退出圈子
	 ****/
	void addCircle() {
		httpControl.addCircle(cricleId, true, new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				//刷新圈子信息
				getCircleDetail(cricleId, mContext, true);
			}

			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(CircleInfoActivity.this, msg);
			}
		}, CircleInfoActivity.this);
	}


	/**
	 * 删除圈子
	 */
	private void deleteCircle() {
		httpControl.deleteCircle(cricleId, "", true, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
				setResult(Constants.RESULTCODE);
				CircleInfoActivity.this.finish();
			}

			@Override
			public void http_Fails(int error, String msg) {

				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

			}
		}, CircleInfoActivity.this);
	}

	/**
	 * 获取圈子详情
	 *
	 * @param ctx
	 * @param showDialog
	 */
	public void getCircleDetail(final String circleId, Context ctx, boolean showDialog) {
		HttpControl httpControl = new HttpControl();
		httpControl.getCircleDetail(circleId, showDialog,
				new HttpCallBackInterface() {


					@Override
					public void http_Success(Object obj) {
						CircleDetailBackBean result = (CircleDetailBackBean) obj;
						CircleDetailBean bean = result.getData();
						if (bean != null) {
							if (tv_top_title_below != null) {
								tv_top_title_below.setVisibility(View.VISIBLE);
								tv_top_title_below.setText("人数（" + bean.getMemberCount() + "）");
							}

							if (tv_circle_name != null) {
								tv_circle_name.setText(ToolsUtil.nullToString(bean.getGroupName()));
							}

							MyApplication.getInstance().getBitmapUtil().display(riv_circle_head, ToolsUtil.getImage(ToolsUtil.nullToString(bean.getGroupPic()), 100, 100));
							List<Users> users = bean.getUsers();
							if (users != null && users.size() > 0) {
								mList.clear();
								mList.addAll(users);
								if (bean.isIsOwer()) {
									Users adduser = new Users();
									adduser.setUser_Type(User_Type.jia);
									adduser.setNickName("邀请好友");
									mList.add(adduser);
									Users jianuser = new Users();
									jianuser.setUser_Type(User_Type.jian);
									jianuser.setNickName("删除成员");
									mList.add(jianuser);
								}
								adapter = new MyCircleInfoAdapter(mContext, mList, circleId, bean.isIsOwer());
								gv_circle.setAdapter(adapter);
							}
							//如果是创建者
							if (bean.isIsOwer()) {
								bt_action.setText("删除圈子");
								rl_head.setEnabled(true);
								rl_circle_name.setEnabled(true);
							} else {
								rl_head.setEnabled(false);
								rl_circle_name.setEnabled(false);
								if (bean.isIsMember()) {
									bt_action.setText("退出圈子");
								} else {
									bt_action.setText("加入圈子");
								}
							}
						} else {
							http_Fails(500, "数据错误");
						}
					}

					@Override
					public void http_Fails(int error, String msg) {
						Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
						CircleInfoActivity.this.finish();
					}
				}, CircleInfoActivity.this);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUESTCODE
				&& resultCode == Constants.RESULTCODE) {//修改圈子名称的返回监听
			if (data != null) {
				String newName = data.getStringExtra("newName");
				tv_circle_name.setText(ToolsUtil.nullToString(newName));
			}
		}
		if (requestCode == Constants.REQUESTCODE
				&& resultCode == Constants.RESULTCODE2) {//邀请粉丝加入圈子的返回监听
			getCircleDetail(cricleId, mContext, true);
		}

		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_ALBUM) {
			// 调用相册返回
			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				Uri uri = data.getData();
				String[] proj = {MediaStore.Images.Media.DATA};
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						littlePicPath = cursor.getString(column_index);
						// 裁剪之后存储的路径
						littlePicPath_cache = Environment
								.getExternalStorageDirectory().toString()
								+ File.separator
								+ UUID.randomUUID().toString()
								+ "littlePic.jpg";
						// 裁剪图片
						startActivityForResult(PhotoUtils.getZoomIntent(Uri
										.fromFile(new File(littlePicPath)), Uri
										.fromFile(FileUtils
												.createNewFile(littlePicPath_cache))),
								PhotoUtils.INTENT_REQUEST_CODE_CROP);
					}
				}
			}
		}
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CROP) {// 裁剪后返回
			if (resultCode == RESULT_OK) {
				Bitmap bm = BitmapFactory.decodeFile(littlePicPath_cache);
				riv_circle_head.setImageBitmap(bm);
				uploadPic();//上传头像
			}
		}
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CAMERA) {// 调用相机返回
			if (resultCode == RESULT_OK) {
				if (littlePicPath != null) {
					// 裁剪之后存储的路径
					littlePicPath_cache = Environment
							.getExternalStorageDirectory().toString()
							+ File.separator
							+ UUID.randomUUID().toString()
							+ "littlePic.jpg";
					// 裁剪图片
					startActivityForResult(PhotoUtils.getZoomIntent(Uri
									.fromFile(new File(littlePicPath)), Uri
									.fromFile(FileUtils
											.createNewFile(littlePicPath_cache))),
							PhotoUtils.INTENT_REQUEST_CODE_CROP);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 弹出底部菜单(相机和图库)
	 */
	protected void showBottomDialog() {
		ToolsUtil.hideSoftInputKeyBoard(CircleInfoActivity.this);
		ShowMenu showMenu = new ShowMenu(mContext, findViewById(R.id.parent),
				R.layout.camera_pic_popwindow);
		showMenu.createView();
	}

	/**
	 * 弹出底部菜单
	 *
	 * @author
	 */
	class ShowMenu extends SelectePhotoType {
		public ShowMenu(Activity activity, View parent, int popLayout) {
			super(activity, parent, popLayout);
		}

		@Override
		public void onExitClick(View v) {
			canceView();
		}

		@Override
		public void onCamera(View v) {
			if (ToolsUtil.isAvailableSpace(mContext)) {
				littlePicPath = PhotoUtils.takePicture(mContext);
			}
			canceView();
		}

		@Override
		public void onPic(View v) {
			if (ToolsUtil.isAvailableSpace(mContext)) {
				PhotoUtils.selectPhoto(mContext);
			}
			canceView();
		}

	}


	public void uploadPic() {
		customerDialog.show();
		HttpControl httpContorl = new HttpControl();
		httpContorl.syncUpload(littlePicPath_cache, new SaveCallback() {

			@Override
			public void onProgress(String arg0, int arg1, int arg2) {

			}

			@Override
			public void onFailure(String arg0, OSSException arg1) {
				if (customerDialog != null && customerDialog.isShowing()) {
					customerDialog.dismiss();
				}

			}

			@Override
			public void onSuccess(String arg0) {


				runOnUiThread(new Runnable() {
					public void run() {
						String picName = littlePicPath_cache.substring(littlePicPath_cache.lastIndexOf("/") + 1,
								littlePicPath_cache.length());
						HttpControl httpControl2 = new HttpControl();
						httpControl2.changeCircleLogo(cricleId, picName, false, new HttpCallBackInterface() {

							@Override
							public void http_Success(Object obj) {
								Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
								customerDialog.dismiss();
							}

							@Override
							public void http_Fails(int error, String msg) {
								customerDialog.dismiss();
								Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
							}
						}, CircleInfoActivity.this);
					}
				});


			}
		});
	}

	@Override
	protected void onDestroy() {
		MyApplication.getInstance().removeActivity(this);//加入回退栈
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

}
