package com.test.person;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.base.MyApplication;
import com.test.R;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.base.Url;
import com.test.utils.ConnectServer;
import com.test.utils.NetworkAction;

public class ForgetPwd extends NormalActivity implements OnClickListener {

	private Title title;// ���ñ�����
	private LinearLayout step1;
	private LinearLayout step2;
	private TextView step3;
	private int step = 1;
	private TextView nextStepBtn;// ��һ���İ�ť
	private EditText phoneNumTxt;// �ֻ��������
	private EditText codeTxt;// ��֤�������
	private TextView getCodeTxt;// �����ȡ��֤���Ժ����ʾ��Ϣ
	private TextView getCode;// ��ȡ��֤��İ�ť
	int seconds = 121;// �������Ժ����
	private CountSecond countNum;// ����ʱ����
	private HashMap<String, String> paramter;
	private String codeId;// �����ȡ������֤��ID
	private TextView newPwd;// �µ�����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_pwd);
		initView();
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(4);
		title.setTitleTxt("�һ�����");
		step1 = (LinearLayout) findViewById(R.id.forget_step1);
		step2 = (LinearLayout) findViewById(R.id.forget_step2);
		step3 = (TextView) findViewById(R.id.forget_step3);
		nextStepBtn = (TextView) findViewById(R.id.forget_btn);
		phoneNumTxt = (EditText) findViewById(R.id.forget_phone);
		getCodeTxt = (TextView) findViewById(R.id.forget_sendTxt);
		getCode = (TextView) findViewById(R.id.forget_getcode_btn);
		getCode.setOnClickListener(this);
		codeTxt = (EditText) findViewById(R.id.forget_getcode_txt);
		countNum = new CountSecond();
		newPwd=(TextView) findViewById(R.id.forget_pwd_txt);
		paramter = new HashMap<String, String>();
	}

	private void initData() {
		nextStep();
		nextStepBtn.setOnClickListener(this);
	}

	private void nextStep() {
		if (step == 1) {
			step1.setVisibility(View.VISIBLE);
			step2.setVisibility(View.GONE);
			step3.setVisibility(View.GONE);
			nextStepBtn.setText("��һ��");
		} else if (step == 2) {
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.VISIBLE);
			step3.setVisibility(View.GONE);
			nextStepBtn.setText("�ύ");
		} else if (step == 3) {
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.GONE);
			step3.setVisibility(View.VISIBLE);
			nextStepBtn.setText("���");
		}
	}

	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		if (request.equals(NetworkAction.��ȡ��֤��)) {
			getCodeTxt.setVisibility(View.VISIBLE);
			codeId = response.getString("smsid");
			getCode.setOnClickListener(null);// ȡ����ť�ĵ���¼�
			getCode.setBackgroundDrawable(ForgetPwd.this.getResources()
					.getDrawable(R.drawable.forget_getcode_select));// �ñ�����ɫ���
			handler.post(countNum);// ִ�е���ʱ����

		} else if (request.equals(NetworkAction.��֤��֤��)) {
			// �����֤����֤�ɹ��Ļ����뵽�ڶ���
			step = 2;
			nextStep();
		}
		else if (request.equals(NetworkAction.�һ�����)) {
			// ����޸�����ɹ��Ļ����뵽������
			step =3;
			nextStep();
		}
	}

	// ��֤�绰����
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	@Override
	public void onClick(View v) {
		paramter.clear();
		String phoneNum = phoneNumTxt.getText().toString();
		String code = codeTxt.getText().toString();
		//��֤�ֻ������Ƿ���ȷ
		if(!isMobileNO(phoneNum))
		{
			Toast.makeText(this, "��������ȷ���ֻ����룡", 2000).show();
			return;
		}
		switch (v.getId()) {
		// ��ȡ��֤��İ�ť
		case R.id.forget_getcode_btn:
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phoneNum);
			paramter.put("SmsContent", "�һ�������֤�룺");
			ConnectServer.getResualt(this, paramter, NetworkAction.��ȡ��֤��,
					Url.URL_VERIFICATION);
			break;
		// ��һ���İ�ť
		case R.id.forget_btn:
			if (step == 1) {
				
				if (!code.equals("")) {
					paramter.clear();
					paramter.put("act", "iscode");
					paramter.put("sid", MyApplication.sid);
					paramter.put("mobile", phoneNum);
					paramter.put("smskey", code);
					paramter.put("smsid", codeId);
					ConnectServer.getResualt(this, paramter,
							NetworkAction.��֤��֤��, Url.URL_MEMBER);
				} else
					Toast.makeText(this, "�������յ�����֤�룡", 2000).show();
			}
			else if(step==2)
			{
				String pwd=newPwd.getText().toString();
				if(pwd.length()<6)
				{
					Toast.makeText(this, "��������ڵ���6λ�����룡", 2000).show();
					return;
				}
				paramter.clear();
				paramter.put("sid", MyApplication.sid);
				paramter.put("act", "setpwd");
				paramter.put("smsid", codeId);
				paramter.put("mobile", phoneNum);
				paramter.put("password", pwd);
				paramter.put("smskey", code);
				paramter.put("username", phoneNum);
				ConnectServer.getResualt(this, paramter, NetworkAction.�һ�����,
						Url.URL_MEMBER);
			}else if(step==3)
			{
				finish();
			}
			break;

		}

	}

	Handler handler = new Handler() {
		int temp = seconds;

		public void handleMessage(Message msg) {
			temp--;
			getCode.setText(String.valueOf(temp));
			if (seconds == msg.arg1) {
				getCode.setOnClickListener(ForgetPwd.this);
				getCode.setBackgroundDrawable(ForgetPwd.this.getResources()
						.getDrawable(R.drawable.forget_getcode_noselect));
				getCode.setText("��ȡ��֤��");
				getCodeTxt.setVisibility(View.GONE);
				temp = seconds;
			} else {
				handler.post(countNum);
			}
		};
	};

	// ����ʱ����ÿ��ִ�м�1��Ȼ������Ϣ
	public class CountSecond implements Runnable {
		int count = 0;

		@Override
		public void run() {
			// Log.i(MyApplication.TAG, "run-->"+count);
			count++;
			Message msg = new Message();
			msg.arg1 = count;
			handler.sendMessageDelayed(msg, 1000);

		}

	}

}
