package com.test.base;

import com.test.R;
import com.test.person.More;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Title extends FrameLayout {

	private Context context;
	private LinearLayout backBtn;// �������ĺ��˰�ť
	private LinearLayout msgBtn;// ��Ϣ��ť
	private FrameLayout searchLayout;// ����ģ��
	private LinearLayout moreBtn;// ����ģ��
	private FrameLayout morePage;// ����ҳ��
	private LinearLayout txtLayout;// ����ģ��
	private TextView titleTxt;// ����������
	private EditText searchTxt;

	public Title(Context context) {
		super(context, null);
	}

	public Title(final Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.title, this, true);
		backBtn = (LinearLayout) findViewById(R.id.backBtn);
		msgBtn = (LinearLayout) findViewById(R.id.msgBtn);
		titleTxt = (TextView) findViewById(R.id.title_txt);
		searchLayout = (FrameLayout) findViewById(R.id.searchLayout);
		moreBtn = (LinearLayout) findViewById(R.id.moreBtn);
		morePage = (FrameLayout) findViewById(R.id.morePage);
		txtLayout = (LinearLayout) findViewById(R.id.txtLayout);
		backBtn.setOnClickListener(new OnclickListener());
		msgBtn.setOnClickListener(new OnclickListener());
		moreBtn.setOnClickListener(new OnclickListener());
		morePage.setOnClickListener(new OnclickListener());
		searchTxt = (EditText) findViewById(R.id.searchTxt);
		searchTxt.setOnEditorActionListener(new MyApplication.OnEditorActionListener(context,searchTxt));
	}

	
	public class OnclickListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			Intent intent=null;
			switch (view.getId()) {
			case R.id.backBtn:
				((Activity) context).finish();
				break;
			case R.id.msgBtn:
				// ��ת����Ϣ��
				break;
			case R.id.moreBtn:
				// ���ఴť�ĵ�����
				break;
			case R.id.morePage:
				// ��ת����ҳ��
				intent=new Intent().setClass(context, More.class);
				break;
			}
			if(intent!=null)
				((Activity)context).startActivity(intent);
		}
	}

	/**
	 * ���ñ�����ģʽ 
	 * 1. ��Ϣ��ť+������+���ఴť 
	 * 2. ���˰�ť+��������+���ఴť 
	 * 3. �������� 
	 * 4. ���˰�ť+�������� 
	 * 5. ���˰�ť+������+���ఴť
	 * 6. �������� +����ҳ��
	 * @param module
	 */
	public void setModule(int module) {
		switch (module) {
		case 1:
			msgBtn.setVisibility(View.VISIBLE);
			searchLayout.setVisibility(View.VISIBLE);
			moreBtn.setVisibility(View.VISIBLE);
			break;
		case 2:
			backBtn.setVisibility(View.VISIBLE);
			txtLayout.setVisibility(View.VISIBLE);
			moreBtn.setVisibility(View.VISIBLE);
			break;
		case 3:
			txtLayout.setVisibility(View.VISIBLE);
		case 4:
			backBtn.setVisibility(View.VISIBLE);
			txtLayout.setVisibility(View.VISIBLE);
			break;
		case 5:
			backBtn.setVisibility(View.VISIBLE);
			searchLayout.setVisibility(View.VISIBLE);
			moreBtn.setVisibility(View.VISIBLE);
			break;
		case 6:
			txtLayout.setVisibility(View.VISIBLE);
			morePage.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * ���ú��˰�ť�Ŀ���ʾ��
	 * @param visibility
	 */
	public void setBackBtnVisibility(boolean visibility) {
		if (visibility)
			backBtn.setVisibility(View.VISIBLE);
		else
			backBtn.setVisibility(View.GONE);
	}

	// ���������ʾ������ģ�������øñ���������������
	public void setTitleTxt(String title) {
		titleTxt.setText(title);
	}
	
	//�������������������
	public void setSearchTxt(String searchString) {
		searchTxt.setText(searchString);
	}
}
