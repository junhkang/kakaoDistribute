package com.kakaoDistribute.dao;

import java.util.List;
import java.util.Map;

import com.kakaoDistribute.dto.KaoDistributeVO;

public interface KaoDistributeDAO {

	public int checkTokenExist(String randomToken) throws Exception;

	public long selectDistSeq()  throws Exception;

	public void insertDistribute(Map<String, Object> distParam) throws Exception;

	public void insertDistributeDetail(Map<String, Object> distDetailParam) throws Exception;

	public Map<String, Object> selectDistributeDetail(Map<String, Object> distParam) throws Exception;

	public Map<String, Object> selectDistributeInfo(Map<String, Object> distParam) throws Exception;

	public int updateDistDetail(Map<String, Object> distDetailInfo) throws Exception;

	public List<Map<String, Object>> selectDistributeDetailResult(Map<String, Object> distParam) throws Exception;

	public Map<String, Object> selectDistributeInfoWithSender(Map<String, Object> distParam) throws Exception;

	public List<Map<String, Object>> selectDistDoneDetail(Map<String, Object> distParam) throws Exception;

}
