package com.test.person;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.test.R;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.utils.NetworkAction;

public class Person extends NormalActivity implements OnClickListener {

	private Title title;// ���ñ�����
	private FrameLayout order;// ������ѯ
	private FrameLayout message;// �ҵ���Ϣ
	private FrameLayout address;// ��ַ����
	private FrameLayout sec;// �˻���ȫ
	private FrameLayout sugguest;// �������
	private FrameLayout coupon;// �ҵ��Ż�ȯ
	private TextView loginBtn;// ��ת����¼ҳ��İ�ť
	private NetworkImageView img;// ��¼�Ժ���ʾ�ĸ���ͷ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person);
		initView();
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(6);
		title.setTitleTxt("�ҵ�");
		order = (FrameLayout) findViewById(R.id.person_order);
		message = (FrameLayout) findViewById(R.id.person_message);
		address = (FrameLayout) findViewById(R.id.person_address);
		sec = (FrameLayout) findViewById(R.id.person_sec);
		sugguest = (FrameLayout) findViewById(R.id.person_sugguest);
		coupon = (FrameLayout) findViewById(R.id.person_coupon);
		loginBtn = (TextView) findViewById(R.id.goto_login_btn);
		img = (NetworkImageView) findViewById(R.id.person_img);
	}

	private void initData() {
		order.setOnClickListener(this);
		message.setOnClickListener(this);
		address.setOnClickListener(this);
		sec.setOnClickListener(this);
		sugguest.setOnClickListener(this);
		coupon.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		img.setOnClickListener(this);
	}

	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ������ѯ
		case R.id.person_order:

			break;
		// �ҵ���Ϣ
		case R.id.person_message:

			break;
		// ��ַ����
		case R.id.person_address:

			break;
		// �˻���ȫ
		case R.id.person_sec:

			break;
		// �������
		case R.id.person_sugguest:

			break;
		// �ҵ��Ż�ȯ
		case R.id.person_coupon:

			break;
		// ��ת����¼ҳ��İ�ť
		case R.id.goto_login_btn:

			break;
		// ��¼�Ժ���ʾ�ĸ���ͷ�񣬵���Ժ󵯳��˵�
		case R.id.person_img:

			break;
		}

	}

}
