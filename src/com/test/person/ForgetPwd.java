package com.test.person;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.test.R;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.base.Url;
import com.test.utils.ConnectServer;
import com.test.utils.NetworkAction;

public class ForgetPwd extends NormalActivity implements OnClickListener{

	
	private Title title;// ���ñ�����
	private LinearLayout step1;
	private LinearLayout step2;
	private TextView step3;
	private int step=1;
	private TextView nextStepBtn;//��һ���İ�ť
	private EditText phoneNumTxt;// �ֻ��������
	private EditText codeTxt;// ��֤�������
	private TextView sendTxt;//�����ȡ��֤���Ժ����ʾ��Ϣ
	private TextView codeBtn;//��ȡ��֤��İ�ť
	int seconds = 121;// �������Ժ����
	private CountSecond countNum;//����ʱ����
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
		step1=(LinearLayout) findViewById(R.id.forget_step1);
		step2=(LinearLayout) findViewById(R.id.forget_step2);
		step3=(TextView) findViewById(R.id.forget_step3);
		nextStepBtn=(TextView) findViewById(R.id.forget_btn);
		phoneNumTxt = (EditText) findViewById(R.id.forget_phone);
		sendTxt=(TextView) findViewById(R.id.forget_sendTxt);
		codeBtn=(TextView) findViewById(R.id.forget_getcode_btn);
		codeBtn.setOnClickListener(this);
		codeTxt = (EditText) findViewById(R.id.forget_getcode_txt);
		countNum=new CountSecond();
	}
	private void initData() {
		nextStep();
		
	}
	
	private void nextStep()
	{
		if(step==1)
		{
			step1.setVisibility(View.VISIBLE);
			step2.setVisibility(View.GONE);
			step3.setVisibility(View.GONE);
			nextStepBtn.setText("��һ��");
		}
		else if(step==2)
		{
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.VISIBLE);
			step3.setVisibility(View.GONE);
			nextStepBtn.setText("�ύ");
		}
		else if(step==3)
		{
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.GONE);
			step3.setVisibility(View.VISIBLE);
			nextStepBtn.setText("���");
		}
	}
	
	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//��ȡ��֤��İ�ť
		case R.id.forget_getcode_btn:
//			ConnectServer.getResualt(this, paramter,
//					NetworkAction.��¼, Url.URL_USERS);
			break;

		}
		
	}
	
	Handler handler = new Handler() {
		int temp=seconds;
		public void handleMessage(Message msg) {
			temp--;
			codeBtn.setText(String.valueOf(temp));
			if (seconds == msg.arg1) {
				codeBtn.setOnClickListener(ForgetPwd.this);
				codeBtn.setBackgroundDrawable(ForgetPwd.this.getResources()
						.getDrawable(R.drawable.forget_getcode_noselect));
				codeBtn.setText("��ȡ��֤��");
				temp=seconds;
			}
			else
			{
				handler.post(countNum);
			}
		};
	};
	
	//����ʱ����ÿ��ִ�м�1��Ȼ������Ϣ
	public class CountSecond implements Runnable {
		int count= 0;

		@Override
		public void run() {
//			Log.i(MyApplication.TAG, "run-->"+count);
			count++;
			Message msg = new Message();
			msg.arg1 = count;
			handler.sendMessageDelayed(msg, 1000);

		}

	}

}
