package com.shenma.yueba.yangjia.adapter;

import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.activity.BaijiaPayActivity;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.baijia.modle.PayResponseFormBean;
import com.shenma.yueba.inter.RefreshOrderListener;
import com.shenma.yueba.util.DialogUtilInter;
import com.shenma.yueba.util.DialogUtils;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.modle.OrderItem;
import com.shenma.yueba.yangjia.modle.ProductItemBean;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SalesManagerForAttestationBuyerAdapter extends BaseAdapterWithUtil {
	private List<OrderItem> mList;
	private int tag;//标记不同的订单状态
	private RefreshOrderListener lisner;
	private DialogUtils dialog = new DialogUtils();
	public SalesManagerForAttestationBuyerAdapter(Context ctx,List<OrderItem> mList,int tag,RefreshOrderListener lisner) {
		super(ctx);
		this.mList = mList;
		this.tag = tag;
		this.lisner = lisner;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	@SuppressWarnings("null")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if(convertView == null){
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.sales_manager_item_for_attestation_buyer, null);
			holder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
			holder.tv_commission = (TextView) convertView.findViewById(R.id.tv_commission);
			holder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
			holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
			holder.tv_money_payed = (TextView) convertView.findViewById(R.id.tv_money_payed);
			holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
			holder.tv_same_product_count = (TextView) convertView.findViewById(R.id.tv_same_product_count);
			holder.tv_huohao = (TextView) convertView.findViewById(R.id.tv_huohao);
			holder.tv_guige = (TextView) convertView.findViewById(R.id.tv_guige);
			holder.ll_bottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
			holder.tv_bottom = (TextView) convertView.findViewById(R.id.tv_bottom);
			holder.tv_bottom.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(v.getTag()!=null && v.getTag() instanceof OrderItem)
					{
						final OrderItem orderItem=(OrderItem)v.getTag();
						if("充值并退款".equals(holder.tv_bottom.getText().toString().trim())){
							
							Intent intent=new Intent(ctx,BaijiaPayActivity.class);
							PayResponseFormBean bean=new PayResponseFormBean();
							bean.setOrderNo(orderItem.getRmaNo());
							bean.setPrice(Double.parseDouble(orderItem.getAmount()));
							bean.setContent(ToolsUtil.nullToString(orderItem.getProducts().get(0).getName()));
							bean.setDesc("充值并退款");
							bean.setUrl(com.shenma.yueba.constants.Constants.WX_PAYAREFUND_URL);
							intent.putExtra("PAYDATA",bean);
							ctx.startActivity(intent);
						}else if("确认退款".equals(holder.tv_bottom.getText().toString().trim())){
							dialog.alertDialog(ctx, "提示", "您确认要退款吗？",
									new DialogUtilInter() {
										@Override
										public void dialogCallBack(int... which) {
											confirmBack(orderItem);

										}
									}, true, "确定", "取消", true, true);
						}
					}	
				}
			});
			FontManager.changeFonts(ctx, holder.tv_order_number,holder.tv_order_status,
					holder.tv_money_payed,holder.tv_product_name,holder.tv_price,holder.tv_description,
					holder.tv_same_product_count,holder.tv_huohao,holder.tv_guige,holder.tv_commission);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		OrderItem item = mList.get(position);
		if(item!=null){
			holder.tv_bottom.setTag(item);
			if(0 == tag){//全部订单
				holder.tv_commission.setText(("佣金￥" + ToolsUtil.DounbleToString_2(item.getInCome())));
						ToolsUtil.setTextColorBlackAndRed(holder.tv_money_payed, "实付：￥", ToolsUtil.getTextColorRed(item.getAmount()));
				if("3".equals(mList.get(position).getStatus()) && mList.get(position).isIsNeedRma()){
					holder.ll_bottom.setVisibility(View.VISIBLE);
					if(mList.get(position).isIsGoodsPick()){//订单状态为退货处理中
						holder.tv_bottom.setText("充值并退款");
					}else{//还未提款
						holder.tv_bottom.setText("确认退款");
					}
				}else{
					holder.ll_bottom.setVisibility(View.GONE);
				}
			}else if(1 == tag){//待付款
				holder.tv_commission.setText("佣金：￥"+item.getInCome());
				ToolsUtil.setTextColorBlackAndRed(holder.tv_money_payed, "实付：￥", ToolsUtil.getTextColorRed(item.getAmount()));
			}else if(2 == tag){//专柜自提
				holder.tv_commission.setText("佣金：￥"+item.getInCome());
				ToolsUtil.setTextColorBlackAndRed(holder.tv_money_payed, "实付：￥", ToolsUtil.getTextColorRed(item.getAmount()));
			}else if(3 == tag){//售后服务
				holder.tv_commission.setText("退还佣金：￥"+item.getInCome());
				ToolsUtil.setTextColorBlackAndRed(holder.tv_money_payed, "退款金额：￥", ToolsUtil.getTextColorRed(item.getAmount()));
				if(mList.get(position).isIsNeedRma()){
					holder.ll_bottom.setVisibility(View.VISIBLE);
					if(mList.get(position).isIsGoodsPick()){//已经提款
						holder.tv_bottom.setText("充值并退款");
					}else{//还未提款
						holder.tv_bottom.setText("确认退款");
					}
				}else{
					holder.ll_bottom.setVisibility(View.GONE);
				}
			}
			holder.tv_order_number.setText(TextUtils.isEmpty(item.getOrderNo())?"":"订单号："+item.getOrderNo());
			holder.tv_order_status.setText(TextUtils.isEmpty(item.getStatusName())?"":item.getStatusName());
			if(item.getProducts()!=null && item.getProducts().size()>0){//说明有商品
				List<ProductItemBean>  products = item.getProducts();
				ProductItemBean productItme = products.get(0);
				if(!TextUtils.isEmpty(productItme.getPicture())){
					bitmapUtils.display(holder.iv_product, ToolsUtil.getImage(productItme.getPicture(), 120, 0));
					holder.tv_price.setText(TextUtils.isEmpty(productItme.getPrice())?"":"￥"+productItme.getPrice());
					holder.tv_product_name.setText(TextUtils.isEmpty(productItme.getBrandName())?"":productItme.getBrandName());
					holder.tv_description.setText(TextUtils.isEmpty(productItme.getName())?"":productItme.getName());
					holder.tv_same_product_count.setText(TextUtils.isEmpty(productItme.getCount())?"":"X"+productItme.getCount());
					holder.tv_huohao.setText(TextUtils.isEmpty(productItme.getStoreItemNo())?"":"货号："+productItme.getStoreItemNo());
					holder.tv_guige.setText(TextUtils.isEmpty(productItme.getSizeName())?"":"规格："+productItme.getSizeName());
				}
			}
			
		}
		
		return convertView;
	}
	
	
	/**
	 * 确认退款
	 * @param orderItem OrderItem
	 */
	private void confirmBack(OrderItem orderItem){
		HttpControl httpControl = new HttpControl();
		httpControl.comformBack(orderItem.getOrderNo(), new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				Toast.makeText(ctx, "退款成功", Toast.LENGTH_SHORT).show();
				lisner.refreshOrderList(tag);
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
				
			}
		}, ctx, true);
	}
	
	class Holder{
		TextView tv_order_number;//订单号
		TextView tv_order_status;//订单状态
		TextView tv_money_payed;//实付价格
		ImageView iv_product;//商品图片
		TextView tv_product_name;//商品名称
		TextView tv_price;//商品价格
		TextView tv_description;//商品描述
		TextView tv_same_product_count;//商品个数
		TextView tv_huohao;//货号
		TextView tv_guige;//规格
		TextView tv_commission;//规格
		LinearLayout ll_bottom;
		TextView tv_bottom;
	}

}
