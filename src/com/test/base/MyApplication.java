package com.test.base;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import com.test.utils.CacheManager;
import com.test.utils.MyHttpClient;
import com.test.model.City;
import com.test.model.Product;
import com.test.product.ProductShow;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.sax.StartElementListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyApplication extends Application {
	public static boolean loginStat = false; // ��¼״̬
	public static boolean jPush = false; // �Ƿ������Ϣ����
	public static boolean goToOrder = false;// �Ƿ�Ҫ��ת���鿴������ҳ��
	public static ProgressDialog mypDialog; // ȫ�ֽ�����
	public static int width; // ��Ļ��
	public static int height; // ��Ļ��
	public static LayoutInflater Inflater; // ���������
	public static String seskey = ""; // ��¼ע�᷵�ص������Կ
	public static String uid = ""; // ��¼ע�᷵�ص��û�id
	private static MyApplication instance; // Myapplication����
	private List<Activity> mList = new LinkedList<Activity>(); // ���ؼ��ϴ��Activity����
	public static SharedPreferences sp; // ���ش洢SharedPreferences
	public static Editor ed; // ���ش洢�༭��Editor
	public CacheManager mcCacheManager; // ���������
	public static boolean registerSuc = false; // ע��ɹ��Ժ�֪ͨ��¼ҳ���Ƿ���Ҫˢ�²���¼
	public static Resources resources;
	public static MyHttpClient client;
	public static String TAG = "New";// �����õ�TAG
	public static int subStringLength = 20;// �����Ʒ���ƹ��������ַ����ĳ���
	public static ArrayList<Object> shopCartList;// ���õ����ﳵ����Ʒ�ļ���
	public static ShopCartManager shopCartManager;// ���ﳵ������
	public static Boolean shopcart_refresh = false;// ���ﳵ���ݷ����仯ʱ�Ƿ���Ҫˢ�¹��ﳵ����
	public static String sid = "15";// �̼�ID
	public static boolean exit = false;
	public static Context context;
	public static boolean comment = false;// �Ƿ������۶�����״̬
	public static boolean seckillModule = false;// �Ƿ���뵽��ɱר��
	/*
	 * ��������ģʽ
	 * 1. �ӷ����б���ת����Ʒ�б����ݷ���ID��ȡ��Ӧ����Ʒ���� 
	 * 2. ������ҳ�������������������Ϣ��������������������������ȡ����Ʒ����
	 * 3. �Ӹ��ఴť��ת����Ʒ�б�ҳ�棬���ǻ�û����������������û����Ʒ���ݣ���Ҫ�������������������ݵ��������ť��ʱ���������ģʽ���ڶ���
	 */
	public static int searchModule;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
		shopCartManager = new ShopCartManager(getApplicationContext());
		shopCartList = new ArrayList<Object>();
		try {
			shopCartList = shopCartManager.readShopCart();
		} catch (StreamCorruptedException e) {
			shopCartManager.dele();
			e.printStackTrace();
		} catch (IOException e) {
			shopCartManager.dele();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			shopCartManager.dele();
			e.printStackTrace();
		}
		/*
		 * ��ʼ��SharedPreferences
		 */
		sp = getSharedPreferences("hrht", MODE_PRIVATE);
		/*
		 * ��ʼ��LayoutInflater
		 */
		Inflater = LayoutInflater.from(getApplicationContext());
		ed = sp.edit();

		resources = getResources();
		/*
		 * ��ʼ��Volley��ܵ�Http������
		 */
		client = MyHttpClient.getInstance(MyApplication.this
				.getApplicationContext());
		/*
		 * ʵ�������������
		 */
		mcCacheManager = new CacheManager(getApplicationContext());
		// city=new City();
	}

	/**
	 * ��ȡ���ص�����
	 * 
	 * @param activity
	 * @return
	 */
	public String getClassName(Class myclass) {
		String fullName = myclass.getName();
		fullName = fullName.substring(fullName.lastIndexOf(".") + 1);
		return fullName;
	}

	/*
	 * ��ȡ���������
	 */
	public CacheManager getCacheManager() {
		if (mcCacheManager == null) {
			mcCacheManager = new CacheManager(getApplicationContext());
		}

		return mcCacheManager;
	}

	/*
	 * ͬ����ȫ��ʽ��ȡMyApplication����ʵ��
	 */
	public synchronized static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// ��ӡ��Ҫ��ӡ����Ϣ
	public static void printLog(String className, String msg) {
		Log.i(MyApplication.TAG, className + "--->" + msg);
	}

	/*
	 * ��Activity���뵽��������
	 */
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void removeActivity(Activity finishActivity) {
		for (Activity activity : mList) {
			if (activity.equals(finishActivity))
				activity.finish();
		}
	}

	/*
	 * ����Ӧ�ó����˳���ѭ������������û�����ٵ�Activity��ȫ�������Ժ����˳�����֤Ӧ�ó��������ر��˳�
	 */
	public void exit() {
		try {
			printLog("application", "exit" + mList.size());
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
			printLog("application", "exit" + mList.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!exit)
				System.exit(0);
		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	/**
	 * ȫ�ֽ����������κ�һ��activity�ж����Ե��øý����������뵱ǰcontext����Ҫ��ʾ�����ּ���
	 * ����Ҫ���ֽ�������ģ����ø÷�������ִ�������������Ժ����ۺ�����������Ƚ������ý�����
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param title
	 *            ����
	 * @param msg
	 *            ��������ʾ������
	 */
	public static void progressShow(Context context, String msg) {

		mypDialog = new ProgressDialog(context);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// ���ý�������񣬷��Ϊ���Σ��п̶ȵ�
		mypDialog.setTitle("����");
		// ����ProgressDialog ����
		mypDialog.setMessage(msg + "������...");
		// ����ProgressDialog ��ʾ��Ϣ
		// mypDialog.setIcon(R.drawable.android);
		// //����ProgressDialog ����ͼ��
		mypDialog.setProgress(40);
		// ����ProgressDialog ����������
		// mypDialog.setButton("�������",this);
		// //����ProgressDialog ��һ��Button
		mypDialog.setCancelable(true);
		// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
		mypDialog.show();
		// ��ProgressDialog��ʾ
	}

	// �رս�����
	public static void progressClose() {
		mypDialog.dismiss();
	}

	// ��ȡƴ�ӳ����������ַ���
	public static String getUrl(HashMap<String, String> paramter, String url) {
		Iterator iter = paramter.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			if (count == 0)
				url = url + "?" + key + "=" + val;
			else
				url = url + "&" + key + "=" + val;
			count++;
		}
		return url;
	}

	public static String limitString(String name) {
		if (name.length() > subStringLength) {
			name = name.substring(0, subStringLength) + "...";
		}
		return name;
	}

	
	
	//title�������������������̵������¼�
	public static class OnEditorActionListener implements
			android.widget.TextView.OnEditorActionListener {

		private Context contextTemp;
		private View view;

		public OnEditorActionListener(Context context, View view) {
			contextTemp = context;
			this.view = view;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				// �����ؼ���
				((InputMethodManager) view.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
						((Activity) contextTemp).getCurrentFocus()
								.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				if(((EditText)view).getText().toString().equals(""))
					Toast.makeText(contextTemp,"�������������ݣ�", 2000)
					.show();
				//���������Ʒ�б�ҳ��
				 if(((Activity)contextTemp).getLocalClassName().equals("product.ProductShow"))
				 {
					 MyApplication.searchModule=3;
					 ((ProductShow)contextTemp).searchTxt=((EditText)view).getText().toString();
					 ((ProductShow)contextTemp).initData();
				 }
				 else
				 {
					 Intent intent=new Intent();
					 intent.setClass(contextTemp, ProductShow.class);
					 intent.putExtra("searchTxt", ((EditText)view).getText().toString());
					 MyApplication.searchModule=2;
					 ((Activity)contextTemp).startActivity(intent);
				 }

			}
			return true;

		}
	}
}
