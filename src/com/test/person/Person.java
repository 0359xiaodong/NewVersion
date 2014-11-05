package com.test.person;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.test.R;
import com.test.base.MyApplication;
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
	private FrameLayout loginout; // û�е�¼ʱ��ģ��
	private FrameLayout login; // ��¼ʱ��ģ��
	private TextView nameTxt;// ��¼�Ժ���ʾ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// ����Ǵ�����ҳ�淵�ظ�ҳ�沢�ҵ�¼�������
		if (!isFirstResume && MyApplication.loginStat) {
			loginout.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
			// ��ȡ�ǳƣ�û�еĻ���ȡ�û���
			String name = MyApplication.sp.getString("nickname","");
			if(name.equals(""))
				name=MyApplication.sp.getString("username", "");
			nameTxt.setText(name);
			// �����û�ͷ�����û������ʾδ��¼ʱ���ͷ��
			String photo = MyApplication.sp.getString("photo", "");
			if (!photo.equals(""))
				MyApplication.client.getImageForNetImageView(photo, img,
						R.drawable.loginout_img);
		}
		// ����Ǵ�����ҳ�淵�ظ�ҳ�沢�����˳���¼�������
		else if (!isFirstResume && !MyApplication.loginStat) {
			loginout.setVisibility(View.VISIBLE);
			login.setVisibility(View.GONE);
		}
		super.onResume();

	}

	private void initView() {
		loginout = (FrameLayout) findViewById(R.id.loginout_layout);
		login = (FrameLayout) findViewById(R.id.login_layout);
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
		nameTxt = (TextView) findViewById(R.id.person_name);
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
		Intent intent = null;
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
			intent = new Intent().setClass(this, Login.class);
			break;
		// ��¼�Ժ���ʾ�ĸ���ͷ�񣬵���Ժ󵯳��˵�
		case R.id.person_img:

			break;
		}
		if (intent != null)
			startActivity(intent);

	}

}
