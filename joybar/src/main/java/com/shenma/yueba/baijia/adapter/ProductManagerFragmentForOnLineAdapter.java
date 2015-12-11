package com.shenma.yueba.baijia.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.ProductManagerForOnLineBean;
import com.shenma.yueba.inter.RefreshProductListInter;
import com.shenma.yueba.util.DialogUtilInter;
import com.shenma.yueba.util.DialogUtils;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.PopwindowUtil;
import com.shenma.yueba.util.ShareUtil;
import com.shenma.yueba.util.ShareUtil.ShareListener;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.activity.ProductManagerActivity;
import com.shenma.yueba.yangjia.activity.PublishProductActivity;

public class ProductManagerFragmentForOnLineAdapter extends BaseAdapterWithUtil {
	private List<ProductManagerForOnLineBean> mList;
	private int flag;
	private RefreshProductListInter inter;
	DialogUtils dialog = new DialogUtils();
	Context ctx;

	public ProductManagerFragmentForOnLineAdapter(Context ctx,
			List<ProductManagerForOnLineBean> mList, int flag,
			RefreshProductListInter inter) {
		super(ctx);
		this.ctx = ctx;
		this.mList = mList;
		this.flag = flag;
		this.inter = inter;
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.product_manager_list_item,
					null);
			holder.iv_product = (ImageView) convertView
					.findViewById(R.id.iv_product);
			// holder.tv_brand_name = (TextView) convertView
			// .findViewById(R.id.tv_brand_name);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);

			holder.tv_button1 = (TextView) convertView
					.findViewById(R.id.tv_button1);
			holder.tv_button2 = (TextView) convertView
					.findViewById(R.id.tv_button2);
			holder.tv_button3 = (TextView) convertView
					.findViewById(R.id.tv_button3);
			holder.tv_button4 = (TextView) convertView
					.findViewById(R.id.tv_button4);

			FontManager.changeFonts(ctx, holder.tv_price,
					holder.tv_description, holder.tv_size, holder.tv_price,
					holder.tv_button1, holder.tv_button2, holder.tv_button3,
					holder.tv_button4);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.tv_button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (flag == 1 || flag == 2) {// 下架
					dialog.alertDialog(ctx, "提示", "您确认要下架该商品吗？",
							new DialogUtilInter() {
								@Override
								public void dialogCallBack(int... which) {
									setOnLineOrOffLine(position,
											mList.get(position).getProductId(),
											0);
								}
							}, true, "确定", "取消", true, true);

				} else if (flag == 0) {// 上架

					dialog.alertDialog(ctx, "提示", "您确认要上架该商品吗？",
							new DialogUtilInter() {
								@Override
								public void dialogCallBack(int... which) {
									setOnLineOrOffLine(position,
											mList.get(position).getProductId(),
											1);
								}
							}, true, "确定", "取消", true, true);

				}
			}
		});

		holder.tv_button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("复制".equals(((TextView) v).getText().toString().trim())) {
					dialog.alertDialog(ctx, "提示", "您确认要复制该商品吗？",
							new DialogUtilInter() {

								@Override
								public void dialogCallBack(int... which) {
									productCopy(mList.get(position)
											.getProductId());

								}
							}, true, "确定", "取消", true, true);

				}
			}
		});

		holder.tv_button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("分享".equals(((TextView) v).getText().toString().trim())) {
					ShareUtil.shareAll((Activity) ctx, mList.get(position).getProductName(),mList.get(position).getProductName(),
							mList.get(position).getShareLink(),
							mList.get(position).getPic(), new ShareListener() {

								@Override
								public void sharedListener_sucess() {
									requestShared(Integer.parseInt(mList.get(
											position).getProductId()));
								}

								@Override
								public void sharedListener_Fails(String msg) {
									MyApplication.getInstance().showMessage(
											ctx, msg);
								}
							});
				}
			}
		});

		holder.tv_button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag == 1 || flag == 2) {// 修改
					Log.i("position", position + "--------");
					Intent intent = new Intent(ctx,
							PublishProductActivity.class);
					intent.putExtra("from", "productManager");
					intent.putExtra("id", mList.get(position).getProductId());
					//intent.putExtra("data", mList.get(position).getDetail());
					((Activity)ctx).startActivityForResult(intent, 100);
				} else if (flag == 0) {// 删除
					dialog.alertDialog(ctx, "提示", "您确认要删除该商品吗？",
							new DialogUtilInter() {

								@Override
								public void dialogCallBack(int... which) {
									deleteProduct(position, mList.get(position)
											.getProductId());

								}
							}, true, "确定", "取消", true, true);
				}
			}
		});

		if (flag == 1) {// 在线商品
			holder.tv_button1.setText("下架");
			holder.tv_button2.setText("复制");
			holder.tv_button3.setText("分享");
			holder.tv_button4.setText("修改");
		} else if (flag == 2) {// 即将下线
			holder.tv_button1.setText("下架");
			holder.tv_button2.setText("复制");
			holder.tv_button3.setText("分享");
			holder.tv_button4.setText("修改");
		} else if (flag == 0) {// 下线商品
			holder.tv_button1.setText("上架");
			holder.tv_button2.setText("复制");
			holder.tv_button3.setText("分享");
			holder.tv_button4.setText("删除");
		}

		if (TextUtils.isEmpty(mList.get(position).getPic())) {
			bitmapUtils.display(holder.iv_product, "aaa");
		} else {
			bitmapUtils.display(holder.iv_product,
					ToolsUtil.getImage(mList.get(position).getPic(), 120, 0));
		}
		// holder.tv_brand_name.setText(ToolsUtil.nullToString(mList.get(position)
		// .getBrandName()));
		holder.tv_date.setText(ToolsUtil.nullToString(mList.get(position)
				.getExpireTime()));
		holder.tv_description.setText(ToolsUtil.nullToString(mList
				.get(position).getProductName()));
		holder.tv_size.setText(ToolsUtil.nullToString(TextUtils.isEmpty(mList
				.get(position).getStoreItemNo()) ? "" : "货号："
				+ mList.get(position).getStoreItemNo()));
		holder.tv_price.setText("￥"
				+ ToolsUtil.nullToString(mList.get(position).getPrice()));
		return convertView;
	}

	class Holder {
		ImageView iv_product;
		// TextView tv_brand_name;
		TextView tv_price;
		TextView tv_description;
		TextView tv_date;
		TextView tv_size;

		TextView tv_button1;
		TextView tv_button2;
		TextView tv_button3;
		TextView tv_button4;
	}

	/**
	 * 复制商品
	 */
	private void productCopy(String productId) {
		HttpControl httpControl = new HttpControl();
		httpControl.productCopy(productId, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				Toast.makeText(ctx, "复制成功", Toast.LENGTH_SHORT).show();
				if (flag == 1) {// 在线商品
					inter.refreshOnLineProduct(0);
				}
				if (flag == 2) {// 即将下线
					inter.refreshOnLineProduct(1);
				}
				if (flag == 0) {// 下线商品
					inter.refreshOnLineProduct(2);
				}
			}

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

			}
		}, ctx);

	}

	/**
	 * 上线/下线接口
	 *
	 * @param status
	 */
	private void setOnLineOrOffLine(final int position, String id,
			final int status) {
		HttpControl httpControl = new HttpControl();
		httpControl.setProductOnLineOrOffLine(id, status,
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						Toast.makeText(ctx, status == 1 ? "上线成功" : "下架成功", Toast.LENGTH_SHORT)
								.show();
						if (flag == 1) {// 在线商品
							inter.refreshOnLineProduct(0);
						}
						if (flag == 2) {// 即将下线
							inter.refreshOnLineProduct(1);
						}
						if (flag == 0) {// 下线商品
							inter.refreshOnLineProduct(2);
						}
						notifyDataSetChanged();
					}

					@Override
					public void http_Fails(int error, String msg) {

					}
				}, ctx, true, true);

	}

	/**
	 * 删除商品接口
	 *
	 */
	private void deleteProduct(final int position, String id) {
		HttpControl httpControl = new HttpControl();
		httpControl.deleteProduct(id, new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				Toast.makeText(ctx, "删除成功", Toast.LENGTH_SHORT).show();
				inter.refreshOnLineProduct(2);
			}

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

			}
		}, ctx, true, true);

	}

	/*****
	 * 分享成功后 回调
	 * ****/
	void requestShared(int productid) {
		HttpControl httpControl = new HttpControl();
		httpControl.createProductShare(productid, false,
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						if (obj != null && obj instanceof BaseRequest) {
							MyApplication.getInstance()
									.showMessage(ctx, "分享成功");
						} else {
							MyApplication.getInstance()
									.showMessage(ctx, "分享失败");
						}
					}

					@Override
					public void http_Fails(int error, String msg) {
						MyApplication.getInstance().showMessage(ctx, msg);
					}
				}, ctx);
	}

}
