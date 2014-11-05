package com.test.base;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.test.base.CustomDialog;
import com.test.base.MyApplication;
import com.test.utils.NetworkAction;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;

/**
 * ��ͨ��activity�̳еĸ���
 * @author Administrator
 *
 */
public abstract class NormalActivity extends Activity implements showResualtI{

	/*
	 * ���ݸò�����onresume��super()����֮ǰ�ж��Ƿ��ǵ�һ�ν����ҳ��
	*	���Ϊ��Ļ����ǵ�һ�ν����ҳ��
	*	���Ϊ�ٵĻ����Ǵ�����ҳ�淵�ص���ҳ������
	 */
	public boolean isFirstResume=true;
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MyApplication.getInstance().addActivity(this);
		Log.i(MyApplication.TAG,"NormalActivity->onStart" );
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isFirstResume=false;
		Log.i(MyApplication.TAG,"NormalActivity->onResume" );
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.getInstance().removeActivity(this);
		if(MyApplication.mypDialog!=null)
			MyApplication.mypDialog.dismiss();
	}
	
//	 public abstract void showResualt(JSONObject response,NetworkAction request) throws JSONException;
}

interface showResualtI{
	void showResualt(JSONObject response,NetworkAction request) throws JSONException;
}
