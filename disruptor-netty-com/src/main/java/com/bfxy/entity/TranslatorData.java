package com.bfxy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TranslatorData implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 8763561286199081881L;

	private String id;
	private String name;
	private String message;	//传输消息体内容

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
