package com.supcoder.fundcrawler.http;

import java.util.Vector;

/**
 * 获取HTTP响应
 * 
 * @author junwei
 *
 * @version 2016年11月11日下午6:15:59
 */
public class HttpRespons {


	public Vector<String> getContentCollection() {
		return contentCollection;
	}

	public void setContentCollection(Vector<String> contentCollection) {
		this.contentCollection = contentCollection;
	}

	Vector<String> contentCollection;


}
