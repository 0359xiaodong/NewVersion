package com.test.product;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.R;
import com.test.base.ChangeTime;
import com.test.base.MyAdapter;
import com.test.base.MyApplication;
import com.test.base.MyGridView;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.base.Url;
import com.test.model.Product;
import com.test.utils.ConnectServer;
import com.test.utils.NetworkAction;

public class HotProduct extends NormalActivity implements OnClickListener{

	private Title title;// ���ñ�����
	private MyGridView gridView;
	private ArrayList<Object> hotProduct; // ���ݼ���
	private MyAdapter adapterHot; // ��ɱ��Ʒ������
	HashMap<String, String> paramterHot;
	private int newHeight = 0;
	private boolean load;
	private int page = 1; // ��ǰҳ��
	private String pageSize = "10"; // ÿҳ��ʾ����������
	private int totalPage = 0; // ��ҳ��
	private ImageView getMore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hot);
		initView();
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		gridView = (MyGridView) findViewById(R.id.hot_gridview);
		hotProduct = new ArrayList<Object>();
		adapterHot = new MyAdapter(this, NetworkAction.������Ʒ, hotProduct);
		paramterHot = new HashMap<String, String>();
		gridView.setAdapter(adapterHot);
		getMore= (ImageView) findViewById(R.id.getMore);
		getMore.setOnClickListener(this);
	}

	private void initData() {
		title.setModule(2);
		title.setTitleTxt("������Ʒ");
		// ������Ʒ
		paramterHot.put("act", "hotsale");
		paramterHot.put("CacheID", "");
		paramterHot.put("CacheID1", "0");
		paramterHot.put("brans", "0");
		paramterHot.put("cates", "0");
		paramterHot.put("clears", "0");
		paramterHot.put("keyname", "");
		paramterHot.put("keyname1", "");
		
		paramterHot.put("pagesize", pageSize);
		paramterHot.put("sort_type", "0");
		paramterHot.put("sid", MyApplication.sid);
		ConnectServer.getResualt(this, paramterHot, NetworkAction.������Ʒ,
				Url.URL_HOTSALE);
	}

	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		Log.i("test", "showResualt");
		page++;// ��ǰҳ��һ
		totalPage = Integer.valueOf(response.getString("totalpage"));// ��ȡ��ҳ��

		// �ж��Ƿ�Ҫ��������
		if (page > totalPage)
			getMore.setVisibility(View.GONE);
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
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getMore:
			paramterHot.remove(paramterHot.get("nowpage"));
			paramterHot.put("nowpage", String.valueOf(page));
			ConnectServer.getResualt(this, paramterHot, NetworkAction.������Ʒ,
					Url.URL_HOTSALE);
			break;

		}
		
	}

}
