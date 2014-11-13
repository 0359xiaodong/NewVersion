package com.test.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.test.R;
import com.test.MenuBottom;
import com.test.base.CustomDialog;
import com.test.base.MyAdapter;
import com.test.base.MyApplication;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.base.Url;
import com.test.model.Address;
import com.test.model.Coupon;
import com.test.utils.ConnectServer;
import com.test.utils.ErrorMsg;
import com.test.utils.NetworkAction;
import com.test.model.Product;
import com.test.product.SubmitSuccess;
import com.test.pay.PayMethod;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SubmitOrder extends NormalActivity implements OnClickListener,
		android.widget.CompoundButton.OnCheckedChangeListener {

	private Title title;// ���ñ�����
	private LinearLayout addressBigLayout;// �ջ���ַģ��
	private TextView addressMsg;// û���ջ���ַʱ�����ʾ
	private LinearLayout addressLayout;// ���ջ���ַʱ����ʾ������ģ��
	private TextView addressName;// �ջ���ַ����
	private TextView addressPhone;// �ջ���ַ�绰
	private TextView addressStreet;// �ջ���ַ�ֵ�
	private RadioButton online; // ���߸��ť
	private RadioButton receive; // �������ť
	private EditText buyerMsg;//�������
	
	private TextView submitBtn;// �ύ��ť
	// private LinearLayout defaultLayout; // Ĭ�ϵ�ַ��
	private LinearLayout couponLayout;// �Ż�ȯģ��
	// private TextView defaultNamePhone;// Ĭ����ʾ�������ֻ��ŵ��ı���
	// private TextView defaultAddressTxt;// Ĭ����ʾ�ĵ�ַ
	// private TextView changeAddressBtn;// �޸ĵ�ַ��ť
	private TextView noCouponTxt;// �����Ż�ȯ�ı���
	private boolean editAddress = false;// �Ƿ����޸ĵ�ַ��״̬ģʽ
	private ListView productListView;// ��Ʒչʾ�б�
	private MyAdapter productAdapter;
	private ArrayList<Object> products;// ��Ʒ����
	private RadioGroup paywayGroup;
	private String payway = "1";// ���ʽ��1����֧����2��������
	private Address defaultAddress = null;
	private HashMap<String, Coupon> couponList;// �Ż�ȯ����
	private String productID;// ��ƷID
	private TextView totalPriceTxt;// ��Ʒ�ܼ�
	private TextView freightPriceTxt;// �˷��ܼ�
	private TextView couponPriceTxt;// �Żݵļ۸�
	private TextView realPriceTxt;// ʵ����
	private double oldRealPrice;

	private HashMap<String, String> paramterAddress;// �ջ���ַ��������
	private HashMap<String, String> paramterFreight;
	private HashMap<String, String> paramtersubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ����Ǵ��޸ĵ�ַ�Ľ��淵�����Ļ������»�ȡ��ˢ��һ��Ĭ�ϵ�ַ
		// if (editAddress) {
		// defaultAddress = null;
		// sendData(NetworkAction.��ȡ�ջ���ַ�б�, null);
		// editAddress = false;
		// }
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(4);
		title.setTitleTxt("ȷ�϶���");
		addressBigLayout = (LinearLayout) findViewById(R.id.address_big_layout);
		addressMsg = (TextView) findViewById(R.id.address_msg);
		addressLayout = (LinearLayout) findViewById(R.id.address_layout);
		addressName = (TextView) findViewById(R.id.submit_address_name);
		addressPhone = (TextView) findViewById(R.id.submit_address_phone);
		addressStreet = (TextView) findViewById(R.id.submit_address_street);
		buyerMsg= (EditText) findViewById(R.id.buyer_msg);
		
		totalPriceTxt = (TextView) findViewById(R.id.submit_totalprice);// ��Ʒ�ܼ�
		freightPriceTxt = (TextView) findViewById(R.id.submit_freightprice);// �˷��ܼ�
		// couponPriceTxt = (TextView) findViewById(R.id.submit_couponprice);//
		// �Żݵļ۸�
		realPriceTxt = (TextView) findViewById(R.id.submit_realprice);// ʵ����
		couponLayout = (LinearLayout) findViewById(R.id.submit_coupon_layout);// �Ż�ȯģ��
		noCouponTxt = (TextView) findViewById(R.id.submit_nocoupon_txt);// �����Ż�ȯ�ı���
		couponList = new HashMap<String, Coupon>();
		// defaultLayout = (LinearLayout)
		// findViewById(R.id.submit_default_layout); // Ĭ�ϵ�ַ��
		// defaultNamePhone = (TextView)
		// findViewById(R.id.submit_default_name_phone);// Ĭ����ʾ�������ֻ��ŵ��ı���
		// defaultAddressTxt = (TextView)
		// findViewById(R.id.submit_default_address);// Ĭ����ʾ�ĵ�ַ
		// changeAddressBtn = (TextView)
		// findViewById(R.id.submit_change_address_btn);// �޸ĵ�ַ��ť
		// changeAddressBtn.setOnClickListener(this);
		submitBtn = (TextView) findViewById(R.id.submit_btn);
		submitBtn.setOnClickListener(this);
		productListView = (ListView) findViewById(R.id.submit_product_listview);
		paywayGroup = (RadioGroup) findViewById(R.id.submit_radiogroup);
		online = (RadioButton) findViewById(R.id.submit_payway_online);
		receive = (RadioButton) findViewById(R.id.submit_payway_receive);
		online.setOnCheckedChangeListener(this);
		receive.setOnCheckedChangeListener(this);
		online.setChecked(true);
	}

	private void initData() {
		Intent intent = getIntent();
		// ��ȡ�Ӳ�Ʒ����ҳ�洫�����ĵ�����Ʒ���ߴӹ��ﳵ�������Ķ����Ʒ����ArrayList<Object>����
		products = (ArrayList<Object>) intent.getSerializableExtra("products");
		// ��ȡ�ջ���ַ
		paramterAddress = new HashMap<String, String>();
		paramterAddress.put("act", "addrlist");
		paramterAddress.put("sessionid", MyApplication.seskey);
		paramterAddress.put("uid", MyApplication.uid);
		paramterAddress.put("sid", MyApplication.sid);
		ConnectServer.getResualt(this, paramterAddress, NetworkAction.��ȡ�ջ���ַ�б�,
				Url.URL_USERS);
		productAdapter = new MyAdapter(this, NetworkAction.�ύ����, products);
		productListView.setDivider(null);
		productListView.setAdapter(productAdapter);
		refreshListViewHeight();
		// ��ȡ�˷�
		paramterFreight = new HashMap<String, String>();
		paramterFreight.put("act", "freight");
		paramterFreight.put("sid", MyApplication.sid);
		// ��ȡ������Ʒ�۸�
		getTotalPrice();
		paramtersubmit = new HashMap<String, String>();

		// // ��ȡ������Ʒ��Ӧ����Ч���Ż�ȯ
		// getCouponList();
	}

	// ��ȡ������Ʒ�ļ۸�
	private void getTotalPrice() {
		double tatoalPrice = 0;
		// double freightPrice = 0;
		double realPrice = 0;
		for (int i = 0; i < products.size(); i++) {
			double tempTatoalPrice = Double.valueOf(((Product) products.get(i))
					.getTotalPrice());
			// double tempFreightPrice = Double
			// .valueOf(((Product) products.get(i)).getFreight());
			tatoalPrice = tatoalPrice + tempTatoalPrice;
			// freightPrice = freightPrice + tempFreightPrice;
		}
		// realPrice = tatoalPrice + freightPrice;
		realPrice = tatoalPrice;
		totalPriceTxt.setText("��" + String.valueOf(tatoalPrice));
		freightPriceTxt.setText("��" + String.valueOf("0.00"));
		realPriceTxt.setText("��" + String.valueOf(realPrice));
		paramterFreight.put("order_price", String.valueOf(tatoalPrice));
		ConnectServer.getResualt(this, paramterFreight, NetworkAction.��ȡ�˷�,
				Url.URL_ORDER);
	}

	/**
	 * ��ȡ������Ʒ��Ӧ����Ч���Ż�ȯ
	 */
	private void getCouponList() {
		// �����Ʒ����������0��ѯ���е���Ʒ��Ӧ���Ż�ȯ
		if (products.size() > 0) {
			noCouponTxt.setVisibility(View.GONE);
			for (int i = 0; i < products.size(); i++) {
				// productID=((Product)products.get(i)).getId();
				sendData(NetworkAction.��ѯ������Ʒ���Ż�ȯ, products.get(i));
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.address_big_layout:// �޸ĵ�ַ��ť
			editAddress = true;
			// Intent intent = new Intent().setClass(this,
			// PersonAddressManager.class);
			// startActivity(intent);
			break;
		case R.id.submit_btn:// �ύ��ť
			// �ύ������ʱ���ж�һ���Ƿ�����Ч���ջ���ַ��
			if (defaultAddress == null) {
				Toast.makeText(this, "��û���ջ���ַ��������ջ���ַ�Ժ����ύ", 2000).show();
				return;
			}
			paramtersubmit.put("act", "CartConfirm");
			paramtersubmit.put("sessionid", MyApplication.seskey);
			paramtersubmit.put("uid", MyApplication.uid);
			paramtersubmit.put("sid", MyApplication.sid);
			paramtersubmit.put("username", MyApplication.sp.getString("username", ""));
			paramtersubmit.put("cart_num", "" + products.size());
			paramtersubmit.put("totalMoney", ""
					+ realPriceTxt.getText().toString().substring(1));
			paramtersubmit.put("realname", defaultAddress.getRealname());
			paramtersubmit.put("mobile", defaultAddress.getMobile());
			paramtersubmit.put("address", defaultAddress.getStreet());
			paramtersubmit.put("provincesid", defaultAddress.getProvince_id());
			paramtersubmit.put("citysid", defaultAddress.getCity_id());
			paramtersubmit.put("countysid", defaultAddress.getCounty_id());
			paramtersubmit.put("payway", payway);
			paramtersubmit.put("timeid", "0");
			paramtersubmit.put("form", "2");
			paramtersubmit.put("note", buyerMsg.getText().toString());
			paramtersubmit.put("Freight", freightPriceTxt.getText().toString()
					.substring(1));
			paramtersubmit.put("PayType", "4");
			paramtersubmit.put("cartlist", orderJSON());
			ConnectServer.getResualt(this, paramtersubmit, NetworkAction.�ύ����,
					Url.URL_ORDER);
			break;
		}

	}

	public void sendData(final NetworkAction request, Object object) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.��ȡ�ջ���ַ�б�)) {
			url = Url.URL_USERS;
			paramter.put("act", "addrlist");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
		} else if (request.equals(NetworkAction.��ѯ������Ʒ���Ż�ȯ)) {
			url = Url.URL_MEMBER;
			paramter.put("act", "GetCouponByProductID");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
			paramter.put("product_id", ((Product) object).getId());
		} else if (request.equals(NetworkAction.�ύ����)) {
			url = Url.URL_ORDER;
			paramter.put("act", "CartConfirm");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
			paramter.put("username", MyApplication.sp.getString("username", ""));
			paramter.put("cart_num", "" + products.size());
			// double totalMoney=0;
			// for (int i = 0; i < products.size(); i++) {
			// totalMoney=totalMoney+Double.valueOf(((Product)products.get(i)).getTotalPrice());
			// }

			// �ύ������ʱ���ж�һ���Ƿ�����Ч���ջ���ַ��
			if (defaultAddress == null) {
				Toast.makeText(this, "��û���ջ���ַ��������ջ���ַ�Ժ����ύ", 2000).show();
				return;
			}
			paramter.put("totalMoney", ""
					+ realPriceTxt.getText().toString().substring(1));
			paramter.put("realname", defaultAddress.getRealname());
			paramter.put("mobile", defaultAddress.getMobile());
			paramter.put("address", defaultAddress.getStreet());
			paramter.put("provincesid", defaultAddress.getProvince_id());
			paramter.put("citysid", defaultAddress.getCity_id());
			paramter.put("countysid", defaultAddress.getCounty_id());
			paramter.put("payway", payway);
			paramter.put("timeid", "0");
			paramter.put("form", "2");
			paramter.put("note", "");
			paramter.put("Freight", freightPriceTxt.getText().toString()
					.substring(1));
			paramter.put("PayType", "4");
			paramter.put("cartlist", orderJSON());
		} else if (request.equals(NetworkAction.��ȡ�˷�)) {
			url = Url.URL_ORDER;
			paramter.put("act", "freight");
			paramter.put("sid", MyApplication.sid);
			paramter.put("order_price", object.toString());
		}
		Log.i(MyApplication.TAG, request + MyApplication.getUrl(paramter, url));
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG, request + "response-->"
									+ response.toString());
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								if (request.equals(NetworkAction.��ȡ�ջ���ַ�б�)) {

								} else if (request
										.equals(NetworkAction.��ѯ������Ʒ���Ż�ȯ)) {
									JSONArray coupons = response
											.getJSONArray("list");
									if (coupons.length() > 0) {
										Log.i(MyApplication.TAG,
												"coupons-->start");
										couponLayout
												.setVisibility(View.VISIBLE);
										LinearLayout layout = new LinearLayout(
												couponLayout.getContext());
										layout.setOrientation(1);
										LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
												LayoutParams.MATCH_PARENT,
												LayoutParams.WRAP_CONTENT);
										layoutParams.setMargins(0, 6, 0, 0);
										TextView productName = new TextView(
												layout.getContext());
										productName
												.setTextColor(MyApplication.resources
														.getColor(R.color.gray));
										// productName.setPadding(0, 0, 20, 0);
										RadioGroup couponGroup = new RadioGroup(
												layout.getContext());
										couponGroup.setOrientation(1);// horizontal
																		// 0
																		// vertical
																		// 1
										for (int i = 0; i < coupons.length(); i++) {
											JSONObject couponObject = coupons
													.getJSONObject(i);
											Coupon coupon = new Coupon();
											coupon.setCouponID(couponObject
													.getString("CouponID"));
											coupon.setProductID(couponObject
													.getString("ProductID"));
											coupon.setProductName(couponObject
													.getString("ProductName"));
											coupon.setPriceLine(couponObject
													.getString("PriceLine"));
											coupon.setPrice(couponObject
													.getString("Price"));
											productName.setText(coupon
													.getProductName());
											RadioGroup.LayoutParams layoutParams1 = new RadioGroup.LayoutParams(
													LayoutParams.WRAP_CONTENT,
													LayoutParams.WRAP_CONTENT);
											layoutParams1.setMargins(30, 7, 0,
													0);
											RadioButton couponRadio = (RadioButton) LayoutInflater
													.from(couponGroup
															.getContext())
													.inflate(
															R.layout.radio_item,
															null);
											// couponRadio.setBackgroundDrawable(MyApplication.resources.getDrawable(R.drawable.radio_item));
											// couponRadio.setButtonDrawable(null);
											couponRadio.setTag(coupon);
											couponRadio.setId(i);
											couponRadio.setTextSize(14);
											couponRadio.setText("��"
													+ coupon.getPriceLine()
													+ "Ԫ��" + coupon.getPrice()
													+ "Ԫ�Ż�ȯ");
											couponRadio
													.setTextColor(MyApplication.resources
															.getColor(R.color.gray));
											couponGroup.addView(couponRadio,
													layoutParams1);
											// couponList.put(coupon.getProductID(),
											// coupon);
											// couponList.add(coupon);
										}
										couponGroup
												.setOnCheckedChangeListener(new OnCheckedChangeListener() {

													@Override
													public void onCheckedChanged(
															RadioGroup group,
															int checkedId) {
														RadioButton couponRadio = (RadioButton) group
																.getChildAt(checkedId);
														Coupon coupon = (Coupon) couponRadio
																.getTag();
														boolean exist = false;
														// ����Ż�ȯ�б����Ƿ���ѡ�����Ʒ��Ӧ�����Ż�ȯ
														Iterator iter = couponList
																.entrySet()
																.iterator();
														while (iter.hasNext()) {
															Map.Entry entry = (Map.Entry) iter
																	.next();
															Object key = entry
																	.getKey();
															if (coupon
																	.getProductID()
																	.equals(key
																			.toString()))
																exist = true;
															// Object val =
															// entry.getValue();
														}
														// ����ǵ�һ��ѡ�����Ʒ���Ż�ȯ
														if (!exist) {
															// �����޸��Ż�ȯ�۸�֮ǰ���Ż�ȯ�Żݵļ۸�
															coupon.setOldCouponPrice(couponPriceTxt
																	.getText()
																	.toString()
																	.substring(
																			1));
															// �����޸�ʵ����֮ǰ��ʵ����۸�
															coupon.setOldRealPrice(realPriceTxt
																	.getText()
																	.toString()
																	.substring(
																			1));
															// ����ҳ�����Ż�ȯ�Żݵ����¼۸�
															double couponPrice = Double
																	.valueOf(couponPriceTxt
																			.getText()
																			.toString()
																			.substring(
																					1));
															couponPrice = couponPrice
																	+ Double.valueOf(coupon
																			.getPrice());
															couponPriceTxt.setText("��"
																	+ String.valueOf(couponPrice));
															// ����ҳ����ʵ����ļ۸�
															double realPrice = Double
																	.valueOf(realPriceTxt
																			.getText()
																			.toString()
																			.substring(
																					1));
															realPrice = realPrice
																	- Double.valueOf(coupon
																			.getPrice());
															realPriceTxt.setText("��"
																	+ String.valueOf(realPrice));
															// ���뵽�Ż�ȯ������
															couponList.put(
																	coupon.getProductID(),
																	coupon);

														} else {

															Coupon oldCoupon = couponList.get(coupon
																	.getProductID());
															// Toast.makeText(SubmitOrder.this,
															// "oldCoupon==>"+oldCoupon.getPrice(),
															// 2000).show();
															double couponPrice = Double
																	.valueOf(oldCoupon
																			.getOldCouponPrice());
															couponPrice = couponPrice
																	+ Double.valueOf(coupon
																			.getPrice());
															couponPriceTxt.setText("��"
																	+ String.valueOf(couponPrice));

															double realPrice = Double
																	.valueOf(oldCoupon
																			.getOldRealPrice());
															realPrice = realPrice
																	- Double.valueOf(coupon
																			.getPrice());
															realPriceTxt.setText("��"
																	+ String.valueOf(realPrice));
														}
													}
												});
										layout.addView(productName);
										layout.addView(couponGroup);
										couponLayout.addView(layout);
									}
								} else if (request.equals(NetworkAction.�ύ����)) {
									// ɾ�����ﳵ���ύ�ɹ�����Ʒ
									Log.i(MyApplication.TAG, "start");
									for (int i = 0; i < MyApplication.shopCartList
											.size(); i++) {
										Product product = (Product) MyApplication.shopCartList
												.get(i);
										if (product.isChecked()) {
											MyApplication.shopCartList
													.remove(i);

											if (MyApplication.shopCartList
													.size() == 1)
												MyApplication.shopCartList
														.clear();
										}
									}
									Log.i(MyApplication.TAG, "sub suc->"
											+ MyApplication.shopCartList.size());
									try {
										MyApplication.shopCartManager
												.saveProducts(MyApplication.shopCartList);
									} catch (Exception e) {
										// TODO: handle exception
									}
									// ���������֧���Ļ���ת��֧��ҳ��
									Intent intent = new Intent();
									if (payway.equals("1")) {
										Toast.makeText(SubmitOrder.this,
												"�ύ�����ɹ�", 2000).show();
										intent.setClass(SubmitOrder.this,
												PayMethod.class);
										intent.putExtra("subject",
												response.getString("subject"));
										intent.putExtra("price", realPriceTxt
												.getText().toString()
												.substring(1));
										intent.putExtra("oid",
												response.getString("NEWID"));
									}
									// ����ǻ�������Ļ���ת���ύ�����ɹ�ҳ��
									else if (payway.equals("2")) {
										intent.setClass(SubmitOrder.this,
												SubmitSuccess.class);
									}
									startActivity(intent);
									finish();
									// showResult();
								}
							} else {
								Toast.makeText(
										SubmitOrder.this,
										request
												+ ErrorMsg.getErrorMsg(request,
														code), 2000).show();
							}
						} catch (JSONException e) {
							Log.i(MyApplication.TAG, request
									+ "JSONException-->" + e.getMessage());
						}
					}

				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(MyApplication.TAG, request + "onErrorResponse-->"
								+ error.getMessage());
					}
				});
	}

	/**
	 * 
	 */
	public String orderJSON() {
		StringBuffer json = new StringBuffer();
		// ��Ϣ���ϣ��������Ʒ��Ϣ
		json.append("[");
		for (int i = 0; i < products.size(); i++) {
			Product product = (Product) products.get(i);
			if (i > 0)
				json.append(",");
			json.append("{");
			json.append("\"product_id\":\"").append(product.getId())
					.append("\"");
			if (product.isHaveGift())
				json.append(",\"gift_name\":\"")
						.append(product.getGift().getName()).append("\"");
			json.append(",\"product_num\":\"").append(product.getNum())
					.append("\"");
			json.append(",\"store_price\":\"").append(product.getStorePrice())
					.append("\"");
			json.append(",\"reference_price\":\"")
					.append(product.getReferencePrice()).append("\"");
			json.append(",\"product_photo\":\"").append(product.getOrderImg())
					.append("\"");
			json.append(",\"totalPrice\":\"").append(product.getTotalPrice())
					.append("\"");
			json.append(",\"product_name\":\"").append(product.getName())
					.append("\"");
			json.append(",\"date\":\"").append(product.getDate()).append("\"");
			json.append(",\"Nature\":\"").append(product.getNature())
					.append("\"");
			if (product.isHaveAttribute())
				json.append(",\"attribute\":\"")
						.append(product.getAttribute().getName()).append("\"");
			else
				json.append(",\"attribute\":\"").append("").append("\"");
			json.append(",\"date\":\"").append(product.getDate()).append("\"");
			json.append(",\"Freight\":\"").append(product.getFreight())
					.append("\"");
			json.append(",\"Note\":\"").append(buyerMsg.getText().toString()).append("\"");
			// ʹ�õ��Ż�ȯ
			Coupon coupon = couponList.get(product.getId());
			if (coupon != null)
				json.append(",\"CouponID\":\"").append(coupon.getCouponID())
						.append("\"");
			else
				json.append(",\"CouponID\":\"").append("").append("\"");
			// ���͵��Ż�ȯ��������Ʒ���������Ʒ�ܽ�����priceline����ȡ����priceline��Ӧ���Ż�ȯ
			ArrayList<Coupon> coupons = product.getCoupons();
			double totalPrice = Double.valueOf(product.getTotalPrice());
			Coupon giftCoupon = null;
			// ����Ʒ���ܶ��ÿһ��priceline���бȽϣ�ȡ�������˵�����priceline������λ��
			ArrayList<Integer> index = new ArrayList<Integer>();
			for (int j = 0; j < coupons.size(); j++) {
				Coupon couponItem = coupons.get(j);
				if (!couponItem.getPriceLine().equals("")) {
					double priceLine = Double
							.valueOf(couponItem.getPriceLine());
					if (totalPrice > priceLine)
						index.add(j);
				}
			}
			Log.i(MyApplication.TAG, "index.size()->" + index.size());
			Log.i(MyApplication.TAG, "totalPrice->" + totalPrice);
			// ����г������Ż�ȯ��¼�������г����˵��Ż�ȯ���Ƚϼ�������������Ľ����Ż�ȯ���
			if (index.size() > 0) {
				for (int j = 0; j < index.size(); j++) {
					Coupon couponItem = coupons.get(index.get(j));
					if (j == 0)
						giftCoupon = couponItem;
					else {
						double newLine = Double.valueOf(couponItem
								.getPriceLine());
						double oldLine = Double.valueOf(giftCoupon
								.getPriceLine());
						Log.i(MyApplication.TAG, "newLine->" + newLine
								+ " oldLine-->" + oldLine);
						if (newLine > oldLine)
							giftCoupon = couponItem;
					}
				}
			} else// �����Ʒ�ܼ�û�г����κ�һ���Ż�ȯ��ȫ������Ϊ��
			{
				Coupon emptyCoupon = new Coupon();
				emptyCoupon.setCouponID("0");
				emptyCoupon.setStoreID("");
				emptyCoupon.setProductID("");
				emptyCoupon.setProductName("");
				emptyCoupon.setPriceLine("");
				emptyCoupon.setPrice("");
				giftCoupon = emptyCoupon;
			}
			Log.i(MyApplication.TAG,
					"giftCoupon.getPrice()->" + giftCoupon.getPrice());
			// �õ����Ż�ȯ��Ϣ
			json.append(",\"CouponsID\":\"").append(giftCoupon.getCouponID())
					.append("\"");

			json.append(",\"PriceID\":\"").append(product.getPriceID())
					.append("\"");
			json.append("}");

		}
		json.append("]");
		return json.toString();
	}

	// private void showResult() {
	// // �ύ�����ɹ��Ժ�ˢ�¹��ﳵ
	// try {
	// MyApplication.shopCartManager
	// .saveProducts(MyApplication.shopCartList);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// CustomDialog.Builder builder = new CustomDialog.Builder(this);
	// Log.i(MyApplication.TAG, "payway-->" + payway);
	// // ��������߸�����ʾ�鿴������ȥ����
	// if (payway.equals("1")) {
	// builder.setMessage("�����ύ�ɹ�")
	// .setPositiveButton("�鿴����",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	// goToPage(2);
	// dialog.cancel();
	// }
	// })
	// .setNegativeButton("ȥ����",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	// // �򿪸���ҳ��
	// dialog.cancel();
	// }
	// });
	// CustomDialog alert = builder.create();
	// alert.show();
	// }
	// // ����ǻ�����������ʾȥ����
	// else if (payway.equals("2")) {
	// builder.setMessage("�����ύ�ɹ�")
	// .setPositiveButton("�鿴����",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	// goToPage(2);
	// dialog.cancel();
	// }
	// })
	// .setNegativeButton("�ٹ��",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	// goToPage(1);
	// dialog.cancel();
	// }
	// });
	// CustomDialog alert = builder.create();
	// alert.show();
	// }
	//
	// }
	//
	// // ����1����ǰ����ҳ��䣬����2����ȥ����ҳ��
	// private void goToPage(int page) {
	//
	// MyApplication.goToOrder = true;
	//
	// // ����1����ǰ����ҳ��䣬����2����ȥ����ҳ��
	// if (page == 1) {
	// MenuBottom.tabHost.setCurrentTab(0);
	// MenuBottom.radioGroup.check(R.id.main_tab_home);
	// } else if (page == 2) {
	// MenuBottom.tabHost.setCurrentTab(4);
	// MenuBottom.radioGroup.check(R.id.main_tab_personcenter);
	// }
	//
	// SubmitOrder.this.finish();
	// }

	// ˢ��listview�ĸ߶�
	public void refreshListViewHeight() {
		int totalHeight = 0;
		for (int index = 0, len = productAdapter.getCount(); index < len; index++) {

			View listViewItem = productAdapter.getView(index, null,
					productListView);

			// ��������View �Ŀ��

			listViewItem.measure(0, 0);

			// ������������ĸ߶Ⱥ�

			totalHeight += listViewItem.getMeasuredHeight() + 5;

		}
		ViewGroup.LayoutParams params = productListView.getLayoutParams();

		// listView.getDividerHeight()��ȡ�����ָ����ĸ߶�

		// params.height����ListView��ȫ��ʾ��Ҫ�ĸ߶�

		params.height = totalHeight
				+ (productListView.getDividerHeight() * (productAdapter
						.getCount() - 1));

		productListView.setLayoutParams(params);
	}

	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		if (request.equals(NetworkAction.��ȡ�ջ���ַ�б�)) {
			JSONArray lists = response.getJSONArray("adr_info");
			// ���û���ջ���ַ�Ļ������ı�������
			if (lists.length() < 1) {
				addressMsg.setVisibility(View.VISIBLE);
				addressLayout.setVisibility(View.GONE);
				return;
			} else {
				addressMsg.setVisibility(View.GONE);
				addressLayout.setVisibility(View.VISIBLE);
			}
			ArrayList<Address> addressList = new ArrayList<Address>();
			for (int i = 0; i < lists.length(); i++) {
				JSONObject items = lists.getJSONObject(i);
				Address address = new Address();
				address.setAddressID(items.getString("address_id"));
				address.setProvince_id(items.getString("province_id"));
				address.setProvince_name(items.getString("province_name"));
				address.setCity_id(items.getString("city_id"));
				address.setCity_name(items.getString("city_name"));
				address.setCounty_id(items.getString("county_id"));
				address.setCounty_name(items.getString("county_name"));
				address.setRealname(items.getString("realname"));
				address.setMobile(items.getString("mobile"));
				address.setAddress(items.getString("address"));
				address.setTag(items.getString("tag"));
				if (address.getTag().equals("1")) {
					defaultAddress = address;
				}
				addressList.add(address);
			}
			// ���û������Ĭ�ϵ�ַ��Ĭ����ʾ��һ����ַ
			if (defaultAddress == null) {
				defaultAddress = addressList.get(0);
			}
			// ����Ĭ�ϵ�ַ�������͵绰
			addressName.setText(defaultAddress.getRealname());
			addressPhone.setText(defaultAddress.getMobile());
			// ���õ�ַ
			addressStreet.setText(defaultAddress.getStreet());
		} else if (request.equals(NetworkAction.��ȡ�˷�)) {
			double freight = response.getDouble("freight");
			double realPrice = Double.valueOf(realPriceTxt.getText().toString()
					.substring(1));
			realPrice = realPrice + freight;
			freightPriceTxt.setText("��" + String.valueOf(freight));
			realPriceTxt.setText("��" + String.valueOf(realPrice));
		}
		else if (request.equals(NetworkAction.�ύ����)) {
			// ɾ�����ﳵ���ύ�ɹ�����Ʒ
			Log.i(MyApplication.TAG, "start");
			for (int i = 0; i < MyApplication.shopCartList
					.size(); i++) {
				Product product = (Product) MyApplication.shopCartList
						.get(i);
				if (product.isChecked()) {
					MyApplication.shopCartList
							.remove(i);

					if (MyApplication.shopCartList
							.size() == 1)
						MyApplication.shopCartList
								.clear();
				}
			}
			Log.i(MyApplication.TAG, "sub suc->"
					+ MyApplication.shopCartList.size());
			try {
				MyApplication.shopCartManager
						.saveProducts(MyApplication.shopCartList);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// ���������֧���Ļ���ת��֧��ҳ��
			Intent intent = new Intent();
			if (payway.equals("1")) {
				Toast.makeText(SubmitOrder.this,
						"�ύ�����ɹ�", 2000).show();
				intent.setClass(SubmitOrder.this,
						PayMethod.class);
				intent.putExtra("subject",
						response.getString("subject"));
				intent.putExtra("price", realPriceTxt
						.getText().toString()
						.substring(1));
				intent.putExtra("oid",
						response.getString("NEWID"));
			}
			// ����ǻ�������Ļ���ת���ύ�����ɹ�ҳ��
			else if (payway.equals("2")) {
				intent.setClass(SubmitOrder.this,
						SubmitSuccess.class);
			}
			startActivity(intent);
			finish();
			// showResult();
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			buttonView.setBackgroundDrawable(MyApplication.resources
					.getDrawable(R.drawable.style_btn_bg));
			buttonView.setTextColor(MyApplication.resources
					.getColor(R.color.white));
			switch (buttonView.getId()) {
			case R.id.submit_payway_online:// ���߸����ѡ��״̬
				payway = "1";
				break;
			case R.id.submit_payway_receive:// ���������ѡ��״̬
				payway = "2";
				break;

			}
		} else {
			buttonView.setBackgroundDrawable(MyApplication.resources
					.getDrawable(R.drawable.style_bolder_bg));
			buttonView.setTextColor(MyApplication.resources
					.getColor(R.color.style_color));
		}
		buttonView.setPadding(10, 5, 10, 5);
		
	}
}
