package com.kakaoDistribute.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize
public class ResultRetVO<T> {

	private String resultCd;
	private String resultMsg;
	private String datecreated;
	private long amount;
	private long distSum;
	private List<Map<String, Object>> distDetail;
	
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
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(String datecreated) {
		this.datecreated = datecreated;
	}
	public long getDistSum() {
		return distSum;
	}
	public void setDistSum(long distSum) {
		this.distSum = distSum;
	}
	public List<Map<String, Object>> getDistDetail() {
		return distDetail;
	}
	public void setDistDetail(List<Map<String, Object>> distDetail) {
		this.distDetail = distDetail;
	}


}
