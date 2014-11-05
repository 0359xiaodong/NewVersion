package com.test.person;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.base.MyApplication;
import com.test.R;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.base.Url;
import com.test.product.Home;
import com.test.utils.ConnectServer;
import com.test.utils.NetworkAction;

public class Login extends NormalActivity implements OnClickListener {

	private Title title;// ���ñ�����
	private EditText userName;// �û���
	private EditText userPwd;// ����
	private TextView forgetPwd;// ��������
	private TextView register;// ����ע��
	private TextView login;// ��¼��ť

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initView();
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(4);
		title.setTitleTxt("��¼");
		userName = (EditText) findViewById(R.id.login_username);
		userPwd = (EditText) findViewById(R.id.login_password);
		forgetPwd = (TextView) findViewById(R.id.login_forget_btn);
		register = (TextView) findViewById(R.id.login_register_btn);
		login = (TextView) findViewById(R.id.login_btn);
	}

	private void initData() {
		forgetPwd.setOnClickListener(this);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		if (MyApplication.sp.getString("username", "") != null)
		{
			userName.setText(MyApplication.sp.getString("username", ""));
			userPwd.setText(MyApplication.sp.getString("password", ""));
		}
	}

	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		//����Ƿ��Ǹ������û�������ǵĻ���չ��ﳵ��Ϣ
		if (!MyApplication.sp.getString(
				"username", "").equals(
						response.getString("username"))) {
			MyApplication.shopCartList
					.clear();
			try {
				MyApplication.shopCartManager
						.saveProducts(MyApplication.shopCartList);
				MyApplication.shopcart_refresh = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		MyApplication.seskey = response
				.getString("sessionid");
		MyApplication.uid = response
				.getString("uid");
		MyApplication.ed.putString("password",userPwd.getText().toString());
		MyApplication.ed.putString("username",
				response.getString("username"));

		MyApplication.ed.putString("nickname",
				response.getString("nickname"));
		MyApplication.ed.putString("address",
				response.getString("address"));

		MyApplication.ed.putString("photo",
				response.getString("photo"));
		MyApplication.ed.putString("email",
				response.getString("email"));
		MyApplication.ed.putString("createtime",
				response.getString("createtime"));
		MyApplication.ed.putString("birthday",
				response.getString("birthday"));
		MyApplication.ed.putString("sex",
				response.getString("sex"));
		MyApplication.ed.putString("credit",
				response.getString("credit"));
		MyApplication.ed.putString("newfriends",
				response.getString("newfriends"));
		MyApplication.ed.putString("province_name",
				response.getString("province_name"));
		MyApplication.ed.putString("city_name",
				response.getString("city_name"));
		MyApplication.ed.putString("area_name",
				response.getString("area_name"));
		MyApplication.ed.putString("province_id",
				response.getString("province_id"));
		MyApplication.ed.putString("city_id",
				response.getString("city_id"));
		MyApplication.ed.putString("area_id",
				response.getString("area_id"));
		MyApplication.ed.commit();
		MyApplication.loginStat = true;
		Login.this.finish();
		Toast.makeText(Login.this, "��¼�ɹ�", 2000)
				.show();
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.login_forget_btn:
			intent=new Intent().setClass(this, ForgetPwd.class);
			break;
		case R.id.login_register_btn:

			break;
		case R.id.login_btn:
			login();
			break;
		}
		if(intent!=null)
			startActivity(intent);
	}

	private void login() {
		String username = "";
		String password = "";
		username = userName.getText().toString();
		password = userPwd.getText().toString();
		// ������Ϊ��
		if (username.equals("") || password.equals("")) {
			Toast.makeText(this, "�������û��������룡", 2000).show();
			return;
		}
		HashMap<String, String> paramter = new HashMap<String, String>();
		paramter.put("act", "login");
		paramter.put("sid", MyApplication.sid);
		paramter.put("username", username);
		paramter.put("password", password);
		ConnectServer.getResualt(this, paramter,
				NetworkAction.��¼, Url.URL_USERS);
	}
}
