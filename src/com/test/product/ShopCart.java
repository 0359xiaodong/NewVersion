package com.test.product;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.MenuBottom;
import com.test.person.Login;
import com.test.base.CustomDialog;
import com.test.base.MyAdapter;
import com.test.base.MyApplication;
import com.test.base.ShopCartManager;
import com.test.base.Title;
import com.test.utils.NetworkAction;
import com.test.model.Product;
import com.test.R;
import com.test.MainActivity;
import com.test.base.MenuActivity;

public class ShopCart extends MenuActivity implements OnCheckedChangeListener,
		OnClickListener {

	private Title title;// ���ñ�����
	private LinearLayout emptyLayout;// �չ��ﳵģ��
	private LinearLayout unemptyLayout;// �ǿչ��ﳵģ��
	private CheckBox selectAll;// ȫѡ��ť
	private Button viewBtn;// ȥ��䰴ť
	private TextView totalPriceTxt;// С�Ƽ۸�
	private MyAdapter adapter;
	public ListView listView;
	private boolean deleteAll = false;
	private Button submit;
	private ArrayList<Object> tempSubmitProduct;
	private LinearLayout cartTotalLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopcart);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(MyApplication.TAG, "onResume-->shopcart->"
				+ MyApplication.shopCartList.size());
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(8);
		title.setTitleTxt("�ҵĹ��ﳵ");
		title.setRightLayoutClick(this);
//		Toast.makeText(this, "isEditNum-->"+((Product) (MyApplication.shopCartList.get(0))).isEditNum(), 2000).show();
		if (MyApplication.shopCartList.size() > 0
				&& ((Product) (MyApplication.shopCartList.get(0))).isEditNum())
			title.setRightTxt("���");
		else
			title.setRightTxt("�༭");
		tempSubmitProduct = new ArrayList<Object>();
		emptyLayout = (LinearLayout) findViewById(R.id.shopcart_empty_layout);// �չ��ﳵģ��
		unemptyLayout = (LinearLayout) findViewById(R.id.shopcart_unempty_layout);// �ǿչ��ﳵģ��
		totalPriceTxt = (TextView) findViewById(R.id.shopcart_totalprice);
		viewBtn = (Button) findViewById(R.id.shopcart_view_btn);// ȥ��䰴ť
		viewBtn.setOnClickListener(this);
		selectAll = (CheckBox) findViewById(R.id.shopcart_deleteall);// ȫѡ��ť
		selectAll.setOnCheckedChangeListener(this);
		listView = (ListView) findViewById(R.id.shopcart_listview);
		listView.setDivider(null);
		adapter = new MyAdapter(this, NetworkAction.���ﳵ,
				MyApplication.shopCartList);
		listView.setAdapter(adapter);
		submit = (Button) findViewById(R.id.shopcart_pay_btn);// ȥ���㰴ť
		submit.setOnClickListener(this);
		cartTotalLayout=(LinearLayout) findViewById(R.id.cart_total_layout);
	}

	private void initData() {
		// Toast.makeText(this, "num-->"+num, 2000).show();
		// ���ﳵΪ��ʱ�����ʾģʽ
		if (MyApplication.shopCartList.isEmpty()) {
			title.setRightLayoutVisibility(View.GONE);
			emptyLayout.setVisibility(View.VISIBLE);// ��ʾ�չ��ﳵģ��
			unemptyLayout.setVisibility(View.GONE);// ���طǿչ��ﳵģ��
		}
		// ���ﳵ�ǿ�ʱ�����ʾģʽ
		else {
			title.setRightLayoutVisibility(View.VISIBLE);
			emptyLayout.setVisibility(View.GONE);// ���ؿչ��ﳵģ��
			unemptyLayout.setVisibility(View.VISIBLE);// ��ʾ�ǿչ��ﳵģ��
		}

		// ˢ��listview�ĸ߶�
		refreshListViewHeight();
		adapter.notifyDataSetChanged();
		recalculatePrice();
	}

	// ˢ��listview�ĸ߶�
	public void refreshListViewHeight() {
		int totalHeight = 0;
		for (int index = 0, len = adapter.getCount(); index < len; index++) {

			View listViewItem = adapter.getView(index, null, listView);

			// ��������View �Ŀ��

			listViewItem.measure(0, 0);

			// ������������ĸ߶Ⱥ�

			totalHeight += listViewItem.getMeasuredHeight();

		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();

		// listView.getDividerHeight()��ȡ�����ָ����ĸ߶�

		// params.height����ListView��ȫ��ʾ��Ҫ�ĸ߶�

		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));

		listView.setLayoutParams(params);
	}

	// �����ﳵ�е���Ʒ��������ɾ���������ı��ʱ�����¼���С�Ƶ����¼۸�ˢ��
	public void recalculatePrice() {
		if (!MyApplication.shopCartList.isEmpty()) {
			double totalPrice = 0;
			for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
				Product product = (Product) MyApplication.shopCartList.get(i);
				double price = Double.valueOf(product.getStorePrice());
				int num = Integer.valueOf(product.getNum());
				double siglePrice = num * price;
				product.setTotalPrice(String.valueOf(siglePrice));
				totalPrice = totalPrice + siglePrice;
			}
			DecimalFormat df = new DecimalFormat(".00");
			df.format(totalPrice);
			totalPriceTxt.setText(String.valueOf(totalPrice));
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.i(MyApplication.TAG, "size" + MyApplication.shopCartList.size());
		// ����ȫѡ��ť��״̬���޸Ĺ��ﳵ�����е���Ʒ��ѡ��״̬��Ȼ��ˢ��������
		for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
			Product product = (Product) MyApplication.shopCartList.get(i);
			if (product.isChecked() != isChecked)
				product.setChecked(isChecked);
			Log.i(MyApplication.TAG, "isChecked" + product.isChecked());
		}
		adapter.notifyDataSetChanged();
		if (isChecked)
			deleteAll = true;
		else
			deleteAll = false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.special_right_layout:// �����Ұ�ť
			Toast.makeText(this, "���˱༭", 2000).show();
			// ����༭��ť��״̬
			if (title.getRightTxt().equals("�༭")) {
				for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
					Product product = (Product) MyApplication.shopCartList
							.get(i);
					product.setEditNum(true);
				}
				cartTotalLayout.setVisibility(View.INVISIBLE);
				submit.setText("ɾ��");
				title.setRightTxt("���");
				adapter.notifyDataSetChanged();
				return;
			} else if (title.getRightTxt().equals("���")) {
				for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
					Product product = (Product) MyApplication.shopCartList
							.get(i);
					product.setEditFinished(true);
				}
				cartTotalLayout.setVisibility(View.VISIBLE);
				submit.setText("ȥ����");
				title.setRightTxt("�༭");
				adapter.notifyDataSetChanged();
				return;
			}

			break;
		case R.id.shopcart_view_btn:// ȥ��ɱ��ť
			MenuBottom.tabHost.setCurrentTab(0);
			MenuBottom.radioGroup.check(R.id.main_tab_home);
			break;
		case R.id.shopcart_pay_btn:// ȥ���㰴ť
			if (submit.getText().toString().equals("ȥ����")) {
				Intent intent = new Intent();
				if (MyApplication.loginStat) {
					if (!checkProducts()) {
						Toast.makeText(this, "�빴ѡҪ�������Ʒ", 2000).show();
						return;
					}
					intent.setClass(this, SubmitOrder.class);
					intent.putExtra("products", tempSubmitProduct);
					try {
						MyApplication.shopCartManager
								.saveProducts(MyApplication.shopCartList);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					intent.setClass(this, Login.class);
				}
				startActivity(intent);
			} else if (submit.getText().toString().equals("ɾ��")) {
				int count = 0;
				// �ж��Ƿ�ѡ��Ʒ
				for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
					Product product = (Product) MyApplication.shopCartList
							.get(i);
					if (product.isChecked())
						count++;
				}
				if (count == 0)
					Toast.makeText(this, "��ѡ��ѡ��Ҫɾ������Ʒ��", 2000).show();
				else {
					CustomDialog.Builder builder = new CustomDialog.Builder(
							this);
					builder.setMessage("��ȷ��Ҫɾ����Щ��Ʒ��")
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											if (deleteAll) { // ���ȫ��ɾ���Ļ�ֱ�����
												MyApplication.shopCartList
														.clear();
												totalPriceTxt.setText("��0.00");
											} else {
												// ����й�ѡ����Ʒ����û�е��ȫѡ��ť�Ļ���ѭ��ɾ��
												for (int i = 0; i < MyApplication.shopCartList
														.size(); i++) {
													Product product = (Product) MyApplication.shopCartList
															.get(i);
													if (product.isChecked())
														MyApplication.shopCartList
																.remove(product);
												}
											}
											try {
												MyApplication.shopCartManager.saveProducts(MyApplication.shopCartList);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											// ˢ��listview�ĸ߶�
											refreshListViewHeight();
											recalculatePrice();
											adapter.notifyDataSetChanged();
											if (MyApplication.shopCartList
													.size() == 0) {
												title.setRightLayoutVisibility(View.GONE);// ����ɾ����ť
												emptyLayout
														.setVisibility(View.VISIBLE);// ��ʾ�չ��ﳵģ��
												unemptyLayout
														.setVisibility(View.GONE);// ���طǿչ��ﳵģ��
											}
											selectAll.setChecked(false);
											dialog.cancel();
										}
									})
							.setNegativeButton("����",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					CustomDialog alert = builder.create();
					alert.show();
				}
			}
			break;
		}

	}

	private boolean checkProducts() {
		tempSubmitProduct.clear();
		for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
			Product product = (Product) MyApplication.shopCartList.get(i);
			if (product.isChecked())
				tempSubmitProduct.add(product);
		}

		if (tempSubmitProduct.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public void showResualt(JSONObject response,
			com.test.utils.NetworkAction request) throws JSONException {
		// TODO Auto-generated method stub

	}
}
