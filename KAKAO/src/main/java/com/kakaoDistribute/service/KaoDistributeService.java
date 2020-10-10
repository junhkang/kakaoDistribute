package com.kakaoDistribute.service;

import java.util.List;
import java.util.Map;

import com.kakaoDistribute.dto.KaoDistributeVO;
import com.kakaoDistribute.dto.ResultRetVO;

public interface KaoDistributeService {
	
	public String distribute(Map<String, Object> param) throws Exception;

	public long collect(Map<String, Object> param) throws Exception;

	public ResultRetVO retrieve(Map<String, Object> param) throws Exception; 


}
