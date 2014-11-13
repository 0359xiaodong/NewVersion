package com.test.product;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.test.MenuBottom;
import com.test.person.Login;
import com.test.base.ChangeTime;
import com.test.base.CustomDialog;
import com.test.base.MyAdapter;
import com.test.base.MyApplication;
import com.test.base.MyViewFlipper;
import com.test.base.MyWebView;
import com.test.base.Title;
import com.test.base.Url;
import com.test.model.Attribute;
import com.test.model.Coupon;
import com.test.model.Gift;
import com.test.utils.ConnectServer;
import com.test.utils.ErrorMsg;
import com.test.utils.NetworkAction;
import com.test.model.Product;
import com.test.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ProductDetail extends Activity implements OnClickListener,
		OnGestureListener {

	private Resources resources;
	private TextView secKillTime;// ��ɱ��Ʒʱ��
	private TextView commentNumTxt;// ��������
	private String num = "1";// ��¼��Ʒ����
	private ProgressDialog progressBar;// ������Ʒ����ʱ��Ľ�����
	private AlertDialog alertDialog;// ������Ʒ�������ʱ�����ʾ��
	private WebView webView;// ��ʾ��Ʒ�����webView
	String productUrl = "http://api2.xinlingmingdeng.com/products/"; // ��Ʒ������ַ
	private String productId;// ����������ƷID
	private String skid;// ��ɱ��ƷID
	private TextView name;// ��Ʒ����
	private TextView referencePrice;// ��Ʒ�ο��۸�
	public TextView storePrice;// ��Ʒ�۸�

	private String discount;// ��Ʒ�ۿ�
	private TextView cashBack;// ����
	public Product product;// ��Ʒʵ����
	public MyAdapter adapter;
	public TextView priceTxt;// ���۸�

	private FrameLayout call;// ��ϵ�ͷ���ť
	private FrameLayout comment;// �鿴���۰�ť
	private Button buyNow;// ��������ť
	private Button addShopcart;// ���빺�ﳵ��ť
	private int viewWidth;
	private int viewHeight;
	public static Handler secKillHandler;// ��ɱ����ʱhandler

	private GestureDetector gesture;
	private int photopage = 1;
	private int photototalpage;
	private LinearLayout.LayoutParams layoutParams;
	private String buyType = "1"; // Buy_type �������ͣ�1��������2��ɱ
	private String freight;// �˷�

	// �°��޸ĵĲ���
	private Title title;// ���ñ�����
	private RadioGroup flipperTxt;// �����ҳ���ͼƬ���½Ǹ����ƶ��ĵ�ѡ������
	private MyViewFlipper flipper;
	private boolean getHeight = false;// �Ƿ��л�ȡ��ͼƬ�ĸ߶�
	private int newHeight = 0;
	private LinearLayout timeLayout;// ��ʾ����ʱ������
	private LinearLayout discountLayout;// ������Ϣͷģ��
	private LinearLayout discountModuleLayout;// ������Ϣ�ۿۺ�������ģ������
	private TextView discountModuleTxt;// ������Ϣ�ۿ۵ı�ǩ
	private TextView cashbackModuleTxt;// ������Ϣ�����ı�ǩ
	private ImageView discountImg;// ������Ϣģ��ͼƬ
	private LinearLayout discountContentLayout;// ������Ϣ����ģ��
	private LinearLayout dContentLayout;// �ۿ�����ģ��
	private LinearLayout cContentLayout;// ��������ģ��
	private TextView discountTxt;// �ۿ��ı���
	private TextView cashbackTxt;// �����ı���
	private FrameLayout attributeLayout;// ��Ʒ����
	public TextView attributeTxt;// �����ı���
	public TextView numTxt;// ���Ե������ı���
	private TextView inventoryTxt;// ����ı���
	private ListView listView;// ���Ժ���Ʒ����
	private TextView attributeNumTxt;// ��������������
	private FrameLayout giftLayout;// ��Ʒģ��
	public TextView giftTxt;// �����Ʒ�ı���
	private TextView giftAttributeTxt;// ��ʾ�������Ʒ���ı���
	private LinearLayout commentLayout;// ��������ģ��
	private NetworkImageView commentImg;// ����ͷ��
	private TextView nameTxt;// �����û��ǳ�
	private TextView contentTxt;// ��������
	private TextView dateTxt;// ��������
	private int commentNum = 0;// ������
	private ScrollView scrollView;
	private boolean detailModule=false;//ͼ������ģʽ
	private boolean detailFinished=false;//ͼ�������Ƿ�������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);
		initView();
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(7);
		title.setTitleTxt("��Ʒ����");
		flipperTxt = (RadioGroup) findViewById(R.id.product_txt_layout);
		timeLayout = (LinearLayout) findViewById(R.id.product_seckill_outtime_layout);
		discountContentLayout = (LinearLayout) findViewById(R.id.product_content_layout);
		discountModuleLayout = (LinearLayout) findViewById(R.id.product_discount_title);
		discountModuleTxt = (TextView) findViewById(R.id.product_discount_txt);
		cashbackModuleTxt = (TextView) findViewById(R.id.product_cashback_txt);
		discountImg = (ImageView) findViewById(R.id.product_discount_img);
		dContentLayout = (LinearLayout) findViewById(R.id.product_dcontent_layout);
		cContentLayout = (LinearLayout) findViewById(R.id.product_ccontent_layout);
		discountTxt = (TextView) findViewById(R.id.product_discount);
		cashbackTxt = (TextView) findViewById(R.id.product_cashback);
		attributeTxt = (TextView) findViewById(R.id.product_attribute_txt);
		numTxt = (TextView) findViewById(R.id.product_attribute_num);
		inventoryTxt = (TextView) findViewById(R.id.product_attribute_invetory);
		giftTxt = (TextView) findViewById(R.id.product_gift_txt);
		scrollView = (ScrollView) findViewById(R.id.product_detail_scrollview);
		scrollView.setOnTouchListener(new TouchListenerImpl());

		// TODO Auto-generated method stub
		commentNumTxt = (TextView) findViewById(R.id.product_comment_num);
		discountLayout = (LinearLayout) findViewById(R.id.product_discount_layout);// ������Ϣģ��
		discountLayout.setOnClickListener(this);
		giftLayout = (FrameLayout) findViewById(R.id.product_gift_layout);// ��Ʒģ��
		giftLayout.setOnClickListener(this);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		gesture = new GestureDetector(this);
		flipper = (MyViewFlipper) findViewById(R.id.product_viewFlipper);
		flipper.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gesture.onTouchEvent(event);
			}
		});
		secKillTime = (TextView) findViewById(R.id.product_seckill_outtime);
		viewWidth = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		viewHeight = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		progressBar = new ProgressDialog(this);// ��ʼ��������
		webView = (WebView) findViewById(R.id.product_webview);// ��ʼ����Ʒ������ҳ����

		name = (TextView) findViewById(R.id.product_name);// ��Ʒ����
		referencePrice = (TextView) findViewById(R.id.product_reference_price);// ��Ʒ�۸�
		referencePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // �м�Ӻ���
		storePrice = (TextView) findViewById(R.id.product_store_price);// ��Ʒ�����۸�
		attributeLayout = (FrameLayout) findViewById(R.id.product_attribute_layout);// ��Ʒ����
		attributeLayout.setOnClickListener(this);
		call = (FrameLayout) findViewById(R.id.product_call);// ��ϵ�ͷ���ť
		call.setOnClickListener(this);
		comment = (FrameLayout) findViewById(R.id.product_comment);// �鿴���۰�ť
		comment.setOnClickListener(this);
		buyNow = (Button) findViewById(R.id.product_buynow);// ��������ť
		buyNow.setOnClickListener(this);
		addShopcart = (Button) findViewById(R.id.product_add_shopcart);// ���빺�ﳵ��ť
		addShopcart.setOnClickListener(this);
		resources = ProductDetail.this.getResources();

		secKillHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				// ȡ�����µ�ʱ��
				long time = bundle.getLong("time");
				// �����̷߳��صļ�����˵��µ�ʱ���ַ���
				String timeString = bundle.getString("timeString");
				if (time > 0)
					secKillTime.setText("����ʱ��" + timeString);
				else {
					// ������ɱ��Ʒ����ҳ��ĵ���ʱʱ��
					ChangeTime.secKillTime = -1;
					secKillTime.setText("��ɱ�ѽ���");
				}
			}
		};
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// ������ɱ��Ʒ����ҳ��ĵ���ʱʱ��
		ChangeTime.secKillTime = -1;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MyApplication.goToOrder) {
			MyApplication.goToOrder = false;
			this.finish();
		}
	}

	private void initData() {
		// ��ȡ����������ƷID�������Ϊ�����ȡ��Ʒ����ϸ��Ϣ
		Intent intent = getIntent();
		productId = intent.getStringExtra("productId");
		skid = intent.getStringExtra("skid");
		Log.i(MyApplication.TAG, "skid-->" + skid);
		if (skid == null || skid.equals("null"))
			sendData(NetworkAction.��Ʒ����);
		else {
			buyType = "2";
			sendData(NetworkAction.��ɱ��Ʒ����);
		}

		sendData(NetworkAction.�����б�);
		// ��ȡ��Ʒ������ҳ����ַ
		productUrl = productUrl + "?act=detail&sid=" + MyApplication.sid
				+ "&product_id=" + productId;
		// Log.i(MyApplication.TAG, "url-->"+productUrl);
		// ������Ʒ����
		 initWebView();
	}

	private void sendData(final NetworkAction request) {

		String url = "";
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.��Ʒ����)) {
			url = Url.URL_DETAILS;
			paramter.put("act", "ProductInto");
			paramter.put("product_id", productId);
			paramter.put("sid", MyApplication.sid);
		} else if (request.equals(NetworkAction.��ɱ��Ʒ����)) {
			url = Url.URL_SECKILL;
			paramter.put("act", "seckill");
			paramter.put("product_id", productId);
			paramter.put("sid", MyApplication.sid);
			paramter.put("SKID", skid);
		} else if (request.equals(NetworkAction.�����б�)) {
			url = Url.URL_ORDER;
			paramter.put("act", "comments_list");
			paramter.put("sid", MyApplication.sid);
			paramter.put("ProductID", productId);
			paramter.put("level", "0");
			paramter.put("page", "1");
			paramter.put("pagesize", "100000");
		}
		Log.i(MyApplication.TAG, request + MyApplication.getUrl(paramter, url));
		/*
		 * ���������������
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG, request + "response-->"
									+ response.toString());
							int code = response.getInt("code");

							if (response.getInt("code") == 1) {

								if (request.equals(NetworkAction.��ɱ��Ʒ����)
										|| request.equals(NetworkAction.��Ʒ����)) {
									product = new Product();
									JSONObject normalInfo = response
											.getJSONObject("info");
									JSONArray imgList = null;
									if (request.equals(NetworkAction.��ɱ��Ʒ����)) {

										buyType = "2";
										// ��ȡ�ؼ���Ϣ����
										long startTime = response
												.getLong("date");// ��ȡ��ʼʱ���
										Log.i(MyApplication.TAG, request
												+ " startTime-->" + startTime);
										JSONObject info = response
												.getJSONObject("SecondKill");
										// ��ȡ����ʱ���
										long outTime = info
												.getLong("OutEndTime");
										// �õ�����ɱ��Ʒ����ʣ���ʱ��
										long time = outTime - startTime;
										// ��ʾ��ɱ��Ʒ����ʱ�ı���
										timeLayout.setVisibility(View.VISIBLE);
										secKillTime.setTextSize(15);
										// ��ʼˢ�µ���ʱ�ı���
										ChangeTime.secKillTime = time;
										name.setText(info.getString("SKName"));
										if (!normalInfo.getString(
												"ReferencePrice")
												.equals("null"))
											referencePrice.setText("ԭ�ۣ���"
													+ normalInfo
															.getString("ReferencePrice"));
										else
											referencePrice
													.setVisibility(View.GONE);
										Log.i(MyApplication.TAG,
												"getStorePrice()->"
														+ info.getString("SKPrice"));
										storePrice.setText("��"
												+ info.getString("SKPrice"));

										// ��ʼ��ͼƬ����
										imgList = info.getJSONArray("Images");

										// --------------����Ϊ��Ҫ���ݵ��¸�ҳ������ݼ��ϣ�����Ϊҳ����ʾ���ݼ���-------------

										product.setStorePrice(info
												.getString("SKPrice"));
										product.setSkID(skid);
										product.setInventory(info
												.getString("SKLeftNum"));// ���ÿ����
										product.setSKName(info
												.getString("SKName"));
										product.setSKPrice(info
												.getString("SKPrice"));
										// �����ɱ��Ʒ������ID
										try {
											String priceID = info
													.getString("PriceID");
											if (priceID.equals("null"))
												product.setPriceID("0");
											else
												product.setPriceID(priceID);
										} catch (Exception e) {
											product.setPriceID("0");
										}
									} else if (request
											.equals(NetworkAction.��Ʒ����)) {
										buyType = "1";
										String tempname = normalInfo
												.getString("SubProductName");
										if (tempname.equals(""))
											name.setText(normalInfo
													.getString("ProductName"));
										else
											name.setText(tempname);
										// û��ԭ����Ϣ���ظ�ģ��
										if (!normalInfo.getString(
												"ReferencePrice")
												.equals("null"))
											referencePrice.setText("ԭ�ۣ���"
													+ normalInfo
															.getString("ReferencePrice"));
										else
											referencePrice
													.setVisibility(View.GONE);
										// ������Ʒ�۸�
										product.setStorePrice(normalInfo
												.getString("StorePrice"));
										// ������Ʒ����
										product.setCode(normalInfo
												.getString("ProductCode"));
										// ��ʼ��ͼƬ����
										imgList = response
												.getJSONArray("ImgInfo");

										// ��Ʒ����
										// JSONArray attributes = response
										// .getJSONArray("PriceList");
										// Log.i(MyApplication.TAG,
										// "attributes-->" + attributes);
										// if (attributes.length() >= 1) {
										// attributeLayout
										// .setVisibility(View.VISIBLE);
										// LinearLayout layout = new
										// LinearLayout(
										// attributeLayout
										// .getContext());
										// RadioGroup attributeGroup = new
										// RadioGroup(
										// layout.getContext());
										// attributeGroup.setOrientation(1);
										//
										// for (int i = 0; i < attributes
										// .length(); i++) {
										// RadioGroup.LayoutParams layoutParams1
										// = new RadioGroup.LayoutParams(
										// LayoutParams.WRAP_CONTENT,
										// LayoutParams.WRAP_CONTENT);
										// layoutParams1.setMargins(0, 7,
										// 0, 0);
										//
										// // �������Ե�ѡ��ť
										// // RadioButton attribute = new
										// // RadioButton(
										// // attributeGroup.getContext());
										// RadioButton attribute = (RadioButton)
										// LayoutInflater
										// .from(attributeGroup
										// .getContext())
										// .inflate(
										// R.layout.radio_item2,
										// null);
										// // ��ʾ��������
										// JSONObject item = attributes
										// .getJSONObject(i);
										// // ��ȡ�����Ե���Ʒ����
										// int num = item
										// .getInt("PriceNum");
										// // �������Ʒ�������㣬����ѡ��
										// if (num <= 0)
										// attribute.setEnabled(false);
										// Log.i(MyApplication.TAG,
										// "num-->" + num);
										// attribute.setText(item
										// .getString("PriceName"));
										// attribute.setTextColor(resources
										// .getColor(R.color.gray));
										// attribute.setId(i);
										// attribute.setTextSize(14);
										// attribute.setTag(
										// R.id.tag_first,
										// item.getString("Price"));
										// attribute.setTag(
										// R.id.tag_second,
										// item.getString("PriceNum"));
										// attribute.setTag(
										// R.id.tag_three,
										// item.getString("PriceID"));
										// attributeGroup.addView(
										// attribute,
										// layoutParams1);
										// if (i == 0) {
										// // �����Ʒ��priceID
										// product.setPriceID(item
										// .getString("PriceID"));
										// // ��������ı���
										// TextView attributeTxt = new TextView(
										// layout.getContext());
										// attributeTxt
										// .setText("��Ʒ���ԣ�  ");
										// attributeTxt
										// .setTextColor(resources
										// .getColor(R.color.gray));
										// attributeTxt.setPadding(0,
										// 15, 0, 0);
										// layout.addView(attributeTxt);
										// // Ĭ��ѡ����Ʒ���Եĵ�һ�����ԣ������ø����Եļ۸���ۿ���Ϣ
										// if (buyType.equals("1")
										// && discount != null) {
										// attribute
										// .setChecked(true);
										// // product.setAttribute(attribute
										// // .getText()
										// // .toString());
										// String Price = attribute
										// .getTag(R.id.tag_first)
										// .toString();
										// String newPrice =
										// String.valueOf(Double
										// .valueOf(Price)
										// * (Double
										// .valueOf(discount) / 100));
										// DecimalFormat df = new DecimalFormat(
										// ".00");
										// String lessPrice =
										// String.valueOf(df.format(Double
										// .valueOf(Price)
										// - Double.valueOf(newPrice)));
										// discountTxt
										// .setText(discount
										// + "��   �֣�"
										// + lessPrice);
										// storePrice.setText("��"
										// + newPrice);
										// // ���øò�Ʒ��������Լ۸�
										// product.setStorePrice(newPrice);
										// product.setInventory(item
										// .getString("PriceNum"));
										// }
										// }
										//
										// }
										// attributeGroup
										// .setOnCheckedChangeListener(new
										// OnCheckedChangeListener() {
										//
										// @Override
										// public void onCheckedChanged(
										// RadioGroup group,
										// int checkedId) {
										// RadioButton btn = (RadioButton) group
										// .getChildAt(checkedId);
										// String priceID = btn
										// .getTag(R.id.tag_three)
										// .toString();
										// product.setPriceID(priceID);
										// //
										// ������ͨ��Ʒ��ʱ���Ҹ���Ʒ���ۿ�ʱʹ�������Եļ۸���ۿۼ������µļ۸���Żݼ�
										// if (buyType
										// .equals("1")
										// && discount != null) {
										// String Price = btn
										// .getTag(R.id.tag_first)
										// .toString();
										// String newPrice = String
										// .valueOf(Double
										// .valueOf(Price)
										// * (Double
										// .valueOf(discount) / 100));
										// DecimalFormat df = new DecimalFormat(
										// ".00");
										// String lessPrice = String
										// .valueOf(df
										// .format(Double
										// .valueOf(Price)
										// - Double.valueOf(newPrice)));
										// discountTxt
										// .setText(discount
										// + "��   �֣�"
										// + lessPrice);
										// storePrice
										// .setText("��"
										// + newPrice);
										// // product.setAttribute(btn
										// // .getText()
										// // .toString());
										// product.setStorePrice(newPrice);
										// product.setInventory(btn
										// .getTag(R.id.tag_second)
										// .toString());
										// }
										// //
										// }
										// });
										// layout.addView(attributeGroup);
										// attributeLayout.addView(layout);
										// } else
										// product.setPriceID("0");

										// --------------------------��ͨ��Ʒ����Ʒ��Ϣ���-----------------------------------
										product.setInventory(normalInfo
												.getString("ProductLeft"));// �����
										product.setStorePrice(normalInfo
												.getString("StorePrice"));

									}

									// --------------------------��ͨ��Ʒ����ɱ��Ʒ��ͬ���Ե���Ϣ���---------------------------------
									product.setId(normalInfo
											.getString("ProductID"));// ��ƷID
									product.setFreight(normalInfo
											.getString("Freight"));// ��Ʒ�˷�
									product.setName(normalInfo
											.getString("ProductName"));// ��Ʒ����
									product.setBuy_type(buyType);
									product.setReferencePrice(normalInfo
											.getString("ReferencePrice"));
									product.setDate(String.valueOf(new Date()
											.getTime()));// ��¼���빺�ﳵ��ʱ��
									product.setNature(normalInfo
											.getString("Nature"));
									// ----------------------��ͬ����----------------------------------

									// ��ȡͼƬ���ű�����ĸ߶�
									final JSONObject itemTemp;
									itemTemp = imgList.getJSONObject(0);
									// �����ͼƬ����û�л�ȡ��ʵ������ͼƬ�Ŀ�߱�����ʱ���Ȼ�ȡ����Ը߶�
									if (imgList.length() > 0 && !getHeight) {
										Thread thread = new Thread(
												new Runnable() {

													@Override
													public void run() {

														try {
															String path = Url.URL_IMGPATH
																	+ itemTemp
																			.getString("FilePath");
															URL url = new URL(
																	path);
															String responseCode = url
																	.openConnection()
																	.getHeaderField(
																			0);
															Bitmap map = BitmapFactory
																	.decodeStream(url
																			.openStream());
															int height = map
																	.getHeight();
															int width = map
																	.getWidth();
															newHeight = (int) (MyApplication.width * ((double) height / width));
															getHeight = true;
															sendData(request);
														} catch (Exception e) {
															// TODO
															// Auto-generated
															// catch block
															e.printStackTrace();
														}

													}
												});
										thread.start();
										return;
									}
									// ͼƬ����
									for (int i = 0; i < imgList.length(); i++) {
										// ��¼ͼƬ����
										photopage = imgList.length();
										String m = Url.URL_IMGPATH
												+ ((JSONObject) imgList.get(i))
														.getString("FilePath");
										String imgPath = ((JSONObject) imgList
												.get(i)).getString("FilePath");
										product.setImgPath(imgPath);
										Log.i(MyApplication.TAG, "imgList->"
												+ imgList.length());
										NetworkImageView netView = new NetworkImageView(
												ProductDetail.this);
										netView.setAdjustViewBounds(false);
										LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
												LayoutParams.MATCH_PARENT,
												newHeight);
										netView.setLayoutParams(layoutParams);
										Log.i(MyApplication.TAG, "m->" + m);
										MyApplication.client
												.getImageForNetImageView(m,
														netView,
														R.drawable.ic_launcher);
										flipper.addView(netView);
										flipper.setInAnimation(
												ProductDetail.this,
												R.anim.view_in_from_right);
										flipper.setOutAnimation(
												ProductDetail.this,
												R.anim.view_out_to_left);
										RadioGroup.LayoutParams rbParams = new RadioGroup.LayoutParams(
												15, 15);
										rbParams.setMargins(0, 0, 10, 0);
										RadioButton rb = new RadioButton(
												flipperTxt.getContext());
										rb.setBackgroundDrawable(MyApplication.resources
												.getDrawable(R.drawable.product_img_bg));
										rb.setId(i);
										// ����͸��ɫ��ȥ��radiobuttonԭ����button��ť
										rb.setButtonDrawable(new ColorDrawable(
												Color.TRANSPARENT));
										if (i == 0)
											rb.setChecked(true);
										netView.setTag(rb);
										flipperTxt.addView(rb, rbParams);
									}
									photototalpage = imgList.length();

									// ��Ʒ����Ĵ�����Ϣģ��,ֻ����Է���ɱ��Ʒ��
									if (buyType.equals("1")) {

										// �ۿ���Ϣ����
										JSONArray discounts = response
												.getJSONArray("discount");
										// ������Ϣ����
										JSONArray coupons = response
												.getJSONArray("coupons");
										if (discounts.length() > 0
												|| coupons.length() > 0)
											// ��ʾ������Ϣģ��
											discountLayout
													.setVisibility(View.VISIBLE);
										Log.i(MyApplication.TAG,
												"discount length->"
														+ discounts.length());
										// ���ۿ���Ϣ��ʱ��
										if (discounts.length() > 0) {
											// ���ۿ���Ϣ��ʱ����ʾ�ۿ۱�ǩ������
											discountModuleTxt
													.setVisibility(View.VISIBLE);
											dContentLayout
													.setVisibility(View.VISIBLE);
											for (int i = 0; i < discounts
													.length(); i++) {
												JSONObject discountObject = discounts
														.getJSONObject(i);
												discount = discountObject
														.getString("Per");

												product.setDiscount(discountObject
														.getString("Per"));
												product.setDiscountCash(discountObject
														.getString("OffsetPrice"));
												discountTxt.setText(product
														.getDiscount()
														+ "��   �֣�"
														+ product
																.getDiscountCash());

											}
										}

										if (coupons.length() > 0) {
											cashbackModuleTxt
													.setVisibility(View.VISIBLE);
											cContentLayout
													.setVisibility(View.VISIBLE);
											// ����������Ϣ
											for (int i = 0; i < coupons
													.length(); i++) {
												JSONObject couponObject = coupons
														.getJSONObject(i);
												Coupon coupon = new Coupon();
												coupon.setCouponID(couponObject
														.getString("CouponID"));
												coupon.setStoreID(couponObject
														.getString("StoreID"));
												coupon.setProductID(couponObject
														.getString("ProductID"));
												coupon.setPrice(couponObject
														.getString("Price"));
												coupon.setStart_time(couponObject
														.getString("StartTime"));
												coupon.setEnd_time(couponObject
														.getString("EndTime"));
												coupon.setPriceLine(couponObject
														.getString("PriceLine"));
												product.addCoupon(coupon);
												// ��������
												String cashbackContent = "��������Ʒ����"
														+ couponObject
																.getString("PriceLine")
														+ "Ԫ������"
														+ couponObject
																.getString("Price")
														+ "Ԫ�Ż�ȯ";
												// ����ʱ��
												String time = couponObject
														.getString("StartTime")
														.substring(5, 7)
														+ "��"
														+ couponObject
																.getString(
																		"StartTime")
																.substring(8,
																		10)
														+ "�� ~ "
														+ couponObject
																.getString(
																		"EndTime")
																.substring(5, 7)
														+ "��"
														+ couponObject
																.getString(
																		"EndTime")
																.substring(8,
																		10)
														+ "��";
												if (i == 0) {
													cashbackTxt.setText(time
															+ "      "
															+ cashbackContent);
												} else {
													cashbackTxt
															.setText(cashbackTxt
																	.getText()
																	.toString()
																	+ "��"
																	+ cashbackContent);
												}
											}
										}
									}
									// ��ʼ����Ʒ����
									product.setNum("1");
									// ��ʼ����Ʒ������Ϣ
									JSONArray attributes = response
											.getJSONArray("PriceList");
									if (attributes.length() > 0) {
										attributeLayout
												.setVisibility(View.VISIBLE);
										for (int j = 0; j < attributes.length(); j++) {
											JSONObject item = attributes
													.getJSONObject(j);
											Attribute attribute = new Attribute();
											attribute.setID(item
													.getString("PriceID"));
											attribute.setName(item
													.getString("PriceName"));
											attribute.setPrice(item
													.getString("Price"));
											product.setAttributes(attribute);
											// Ĭ������ѡ���һ��
											if (j == 0) {
												product.setAttribute(attribute);
												attributeTxt.setText(product
														.getAttribute()
														.getName());
												numTxt.setText("��"
														+ product.getNum()
														+ "�� ");
												inventoryTxt.setText("�����"
														+ product
																.getInventory()
														+ "��");
											}
										}
										storePrice.setText("��"
												+ product.getAttribute()
														.getPrice());
									} else {
										product.setHaveAttribute(false);
										storePrice.setText("��"
												+ product.getStorePrice());
									}

									// ��Ʒ
									JSONArray gifts = response
											.getJSONArray("GiftList");
									if (gifts.length() >= 1) {
										giftLayout.setVisibility(View.VISIBLE);
										for (int j = 0; j < gifts.length(); j++) {
											JSONObject item = gifts
													.getJSONObject(j);
											Gift gift = new Gift();
											gift.setID(item.getString("GiftID"));
											gift.setName(item
													.getString("GiftName"));
											gift.setNum(item
													.getString("GiftTotal"));
											product.setGifts(gift);
											if (j == 0) {
												product.setGift(gift);
												giftTxt.setText(product
														.getGift().getName());
											}
										}
									} else
										product.setHaveGift(false);

								} else if (request.equals(NetworkAction.�����б�)) {
									commentNumTxt.setText("("
											+ response
													.getString("total_number")
											+ ")");
									int num = response.getInt("total_number");
									if (num > 0) {
										commentNum = num;
										commentLayout = (LinearLayout) findViewById(R.id.product_comment_layout);
										commentImg = (NetworkImageView) findViewById(R.id.product_detail_comment_img);
										nameTxt = (TextView) findViewById(R.id.product_detail_comment_name);
										contentTxt = (TextView) findViewById(R.id.product_detail_comment_content);
										dateTxt = (TextView) findViewById(R.id.product_detail_comment_date);
										commentLayout
												.setVisibility(View.VISIBLE);
										MyApplication.client
												.getImageForNetImageView("",
														commentImg,
														R.drawable.loginout_img);
										JSONObject item = response
												.getJSONArray("list")
												.getJSONObject(0);
										nameTxt.setText(item
												.getString("username"));
										contentTxt.setText(item
												.getString("comment_content"));
										dateTxt.setText(item
												.getString("createtime"));
									}
								}

							} else {
								Toast.makeText(
										ProductDetail.this,
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

	/*
	 * ��ʼ��webview
	 */
	protected void initWebView() {
//		// ��ƽ�����
//		progressBar = ProgressDialog.show(ProductDetail.this, null,
//				"���ڼ�����Ʒ���飬���Ժ�");
		// ���WebView���
		webView.getSettings().setJavaScriptEnabled(true);
		WebSettings settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// webView.getSettings().setSupportZoom(true);
		// webView.getSettings().setBuiltInZoomControls(true);
		// Log.i(MyApplication.TAG, "url-->"+url);
		webView.loadUrl(productUrl);
		alertDialog = new AlertDialog.Builder(this).create();
		// ������ͼ�ͻ���
		webView.setWebViewClient(new MyWebViewClient());
	}

	/*
	 * ���ü�����ҳʱ��ʾ���������޷�����ʱ����������ʾ
	 */
	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			progressBar.show();
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			detailFinished=true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Toast.makeText(ProductDetail.this, "��ҳ���س���", Toast.LENGTH_LONG);
			alertDialog.setTitle("ERROR");
			alertDialog.setMessage(description);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			alertDialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {

		case R.id.product_call:// ��ϵ�ͷ���ť
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setMessage("\t\tȷ������ͷ��绰?")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// �����ȷ�ϡ���Ĳ���
									Intent intent = new Intent(
											Intent.ACTION_CALL, Uri
													.parse("tel:"
															+ "4000838310"));
									ProductDetail.this.startActivity(intent);
									dialog.cancel();
								}
							})
					.setNegativeButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
			CustomDialog alert = builder.create();
			alert.show();

			break;
		case R.id.product_comment:// �鿴���۰�ť
			Intent commentIntent = new Intent();
			commentIntent.setClass(this, CommentList.class);
			commentIntent.putExtra("productID", product.getId());
			startActivity(commentIntent);
			break;
		case R.id.product_buynow:// ��������ť
			 product.setBuy_type(buyType);// ��¼�������Ʒ����
			 double pNumSub = Double.valueOf(product.getNum());
			 double priceSub = Double.valueOf(product.getStorePrice());
			 product.setTotalPrice(String.valueOf(pNumSub * priceSub));//
			 //���ø���Ʒ���ܼ�
			  if (MyApplication.loginStat) {
			  intent = new Intent(this, SubmitOrder.class);
			  ArrayList<Object> plist = new ArrayList<Object>();
			  plist.add(product);
			  intent.putExtra("products", plist);
			  } else {
			  intent = new Intent(this, Login.class);
			  }
			 startActivity(intent);
			break;
		case R.id.product_add_shopcart:// ���빺�ﳵ��ť
			double p1NumSub = Double.valueOf(product.getNum());
			double p1riceSub = Double.valueOf(product.getStorePrice());
			product.setTotalPrice(String.valueOf(p1NumSub * p1riceSub));// ���ø���Ʒ���ܼ�
			if (MyApplication.loginStat) {
				MyApplication.shopCartManager.add(product);

				CustomDialog.Builder builder1 = new CustomDialog.Builder(this);
				builder1.setMessage("�ɹ����빺�ﳵ��")
						.setPositiveButton("�ٹ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								})
						.setNegativeButton("ȥ���ﳵ",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										MenuBottom.tabHost.setCurrentTab(3);
										MenuBottom.radioGroup
												.check(R.id.main_tab_shopcart);
										ProductDetail.this.finish();
										dialog.cancel();
									}
								});
				CustomDialog alert1 = builder1.create();
				alert1.show();
				ArrayList<Object> products = null;
				try {
					products = MyApplication.shopCartManager.readShopCart();
				} catch (StreamCorruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// intent = new Intent(this, PersonLogin.class);
				startActivity(intent);
			}

			break;
		case 0:// ��̬��ӵ���Ʒ���԰�ť�ĵ���¼�
				// Toast.makeText(this, v.getTag().toString(), 2000).show();
			break;
		case R.id.product_discount_layout:// ������Ϣģ��
			// visible 0 gone 8
			if (discountContentLayout.getVisibility() == 8) {
				discountContentLayout.setVisibility(View.VISIBLE);
				discountImg.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.product_discount_open));
				discountModuleLayout.setVisibility(View.GONE);
			} else {
				discountContentLayout.setVisibility(View.GONE);
				discountImg.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.product_discount_close));
				discountModuleLayout.setVisibility(View.VISIBLE);
			}
			break;
		// ��Ʒ���ģ��
		case R.id.product_attribute_layout:
		case R.id.product_gift_layout:
			AlertDialog congratulateDialog = new AlertDialog.Builder(this)
					.create();
			Window window = congratulateDialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			window.setGravity(Gravity.LEFT | Gravity.TOP);
			lp.x = MyApplication.width - 250; // ��λ��X����
			lp.y = title.getHeight(); // ��λ��Y����
			window.setAttributes(lp);
			congratulateDialog.show();
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.test, null);
			congratulateDialog.getWindow().setContentView(layout);
			congratulateDialog.getWindow().setWindowAnimations(
					R.style.dialogWindowAnim);
			// СͼƬ
			NetworkImageView img = (NetworkImageView) layout
					.findViewById(R.id.attribute_img);
			MyApplication.client.getImageForNetImageView(product.getImgPath(),
					img, R.drawable.ic_launcher);
			// �۸�
			priceTxt = (TextView) layout.findViewById(R.id.attribute_price);
			priceTxt.setText("��" + product.getAttribute().getPrice());
			// ����
			TextView codeTxt = (TextView) layout
					.findViewById(R.id.attribute_code);
			codeTxt.setText("��Ʒ��ţ�" + product.getId());
			giftAttributeTxt = (TextView) layout.findViewById(R.id.gift_txt);
			if (v.getId() == R.id.product_attribute_layout) {
				giftAttributeTxt.setText("���");
				// ���Լ���
				listView = (ListView) layout
						.findViewById(R.id.attribute_listview);
				ArrayList<Object> data = product.getAttributes();
				adapter = new MyAdapter(this, NetworkAction.��Ʒ����, data);
				// ��ʾ����ģ��
				LinearLayout numLayout = (LinearLayout) layout
						.findViewById(R.id.product_num_layout);
				View line = (View) layout.findViewById(R.id.product_num_line);
				numLayout.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
				attributeNumTxt = (TextView) layout
						.findViewById(R.id.product_num);
				attributeNumTxt.setText(product.getNum());
				ImageView jian = (ImageView) layout
						.findViewById(R.id.product_jian);
				ImageView jia = (ImageView) layout
						.findViewById(R.id.product_jia);
				jian.setOnClickListener(this);
				jia.setOnClickListener(this);
			} else if (v.getId() == R.id.product_gift_layout) {
				giftAttributeTxt.setText("��Ʒ");
				listView = (ListView) layout
						.findViewById(R.id.attribute_listview);
				ArrayList<Object> data = product.getGifts();
				adapter = new MyAdapter(this, NetworkAction.��Ʒ, data);
			}
			listView.setDivider(null);
			listView.setAdapter(adapter);
			Button buyBtn = (Button) layout.findViewById(R.id.product_buynow);
			Button addBtn = (Button) layout
					.findViewById(R.id.product_add_shopcart);
			buyBtn.setOnClickListener(this);
			addBtn.setOnClickListener(this);
			break;
		case R.id.product_jian:// ���ҳ��ļ��ŵĵ���¼�
			int numjian = Integer.valueOf(attributeNumTxt.getText().toString());
			if (numjian > 1) {
				numjian--;
				attributeNumTxt.setText(String.valueOf(numjian));
				numTxt.setText(String.valueOf(numjian) + "��");
				product.setNum(String.valueOf(numjian));
			}
			break;
		case R.id.product_jia:// ���ҳ��ļӺŵĵ���¼�
			int numjia = Integer.valueOf(attributeNumTxt.getText().toString());
			numjia++;
			if (numjia <= Integer.valueOf(product.getInventory())) {
				attributeNumTxt.setText(String.valueOf(numjia));
				numTxt.setText(String.valueOf(numjia) + "��");
				product.setNum(String.valueOf(numjia));
			} else
				Toast.makeText(this, "�޷�����ӣ�û�п���ˣ�", 2000).show();
			break;
		}

	}

	// ����������ͻ����¼�������
	private class TouchListenerImpl implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			int count=0;
			int scrollY = view.getScrollY();
			int height = view.getHeight();
			int scrollViewMeasuredHeight = scrollView.getChildAt(0)
					.getMeasuredHeight();
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if((scrollY + height) == scrollViewMeasuredHeight)
					detailModule=true;
				else
					detailModule=false;
//				Toast.makeText(ProductDetail.this, detailModule+"", 2000).show();
				break;
			case MotionEvent.ACTION_UP:

				break;
			case MotionEvent.ACTION_MOVE:

				if(detailModule && detailFinished)
					webView.setVisibility(View.VISIBLE);
				else if(detailModule&& !detailFinished && count==0)
				{
					Toast.makeText(ProductDetail.this, "ͼ�����黹δ������ϣ����Ժ�", 2000).show();
					count++;
				}
				
//				// ������������˵��¼�
//				if (scrollY == 0) {
//
//				}
//				Log.i(MyApplication.TAG, " height->" + height);
//				Log.i(MyApplication.TAG, "scrollY ->" + scrollY);
//				Log.i(MyApplication.TAG, "scrollViewMeasuredHeight->"
//						+ scrollViewMeasuredHeight);
//				// ����������ײ����¼�
//				if ((scrollY + height) > scrollViewMeasuredHeight) {
//					Toast.makeText(ProductDetail.this, "�����׶���", 2000).show();
//				}
				break;

			default:
				break;
			}
			return false;
		}

	};

	// ����ͼƬ�ķ���
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (photopage <= 1)
			return false;

		if (e1.getX() > e2.getX()) {
			flipper.setInAnimation(ProductDetail.this,
					R.anim.view_in_from_right);
			flipper.setOutAnimation(ProductDetail.this, R.anim.view_out_to_left);
			flipper.showNext();
		} else {

			flipper.setInAnimation(ProductDetail.this, R.anim.view_in_from_left);
			flipper.setOutAnimation(ProductDetail.this,
					R.anim.view_out_to_right);
			flipper.showPrevious();
		}

		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
