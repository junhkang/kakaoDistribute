package com.kakaoDistribute.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize
public class ResultColVO<T> {


	private String resultCd;
	private String resultMsg;
	private double price;
	public String getResultCd() {
		return resultCd;
	}
	public void setResultCd(String resultCd) {
		this.resultCd = resultCd;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}


}
