package com.test.product;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.test.R;
import com.test.base.ChangeTime;
import com.test.base.MyAdapter;
import com.test.base.MyApplication;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.base.Url;
import com.test.model.Product;
import com.test.utils.ConnectServer;
import com.test.utils.NetworkAction;

public class SeckillProduct extends NormalActivity {

	private Title title;// ���ñ�����
	private GridView gridView;
	private ArrayList<Object> secKillProduct; // ���ݼ���
	private MyAdapter adapterSecKill; // ��ɱ��Ʒ������
	HashMap<String, String> paramterSeckill;
	public static Handler secHandler;
	private int newHeight = 0;
	private boolean load;
	private int page = 1; // ��ǰҳ��
	private String pageSize = "10"; // ÿҳ��ʾ����������
	private int totalPage = 0; // ��ҳ��
	public ChangeTime changeTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seckill);
		initView();
		initData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ChangeTime.sectimeList.clear();
		ChangeTime.sectxtViewList.clear();
	}
	private void initView() {
		title = (Title) findViewById(R.id.title);
		gridView = (GridView) findViewById(R.id.seckill_gridview);
		secKillProduct = new ArrayList<Object>();
		adapterSecKill = new MyAdapter(this, NetworkAction.��ɱ��Ʒ, secKillProduct);
		paramterSeckill = new HashMap<String, String>();
		gridView.setAdapter(adapterSecKill);
	}

	private void initData() {
		title.setModule(2);
		title.setTitleTxt("��ʱ��ɱ");
		// ��ɱ��Ʒ
		paramterSeckill.put("act", "list");
		paramterSeckill.put("sid", MyApplication.sid);
		paramterSeckill.put("nowpage", String.valueOf(page));
		paramterSeckill.put("pagesize", pageSize);
		ConnectServer.getResualt(this, paramterSeckill, NetworkAction.��ɱ��Ʒ,
				Url.URL_SECKILL);
		// ÿ��ˢ����ɱ��Ʒʱ���handler
				secHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						Bundle bundle = msg.getData();
						// ȡ�����µ�ʱ��
						long time = bundle.getLong("time");
						// ȡ�����������ݸ�������ѯ��Ӧ��ʱ���textview
						int index = bundle.getInt("index");
//						Log.i("test", ""+index);
						// �����̷߳��صļ�����˵��µ�ʱ���ַ���
						String timeString = bundle.getString("timeString");
						TextView txt = (TextView) ((View) ChangeTime.sectxtViewList.get(
								index).getTag())
								.findViewById(R.id.home_seckill_outtime);
						// �����ɱ��û�н����Ļ���ˢ�����µ�ʱ�䣬�������»�ȡһ����ɱ��Ʒ
						// if(time>0)
						// ChangeTime.txtViewList.get(index).setText(timeString);
						if (time > 0)
							txt.setText(timeString);
						else {
							ChangeTime.sectxtViewList.clear();
							ChangeTime.sectimeList.clear();
							secKillProduct.clear();
							ConnectServer.getResualt(SeckillProduct.this, paramterSeckill,
									NetworkAction.��ɱ��Ʒ, Url.URL_SECKILL);
						}
					}
				};
	}

	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
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
