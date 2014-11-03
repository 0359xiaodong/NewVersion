package com.test.product;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.test.model.Product;
import com.test.base.Url;
import com.test.R;
import com.test.base.MenuActivity;
import com.test.base.MyAdapter;
import com.test.base.MyApplication;
import com.test.base.MyGridView;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.model.Category;
import com.test.utils.ConnectServer;
import com.test.utils.NetworkAction;

//��Ʒ�б�
public class ProductShow extends NormalActivity implements OnItemClickListener,
		OnClickListener, OnCheckedChangeListener {

	private Title title;// ���ñ�����
	private ArrayList<Object> products;
	private MyAdapter adapter;
	private MyGridView gridView;
	private HashMap<String, String> paramter;// ��ȡ������Ʒ���������
	private int page = 1; // ��Ҫ����鿴�����ݵ�ҳ��
	private String pageSize = "10";
	// private String sortType = "0"; // ����ʽ 0������1�۸�����2�۸���3������4�ϼ�ʱ�䣬5����
	private int totalpage = 0;
	private LinearLayout getMore;// ��ȡ�������ݵİ�ť
	private RadioGroup radioGroup;
	private RadioButton showSort;// �ۺ�����
	private RadioButton popSort;// �ۺ����򵯳���ѡ��
	private RadioButton popLow;// �ۺ����򵯳���ѡ��
	private RadioButton popHigh;// �ۺ����򵯳���ѡ��
	private RadioButton showSaleFirst;// ��������
	private RadioButton showFilter;// ɸѡ
	private EditText startPrice;// ɸѡ��ֹ�۸�
	private EditText endPrice;// ɸѡ��ֹ�۸�
	private String start_price="0";
	private String end_price="0";
	private PopupWindow sortPopWindow; // �ۺ�����PopupWindow
	private PopupWindow filterPopWindow; // ɸѡPopupWindow
	private int sortTypeTemp = 0;
	private NetworkAction request;
	private String Category_id;
	private String CacheID;
	private String url;
	private String searchTxt;//�����ؼ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_show);
		initView();
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(5);
		gridView = (MyGridView) findViewById(R.id.product_gridView);
		getMore = (LinearLayout) findViewById(R.id.getMore);
		getMore.setOnClickListener(this);
		radioGroup = (RadioGroup) findViewById(R.id.show_group);
		showSort = (RadioButton) findViewById(R.id.show_sort);
		showSort.setOnClickListener(this);
		showSaleFirst = (RadioButton) findViewById(R.id.show_salefirst);
		showFilter = (RadioButton) findViewById(R.id.show_filter);
		showFilter.setOnClickListener(this);
		showSort.setOnCheckedChangeListener(this);
		showSaleFirst.setOnCheckedChangeListener(this);
		showFilter.setOnCheckedChangeListener(this);

	
	}

	private void initData() {
		// �ӷ����б���ת���������
		if(MyApplication.searchModule==1)
			catagorySearch();
		products = new ArrayList<Object>();
		adapter = new MyAdapter(this, request, products);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		
	}

	// �ӷ����б���ת���������
	public void catagorySearch()
	{
		Intent intent = getIntent();
		Category_id = intent.getStringExtra("Category_id");
		CacheID = intent.getStringExtra("CacheID");
		request=NetworkAction.��ȡ������Ʒ;
		url=Url.URL_INDEX;
		paramter = new HashMap<String, String>();
		paramter.put("act", "product");
		paramter.put("sid", MyApplication.sid);
		paramter.put("store_id", MyApplication.sid);
		paramter.put("nowpage", String.valueOf(page));
		paramter.put("pagesize", pageSize);
		paramter.put("keyname", Category_id);
		paramter.put("CacheID", CacheID);
		paramter.put("keyname1", "0");
		paramter.put("CacheID1", "0");
		paramter.put("brans", "0");
		paramter.put("clears", "0");
		paramter.put("start_price",start_price);
		paramter.put("end_price",end_price);
		paramter.put("sort_type", "0");
		paramter.put("cates", "0");
		showSaleFirst.setChecked(true);
	}
	
	
	public void stringSearch()
	{
		Intent intent = getIntent();
		searchTxt = intent.getStringExtra("searchTxt");
	}
	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		if (request.equals(NetworkAction.��ȡ������Ʒ)) {
			page++;
			totalpage = response.getInt("totalpage");

			JSONArray productsArray = response.getJSONArray("list");
			// �ж��Ƿ�Ҫ��������
			if (page > totalpage)
				getMore.setVisibility(View.GONE);
			// �ж��Ƿ�����Ʒ����
			if (productsArray.length() == 0)
				Toast.makeText(ProductShow.this,
						"�÷��໹û����Ʒ" + productsArray.length(), 2000).show();
			else {
				for (int i = 0; i < productsArray.length(); i++) {

					JSONObject product = productsArray.getJSONObject(i);
					Product newProduct = new Product();
					newProduct.setId(product.getString("product_id"));
					newProduct.setName(product.getString("product_name"));
					newProduct.setStorePrice(product.getString("store_price"));
					newProduct.setImgPath(product.getString("product_photo"));
					products.add(newProduct);
				}
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long parentId) {
		// Intent intent = new Intent().setClass(this, ProductListShow.class);
		// intent.putExtra("Category_id",
		// ((Category) listThird.get(position)).getCategory_id());
		// intent.putExtra("CacheID",
		// ((Category) listThird.get(position)).getCacheID());
		//
		// startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getMore:
			paramter.remove(paramter.get("nowpage"));
			paramter.put("nowpage", String.valueOf(page));
			ConnectServer.getResualt(this, paramter,
					request,url);
			break;
		case R.id.show_sort:
			initSortPop(radioGroup);
			break;
		case R.id.show_filter:
			initFilterPop(radioGroup);
			// initSortPop(radioGroup);
			break;
		}

	}

	// ��ѡ���ѡ��״̬�ĸ����¼�
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			Drawable sortSelect = MyApplication.resources
					.getDrawable(R.drawable.sort_ico_select);
			sortSelect.setBounds(0, 0, sortSelect.getMinimumWidth(),
					sortSelect.getMinimumHeight());
			Drawable filterSelect = MyApplication.resources
					.getDrawable(R.drawable.filter_ico_select);
			filterSelect.setBounds(0, 0, filterSelect.getMinimumWidth(),
					filterSelect.getMinimumHeight());
			// �ۺ�����ѡ��ʱ���ұߵĶԺŵ�ͼƬ
			Drawable sortSelectBg = MyApplication.resources
					.getDrawable(R.drawable.sort_select_bg);
			sortSelectBg.setBounds(0, 0, sortSelectBg.getMinimumWidth(),
					sortSelectBg.getMinimumHeight());
			buttonView.setTextColor(MyApplication.resources
					.getColor(R.color.style_color));
			switch (buttonView.getId()) {
			case R.id.show_sort:
				buttonView.setCompoundDrawables(null, null, sortSelect, null); // ������ͼ��
				// initSortPop(radioGroup);
				break;
			case R.id.show_salefirst:
				resetData(0);
				ConnectServer.getResualt(this, paramter,
						NetworkAction.��ȡ������Ʒ, Url.URL_INDEX);
				break;
			case R.id.show_filter:
				buttonView.setCompoundDrawables(null, null, filterSelect, null); // ������ͼ��
				break;

			case R.id.show_pop_sort:
				buttonView.setCompoundDrawables(null, null, sortSelectBg, null); // ������ͼ��
				showSort.setText(buttonView.getText().toString());
				// ����ۺ�����Ĵ���
				resetData(3);
				ConnectServer.getResualt(this, paramter,
						request, url);
				sortPopWindow.dismiss();
				break;
			case R.id.show_pop_low:
				buttonView.setCompoundDrawables(null, null, sortSelectBg, null); // ������ͼ��
				showSort.setText(buttonView.getText().toString());
				// ��Ӽ۸�ӵ͵��ߵĴ���
				resetData(1);
				ConnectServer.getResualt(this, paramter,
						request, url);
				sortPopWindow.dismiss();
				break;
			case R.id.show_pop_high:
				buttonView.setCompoundDrawables(null, null, sortSelectBg, null); // ������ͼ��
				showSort.setText(buttonView.getText().toString());
				// ��Ӽ۸�Ӹߵ��׵Ĵ���
				resetData(2);
				ConnectServer.getResualt(this, paramter,
						request, url);
				sortPopWindow.dismiss();
				break;
			}
		} else {
			Drawable sortNoSelect = MyApplication.resources
					.getDrawable(R.drawable.sort_ico_noselect);
			sortNoSelect.setBounds(0, 0, sortNoSelect.getMinimumWidth(),
					sortNoSelect.getMinimumHeight());

			Drawable filterNoSelect = MyApplication.resources
					.getDrawable(R.drawable.filter_ico_noselect);
			filterNoSelect.setBounds(0, 0, filterNoSelect.getMinimumWidth(),
					filterNoSelect.getMinimumHeight());

			buttonView.setTextColor(MyApplication.resources
					.getColor(R.color.menu_noselect));
			switch (buttonView.getId()) {
			case R.id.show_sort:
				buttonView.setCompoundDrawables(null, null, sortNoSelect, null); // ������ͼ��
				break;
			case R.id.show_filter:
				buttonView.setCompoundDrawables(null, null, filterNoSelect,
						null); // ������ͼ��
				break;
			case R.id.show_pop_sort:
				buttonView.setCompoundDrawables(null, null, null, null); // ������ͼ��
				break;
			case R.id.show_pop_low:
				buttonView.setCompoundDrawables(null, null, null, null); // ������ͼ��
				break;
			case R.id.show_pop_high:
				buttonView.setCompoundDrawables(null, null, null, null); // ������ͼ��
				break;
			}
		}

	}

	// ��ʼ���ۺ����򵯳��˵�
	private void initSortPop(View parent) {
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popuView = layoutInflater.inflate(R.layout.pop_sort, null);
		sortPopWindow = new PopupWindow(popuView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popSort = (RadioButton) popuView.findViewById(R.id.show_pop_sort);
		popLow = (RadioButton) popuView.findViewById(R.id.show_pop_low);
		popHigh = (RadioButton) popuView.findViewById(R.id.show_pop_high);
		popSort.setOnCheckedChangeListener(this);
		popLow.setOnCheckedChangeListener(this);
		popHigh.setOnCheckedChangeListener(this);
		if (showSort.getText().toString().equals(popSort.getText().toString()))
			popSort.setChecked(true);
		else if (showSort.getText().toString()
				.equals(popLow.getText().toString()))
			popLow.setChecked(true);
		else if (showSort.getText().toString()
				.equals(popHigh.getText().toString()))
			popHigh.setChecked(true);
		// ����������������������ط��õ�������ʧ
		ColorDrawable cd = new ColorDrawable(-0000);
		sortPopWindow.setBackgroundDrawable(cd);
		int height = title.getHeight() + radioGroup.getHeight();
		sortPopWindow.setFocusable(true);
		sortPopWindow.setOutsideTouchable(true);
		sortPopWindow.showAtLocation(parent, Gravity.TOP, 10, height + 50);
		sortPopWindow.update();
	}

	// ��ʼ��ɸѡ�����˵�
	private void initFilterPop(View parent) {
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popuView = layoutInflater.inflate(R.layout.pop_filter, null);
		filterPopWindow = new PopupWindow(popuView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		startPrice = (EditText) popuView.findViewById(R.id.filter_startprice);
		endPrice = (EditText) popuView.findViewById(R.id.filter_endprice);

		// ����������������������ط��õ�������ʧ
		ColorDrawable cd = new ColorDrawable(-0000);
		filterPopWindow.setBackgroundDrawable(cd);
		int height = title.getHeight() + radioGroup.getHeight();
		filterPopWindow.setFocusable(true);
		filterPopWindow.setOutsideTouchable(true);
		filterPopWindow.showAtLocation(parent, Gravity.TOP, 0, height + 47);
		filterPopWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				start_price=startPrice.getText().toString();
				end_price=endPrice.getText().toString();
				if (!start_price.equals("")
						&& !end_price.equals("")) {
					resetData(sortTypeTemp);
					paramter.put("start_price", start_price);
					paramter.put("end_price", end_price);
					ConnectServer.getResualt(ProductShow.this,
							paramter, request, url);
				}

			}
		});
		filterPopWindow.update();

	}

	// ��������Դ��ʱ���������ݣ���������ʽ����
	public void resetData(int sortType) {
		sortTypeTemp = sortType;
		products.clear();// �������
		page = 1;// ����ҳ��
		getMore.setVisibility(View.VISIBLE);// ��ԭ���ఴť����ʾ
		paramter.put("nowpage", String.valueOf(page));
		paramter.put("sort_type", String.valueOf(sortType));
		paramter.put("start_price",start_price);
		paramter.put("end_price", end_price);
	}
}
