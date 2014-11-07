package com.test.product;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.test.base.ChangeTime;
import com.test.model.Product;
import com.test.base.MyAdapter;
import com.test.base.MyGridView;
import com.test.base.MyApplication;
import com.test.base.MyViewFlipper;
import com.test.base.Url;
import com.test.R;
import com.test.base.MenuActivity;
import com.test.base.Title;
import com.test.utils.ConnectServer;
import com.test.utils.ErrorMsg;
import com.test.utils.NetworkAction;

public class Home extends MenuActivity implements OnGestureListener,
		OnItemClickListener, OnClickListener {

	private Title title;// ���ñ�����

	private MyViewFlipper flipper;// �������
	private boolean getHeight = false;// �Ƿ��л�ȡ�����ĸ߶�
	private GestureDetector gesture;
	private RadioGroup flipperTxt;// �����ҳ���ͼƬ���½Ǹ����ƶ��ĵ�ѡ������
	private int imgLocation = 0;// ��¼��ҳ���ͼƬλ��

	private LinearLayout hotMoreBtn; // ������Ʒ���ఴť
	private LinearLayout secKillMoreBtn; // ��ɱ��Ʒ���ఴť
	private MyGridView hotGridView;// ������Ʒ
	private MyGridView secKillGridView;// ������Ʒ
	private ArrayList<Object> hotProduct; // ���ݼ���
	private ArrayList<Object> secKillProduct; // ���ݼ���
	private MyAdapter adapterHot; // ������Ʒ������
	private MyAdapter adapterSecKill; // ��ɱ��Ʒ������
	private ScrollView srollView; // ������
	private FrameLayout hotTopModule;// ������Ʒ������ʾ������ı�������
	private FrameLayout secKillTopModule;// ��ɱ��Ʒ������ʾ������ı�������
	private LinearLayout secKillLayout;// ��ɱģ������
	private LinearLayout hotLayout;// ������Ʒģ������
	private boolean hotModule = false;// ������Ʒģ���ʾ
	private boolean secKillModule = false;// ��ɱ��Ʒģ���ʾ
	public static Handler homeHandler;
	private int newHeight = 0;
	private boolean load;
	private int page = 1; // ��ǰҳ��
	private String pageSize = "4"; // ÿҳ��ʾ����������
	private int totalPage = 0; // ��ҳ��
	public ChangeTime changeTime;

	HashMap<String, String> paramter;
	HashMap<String, String> paramterHot;
	HashMap<String, String> paramterSeckill;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		// ��������ʱ����
		startChangeTime();
		initView();
		initData();
	}

	// ��������ʱ����
	private void startChangeTime() {
		// ����˳������Ժ�ñ�ʶ��Ϊtrue���ٴν����ʱ����Ҫ���øñ�ʶ���ſ���������ʱ����
		if (!ChangeTime.exit) {
			ChangeTime.exit = true;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ���������ͼƬ�뿪app�Ժ󷵻ظ�ҳ��������Ź��ͼƬ
		// flipper.startFlipping();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// ��ҳ�汻�����Ժ���ˢ����ɱ����ʱ���߳�Ҳ����֮����
		ChangeTime.exit = false;
	}

	private void initView() {
		changeTime = new ChangeTime();
		Thread thread = new Thread(changeTime);
		thread.start();
		flipper = (MyViewFlipper) findViewById(R.id.home_viewFlipper);
		flipperTxt = (RadioGroup) findViewById(R.id.home_txt_layout);
		title = (Title) findViewById(R.id.title);
		title.setModule(1);
		gesture = new GestureDetector(this);
		flipper.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gesture.onTouchEvent(event);
			}

		});
		secKillGridView = (MyGridView) findViewById(R.id.home_seckill_gridview);
		hotGridView = (MyGridView) findViewById(R.id.home_hot_gridview);
		hotGridView.setOnItemClickListener(this);
		secKillGridView.setOnItemClickListener(this);
		secKillLayout = (LinearLayout) findViewById(R.id.home_seckill_layout);// ��ɱģ��
		hotLayout = (LinearLayout) findViewById(R.id.home_hot_layout);// ������Ʒģ��
		hotMoreBtn = (LinearLayout) findViewById(R.id.home_hot_more_btn);// ������Ʒ���ఴť
		hotMoreBtn.setOnClickListener(this);
		secKillMoreBtn = (LinearLayout) findViewById(R.id.home_seckill_more_btn); // ��ɱ��Ʒ���ఴť
		secKillMoreBtn.setOnClickListener(this);
		srollView = (ScrollView) findViewById(R.id.home_scroll);
		// srollView.setOnTouchListener(new TouchListenerImpl());
		hotProduct = new ArrayList<Object>();
		secKillProduct = new ArrayList<Object>();
		adapterHot = new MyAdapter(this, NetworkAction.������Ʒ, hotProduct);
		adapterSecKill = new MyAdapter(this, NetworkAction.��ɱ��Ʒ, secKillProduct);
		hotGridView.setAdapter(adapterHot);
		secKillGridView.setAdapter(adapterSecKill);

		// ���ù�������ʼλ��
//		 srollView.smoothScrollTo(0, 0);
	}

	private void initData() {
		// ÿ��ˢ����ɱ��Ʒʱ���handler
		homeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				// ȡ�����µ�ʱ��
				long time = bundle.getLong("time");
				// ȡ�����������ݸ�������ѯ��Ӧ��ʱ���textview
				int index = bundle.getInt("index");
//				Log.i("test", ""+index);
				// �����̷߳��صļ�����˵��µ�ʱ���ַ���
				String timeString = bundle.getString("timeString");
				TextView txt = (TextView) ((View) ChangeTime.txtViewList.get(
						index).getTag())
						.findViewById(R.id.home_seckill_outtime);
				// �����ɱ��û�н����Ļ���ˢ�����µ�ʱ�䣬�������»�ȡһ����ɱ��Ʒ
				// if(time>0)
				// ChangeTime.txtViewList.get(index).setText(timeString);
				if (time > 0)
					txt.setText(timeString);
				else {
					ChangeTime.txtViewList.clear();
					ChangeTime.timeList.clear();
					secKillProduct.clear();
					ConnectServer.getResualt(Home.this, paramterSeckill,
							NetworkAction.��ɱ��Ʒ, Url.URL_SECKILL);
				}
			}
		};
		// ��ҳ���
		paramter = new HashMap<String, String>();
		paramter.put("act", "img1");
		paramter.put("nowpage", "1");
		paramter.put("pagesize", "100");
		paramter.put("sid", MyApplication.sid);
		ConnectServer.getResualt(this, paramter, NetworkAction.��ҳ���,
				Url.URL_INDEX);
		// ������Ʒ
		paramterHot = new HashMap<String, String>();
		paramterHot.put("act", "hotsale");
		paramterHot.put("CacheID", "");
		paramterHot.put("CacheID1", "0");
		paramterHot.put("brans", "0");
		paramterHot.put("cates", "0");
		paramterHot.put("clears", "0");
		paramterHot.put("keyname", "");
		paramterHot.put("keyname1", "");
		paramterHot.put("nowpage", String.valueOf(page));
		paramterHot.put("pagesize", pageSize);
		paramterHot.put("sort_type", "0");
		paramterHot.put("sid", MyApplication.sid);
		ConnectServer.getResualt(this, paramterHot, NetworkAction.������Ʒ,
				Url.URL_HOTSALE);
		// ��ɱ��Ʒ
		paramterSeckill = new HashMap<String, String>();
		paramterSeckill.put("act", "list");
		paramterSeckill.put("sid", MyApplication.sid);
		paramterSeckill.put("nowpage", String.valueOf(page));
		paramterSeckill.put("pagesize", pageSize);
		ConnectServer.getResualt(this, paramterSeckill, NetworkAction.��ɱ��Ʒ,
				Url.URL_SECKILL);
	}

	// ���������ݽ��
	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		// Log.i(MyApplication.TAG, "showResualt-->"+response.toString());
		if (request.equals(NetworkAction.��ҳ���)) {
			final JSONArray lists = response.getJSONArray("list");
			Log.i(MyApplication.TAG, "lists.length()-->" + lists.length());
			// ���û����ҳͼƬֱ�ӷ���
			if (lists.length() == 0)
				return;
			// �������ҳ���ͼƬ����û�л�ȡ��ʵ������ͼƬ�Ŀ�߱�����ʱ���Ȼ�ȡ����Ը߶�
			if (lists.length() > 0 && !getHeight) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						JSONObject item;
						try {
							item = lists.getJSONObject(0);
							String path = Url.URL_IMGPATH
									+ item.getString("attachments_path");
							URL url = new URL(path);
							String responseCode = url.openConnection()
									.getHeaderField(0);
							Bitmap map = BitmapFactory.decodeStream(url
									.openStream());
							int height = map.getHeight();
							int width = map.getWidth();
							 Log.i(MyApplication.TAG, "height-->"+height);
							newHeight = (int) (MyApplication.width * ((double) height / width));
							getHeight = true;
							 Log.i(MyApplication.TAG, "newHeight-->"+newHeight);
							ConnectServer.getResualt(Home.this, paramter,
									NetworkAction.��ҳ���, Url.URL_INDEX);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();
				return;
			}

			// �������ҳͼƬ��ѭ����ʾ
			for (int i = 0; i < lists.length(); i++) {
				JSONObject item = lists.getJSONObject(i);
				NetworkImageView netView = new NetworkImageView(Home.this);
				String path = Url.URL_IMGPATH
						+ item.getString("attachments_path");
				Log.i(MyApplication.TAG, "path-->" + path);
				netView.setAdjustViewBounds(false);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, newHeight);
				netView.setLayoutParams(layoutParams);
				MyApplication.client.getImageForNetImageView(path, netView,
						R.drawable.ic_launcher);
				flipper.addView(netView);
				flipper.setInAnimation(Home.this, R.anim.view_in_from_right);
				flipper.setOutAnimation(Home.this, R.anim.view_out_to_left);
				RadioGroup.LayoutParams rbParams = new RadioGroup.LayoutParams(
						20, 5);
				rbParams.setMargins(0, 0, 10, 0);
				// ���ͼƬ����������1�ſ�ʼѭ������
				RadioButton rb = new RadioButton(flipperTxt.getContext());
				rb.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.home_img_bg));
				rb.setId(i);
				// ����͸��ɫ��ȥ��radiobuttonԭ����button��ť
				rb.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
				if (i == 0)
					rb.setChecked(true);
				netView.setTag(rb);
				flipperTxt.addView(rb, rbParams);
			}
			if (lists.length() > 1) {
				flipper.setFlipInterval(3000);
				flipper.startFlipping();
			}
		} else if (request.equals(NetworkAction.������Ʒ)) {
			page++;// ��ǰҳ��һ
			totalPage = Integer.valueOf(response.getString("totalpage"));// ��ȡ��ҳ��

			// �ж��Ƿ�Ҫ��������
			if (page > totalPage)
				load = false;
			else
				load = true;
			JSONArray lists = response.getJSONArray("list");// ��ȡ���ݼ�
			for (int i = 0; i < lists.length(); i++) {
				JSONObject product = lists.getJSONObject(i);
				Product newProduct = new Product();
				newProduct.setId(product.getString("product_id"));
				newProduct.setName(product.getString("product_name"));
				newProduct.setStorePrice(product.getString("store_price"));
				newProduct.setImgPath(product.getString("product_photo"));
				hotProduct.add(newProduct);// �»�ȡ����������ӵ����ݼ�����
			}
			adapterHot.notifyDataSetChanged();// ֪ͨ���������ݷ����仯��
		} else if (request.equals(NetworkAction.��ɱ��Ʒ)) {// ��ȡ��ɱ��Ʒ
			secKillProduct.clear();
			page++;// ��ǰҳ��һ
			totalPage = Integer.valueOf(response.getString("totalpage"));// ��ȡ��ҳ��

			// �ж��Ƿ�Ҫ��������
			if (page > totalPage)
				load = false;
			else
				load = true;
			String time = response.getString("time");
			JSONArray lists = response.getJSONArray("list");// ��ȡ���ݼ�
			for (int i = 0; i < lists.length(); i++) {
				JSONObject product = lists.getJSONObject(i);
				Product newProduct = new Product();
				newProduct.setSkID(product.getString("SKID"));
				newProduct.setId(product.getString("ProductID"));
				newProduct.setName(product.getString("SKName"));
				newProduct.setSKPrice(product.getString("SKPrice"));
				newProduct.setOutEndTime(product.getString("OutEndTime"));
				newProduct.setImgPath(product.getString("Images"));
				newProduct.setTime(time);
				secKillProduct.add(newProduct);// �»�ȡ����������ӵ����ݼ�����
			}
			adapterSecKill.notifyDataSetChanged();// ֪ͨ���������ݷ����仯��

		}
	}

	// ����������ͻ����¼�������
	// private class TouchListenerImpl implements OnTouchListener {
	// @Override
	// public boolean onTouch(View view, MotionEvent motionEvent) {
	// switch (motionEvent.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	//
	// break;
	// case MotionEvent.ACTION_MOVE:
	// int scrollY = view.getScrollY();
	// int height = view.getHeight();
	// int scrollViewMeasuredHeight = srollView.getChildAt(0)
	// .getMeasuredHeight();
	// // ������������˵��¼�
	// if (scrollY == 0) {
	//
	// }
	// // ����������ײ����¼�
	// if ((scrollY + height) == scrollViewMeasuredHeight) {
	// // �������Ҫ�������ݵĻ����ղ�ͬ��ģ���ȡ��ͬ������
	// if (load) {
	// load = false;
	//
	// // ������Ʒ��ʾģʽ
	// if (hotModule) {
	// // MyApplication.progressShow(Home.this, "����");
	// // sendDataToServer(NetworkAction.������Ʒ);// ��ȡ������Ʒ
	// } else if (secKillModule) {
	// MyApplication.progressShow(Home.this, "����");
	// // sendDataToServer(NetworkAction.��ɱ��Ʒ);// ��ȡ��ɱ��Ʒ
	// }
	// }
	//
	// }
	// break;
	//
	// default:
	// break;
	// }
	// return false;
	// }
	//
	// };
	@Override
	public boolean onDown(MotionEvent e) {
		Log.i("test", "onDown");
		flipper.stopFlipping();
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// ((RadioButton)flipper.getCurrentView().getTag()).setChecked(true);
		if (e1.getX() > e2.getX()) {
			flipper.setInAnimation(Home.this, R.anim.view_in_from_right);
			flipper.setOutAnimation(Home.this, R.anim.view_out_to_left);
			flipper.showNext();
		} else {

			flipper.setInAnimation(Home.this, R.anim.view_in_from_left);
			flipper.setOutAnimation(Home.this, R.anim.view_out_to_right);
			flipper.showPrevious();
		}

		flipper.startFlipping();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Toast.makeText(this, "������ͼ", 2000).show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long productId) {

		Intent intent = new Intent().setClass(this, ProductDetail.class);
		
		if (parent.getId() == R.id.home_seckill_gridview) {
			Product product = (Product) secKillProduct.get(position);
			intent.putExtra("productId",product.getId());
			intent.putExtra("skid",product.getSkID());
//			Log.i(MyApplication.TAG,"productId-->"+product.getId()+"  skid-->"+product.getSkID());
		}
		else if(parent.getId() == R.id.home_hot_gridview)
		{
			intent.putExtra("productId", String.valueOf(productId));
		}
//		Toast.makeText(this, String.valueOf(productId), 2000).show();
		 startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		switch (v.getId()) {
		// ��ɱ���ఴť
		case R.id.home_seckill_more_btn:
			intent.setClass(this, SeckillProduct.class);
			MyApplication.seckillModule=true;
			break;
		// ������Ʒ���ఴť
		case R.id.home_hot_more_btn:
			intent.setClass(this, HotProduct.class);
			break;
		}

		startActivity(intent);
	}

}
