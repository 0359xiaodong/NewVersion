package com.test.model;

import java.io.Serializable;

public class Attribute  implements Serializable{

	private String name;//��������
	private String price;//�����Զ�Ӧ�ļ۸�
	private String ID;//������ID
	private boolean isChecked;//�Ƿ�ѡ��
	
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	
}
