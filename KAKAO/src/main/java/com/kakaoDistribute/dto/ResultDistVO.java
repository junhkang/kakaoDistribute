package com.kakaoDistribute.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize
public class ResultDistVO<T> {


	private String resultCd;
	private String resultMsg;
	private String token;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}


}
