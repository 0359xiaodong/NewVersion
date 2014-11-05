package com.test.person;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;

import cn.jpush.android.api.JPushInterface;

import com.test.base.CustomDialog;
import com.test.utils.ConnectServer;
import com.test.utils.UpdateVersion;
import com.test.base.MyApplication;
import com.test.R;
import com.test.base.NormalActivity;
import com.test.base.Title;
import com.test.base.Url;
import com.test.utils.NetworkAction;

public class More extends NormalActivity implements OnClickListener {

	private Title title;// ���ñ�����
	private CheckBox pushBox;// ������Ϣ
	private FrameLayout update;// �汾����
	private FrameLayout clear;// ��ջ���
	private FrameLayout about;// ��������
	private FrameLayout phone;// �ͷ��绰
	private Button loginOutBtn;// �˳���ť
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_more);
		initView();
		initData();
	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		title.setModule(4);
		title.setTitleTxt("����");
		pushBox = (CheckBox) findViewById(R.id.more_push);
		pushBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// ����Ϣ���ͷ���
				if (isChecked) {
					JPushInterface.resumePush(MyApplication.context);
					buttonView.setBackgroundDrawable(MyApplication.resources
							.getDrawable(R.drawable.push_select));
					Toast.makeText(More.this, "��Ϣ���͹����Ѵ򿪣�", 2000).show();
				}
				// �ر���Ϣ����
				else {
					JPushInterface.stopPush(MyApplication.context);
					buttonView.setBackgroundDrawable(MyApplication.resources
							.getDrawable(R.drawable.push_noselect));
					Toast.makeText(More.this, "��Ϣ���͹����ѹرգ�", 2000).show();
				}
				MyApplication.jPush = isChecked;
				MyApplication.ed.putBoolean("push", isChecked);
				MyApplication.ed.commit();
			}
		});
		update = (FrameLayout) findViewById(R.id.more_update);
		clear = (FrameLayout) findViewById(R.id.more_clear);
		about = (FrameLayout) findViewById(R.id.more_about);
		phone = (FrameLayout) findViewById(R.id.more_phone);
		handler = new Handler() {
			public void handleMessage(Message arg0) {
				super.handleMessage(arg0);
				Toast.makeText(More.this, "������", 2000).show();
			}
		};

		loginOutBtn = (Button) findViewById(R.id.more_loginout_btn);

	}

	private void initData() {
		// �鿴�����ļ�����Ϣ�����Ƿ��
		MyApplication.jPush = MyApplication.sp.getBoolean("push", true);
		if (MyApplication.jPush)
			pushBox.setBackgroundDrawable(MyApplication.resources
					.getDrawable(R.drawable.push_select));
		else
			pushBox.setBackgroundDrawable(MyApplication.resources
					.getDrawable(R.drawable.push_noselect));
		pushBox.setChecked(MyApplication.jPush);
		update.setOnClickListener(this);
		clear.setOnClickListener(this);
		about.setOnClickListener(this);
		phone.setOnClickListener(this);
		if (MyApplication.loginStat) 
			loginOutBtn.setOnClickListener(this); 
		else
			loginOutBtn.setVisibility(View.GONE);

	}

	@Override
	public void showResualt(JSONObject response, NetworkAction request)
			throws JSONException {
		if(request.equals(NetworkAction.ע����¼))
		{
			MyApplication.loginStat=false;
			Toast.makeText(this, "�ɹ��˳���¼��",2000).show();
			loginOutBtn.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.more_loginout_btn:
			CustomDialog.Builder builder1 = new CustomDialog.Builder(this);
			builder1.setMessage("ȷ���˳���¼��")
		      .setPositiveButton("ȷ��",
		        new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog,
		           int id) {
		        	 HashMap<String, String> paramter = new HashMap<String, String>();
		     		paramter.put("act", "logout");
		     		paramter.put("sessionid", MyApplication.seskey);
		     		ConnectServer.getResualt(More.this, paramter, NetworkAction.ע����¼,
		     				Url.URL_USERS);
						 dialog.cancel();
		         }
		        })
		      .setNegativeButton("����",
		        new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog,
		           int id) {
		          dialog.cancel();
		         }
		        });
		    CustomDialog alert1 = builder1.create();
		    alert1.show();
			break;
		case R.id.more_update:
			Toast.makeText(this, "���ڼ�飬���Ժ�", 2000).show();
			new UpdateVersion("", this).startThread();
			break;
		case R.id.more_clear:
			handler.sendEmptyMessageAtTime(1, 2000);
			break;
		case R.id.more_about:
			intent = new Intent().setClass(this, MoreAbout.class);
			break;
		case R.id.more_phone:
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setMessage(
					"��ȷ������"
							+ MyApplication.resources
									.getString(R.string.more_tel_num_txt)
							+ "��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent phoneIntent = new Intent(
											"android.intent.action.CALL",
											Uri.parse("tel:"
													+ MyApplication.resources
															.getString(R.string.person_more_tel_num)));
									startActivity(phoneIntent);
									dialog.cancel();
								}
							})
					.setNegativeButton("����",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			CustomDialog alert = builder.create();
			alert.show();
			break;
		}
		if (intent != null)
			startActivity(intent);
	}

}
