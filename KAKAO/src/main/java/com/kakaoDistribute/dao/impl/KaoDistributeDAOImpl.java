package com.kakaoDistribute.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kakaoDistribute.dao.KaoDistributeDAO;

@Repository
public class KaoDistributeDAOImpl implements KaoDistributeDAO {

	@Inject
	private SqlSession sqlSession;
	
	private static final String Namespace = "com.kakaoDistribute.mapper.kaoDistributeMapper";
	
	@Override
	public int checkTokenExist(String randomToken) throws Exception {
		Map<String, Object> param = new HashMap<String,Object>();
		param.put("randomToken", randomToken);
		return sqlSession.selectOne(Namespace+".checkTokenExist", param); 
	}

	@Override
	public long selectDistSeq() throws Exception {
		return sqlSession.selectOne(Namespace+".selectDistSeq"); 
	}

	@Override
	public void insertDistribute(Map<String, Object> distParam) throws Exception {
		sqlSession.insert(Namespace+".insertDistribute", distParam); 
	}

	@Override
	public void insertDistributeDetail(Map<String, Object> distDetailParam) throws Exception {
		sqlSession.insert(Namespace+".insertDistributeDetail", distDetailParam); 
	}

	@Override
	public Map<String, Object> selectDistributeDetail(Map<String, Object> distParam) throws Exception {
		return sqlSession.selectOne(Namespace+".selectDistributeDetail", distParam); 
	}

	@Override
	public Map<String, Object> selectDistributeInfo(Map<String, Object> distParam) throws Exception {
		return sqlSession.selectOne(Namespace+".selectDistributeInfo", distParam);
	}

	@Override
	public int updateDistDetail(Map<String, Object> distDetailInfo) throws Exception {
		return sqlSession.update(Namespace+".updateDistDetail", distDetailInfo);
	}

	@Override
	public List<Map<String, Object>> selectDistributeDetailResult(Map<String, Object> distParam) throws Exception {
		return sqlSession.selectList(Namespace+".selectDistributeDetailResult", distParam);
	}

	@Override
	public Map<String, Object> selectDistributeInfoWithSender(Map<String, Object> distParam) throws Exception {
		return sqlSession.selectOne(Namespace+".selectDistributeInfoWithSender", distParam);
	}

	@Override
	public List<Map<String, Object>> selectDistDoneDetail(Map<String, Object> distParam) throws Exception {
		return sqlSession.selectList(Namespace+".selectDistDoneDetail", distParam);
	}

	
}
