package com.kakaoDistribute.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaoDistribute.dao.KaoDistributeDAO;
import com.kakaoDistribute.dto.ResultRetVO;
import com.kakaoDistribute.service.KaoDistributeService;

import util.StringUtil;

@Service
public class KaoDistributeServiceImpl implements KaoDistributeService {

	@Inject
	private KaoDistributeDAO kaoDistributeDao;
	
	@Transactional(rollbackFor= {RuntimeException.class, Exception.class})
	@Override
	public String distribute(Map<String, Object> param) throws Exception {
		String xUserId = (String) param.get("xUserId");
		String xRoomId = (String) param.get("xRoomId");
		String amount = (String) param.get("amount");
		String targetCnt = (String) param.get("targetCnt");
		String randomToken = "";
		int tokenCnt = 1;
		while (tokenCnt!=0)		{
			randomToken = StringUtil.getRandomToken(3);
			tokenCnt = kaoDistributeDao.checkTokenExist(randomToken);
			// 생성가능한 토큰이 모두 생성되었을 경우 방지로직 추가 필요 
		}
		long distSeq = kaoDistributeDao.selectDistSeq();
		Integer amountL = Integer.valueOf(amount);
		Integer targetCntL = Integer.valueOf(targetCnt);
		Integer xUserIdL = Integer.valueOf(xUserId);
		Integer xRoomIdL = Integer.valueOf(xRoomId);
		
		Integer leftOver = amountL%targetCntL;
		Integer eachAmount = amountL/targetCntL;
		
		Map<String, Object> distParam = new HashMap<String,Object>();
		distParam.put("kao_dist_id", distSeq);
		distParam.put("sender", xUserIdL);
		distParam.put("room", xRoomIdL);
		distParam.put("amount", amountL);
		distParam.put("target_cnt", targetCntL);
		distParam.put("token", randomToken);
		// 뿌리기 원장 정보 insert 
		kaoDistributeDao.insertDistribute(distParam);
		
		Map<String, Object> distDetailParam = new HashMap<String,Object>();
		distDetailParam.put("kao_dist_id", distSeq);
		distDetailParam.put("sender", xUserIdL);
		// 뿌리기 상세 정보 insert
		for (int i =0; i<targetCntL; i++) {
			if (targetCntL-1 == i) {
				eachAmount = eachAmount+leftOver;
			}
			distDetailParam.put("each_amount", eachAmount);
			kaoDistributeDao.insertDistributeDetail(distDetailParam);
		}
		return randomToken;
	}

	@Transactional(rollbackFor= {RuntimeException.class, Exception.class})
	@Override
	public long collect(Map<String, Object> param) throws RuntimeException,Exception {
		String xUserId = (String) param.get("xUserId");
		String xRoomId = (String) param.get("xRoomId");
		String token = (String) param.get("token");
		long price = 0;
		
		Integer xUserIdL = Integer.valueOf(xUserId);
		Integer xRoomIdL = Integer.valueOf(xRoomId);
		
		Map<String, Object> distParam = new HashMap<String,Object>();
		distParam.put("receiver", xUserIdL);
		distParam.put("room", xRoomIdL);
		distParam.put("token", token);
		// 뿌리기 원장 정보 insert 
		Map<String, Object> distInfo = kaoDistributeDao.selectDistributeInfo(distParam);
		if (distInfo == null )	{
			//해당 뿌리기 건이 없습니다. 
			throw new RuntimeException("10분 이내의 해당 방/토큰의 뿌리기 정보가 존재하지 않습니다.");
		}
		if ((Integer)distInfo.get("sender") == xUserIdL)	{
			// 자기 자신이 뿌린 뿌리기 건 입니다.
			throw new RuntimeException ("자기 자신의 뿌리기 건 입니다.");
		}
		Integer kaoDistId = (Integer) distInfo.get("kao_dist_id");
		distParam.put("kao_dist_id", kaoDistId);
		Map<String, Object> distDetailInfo = kaoDistributeDao.selectDistributeDetail(distParam);
		if (distDetailInfo == null)	{
			// 덜받은 상세 뿌리기 내역 없음 
			throw new RuntimeException ("줍기가 완료된 뿌리기 건 입니다.");
		}
		distDetailInfo.put("receiver", xUserIdL);
		int distDetailUpd = kaoDistributeDao.updateDistDetail(distDetailInfo);
		if (distDetailUpd != 1)	{
			// 기처리건 재업데이트 시도 (조건에 is null 추가하여 중복처리 방지 ) -> 롤백 
			throw new RuntimeException ("기처리 된 줍기건 입니다. 재시도해주세요.");
		}
		List<Map<String, Object>> distDetailResultInfo = kaoDistributeDao.selectDistributeDetailResult(distParam);
		if (distDetailResultInfo.size() != 1) {
			// 이미 본인이 줍기한 이력이 있는 줍기 건 -> 롤백 
			throw new RuntimeException ("이미 본인이 줍기한 이력이 있는 뿌리기 건 입니다.");
		}
		Map<String, Object> distResult = distDetailResultInfo.get(0);
		price = Long.valueOf( String.valueOf( distResult.get("price")));
		return price;
	}

	@Transactional(rollbackFor= {RuntimeException.class, Exception.class})
	@Override
	public ResultRetVO retrieve(Map<String, Object> param) throws Exception {
		ResultRetVO resultVo = new ResultRetVO();
		String xUserId = (String) param.get("xUserId");
		String xRoomId = (String) param.get("xRoomId");
		String token = (String) param.get("token");
		
		Integer xUserIdL = Integer.valueOf(xUserId);
		Integer xRoomIdL = Integer.valueOf(xRoomId);
		
		Map<String, Object> distParam = new HashMap<String,Object>();
		distParam.put("sender", xUserIdL);
		distParam.put("room", xRoomIdL);
		distParam.put("token", token);
		// 뿌리기 원장 조회 
		Map<String, Object> distInfo = kaoDistributeDao.selectDistributeInfoWithSender(distParam);
		if (distInfo == null )	{
			//해당 뿌리기 건이 없습니다. 
			throw new RuntimeException("해당 7일내 해당 사용자의 방/토큰의 뿌리기 정보가 존재하지 않습니다.");
		}
		
		Integer kaoDistId = (Integer) distInfo.get("kao_dist_id");
		String datecreated = (String) distInfo.get("datecreated");
		long amount =  Long.valueOf( String.valueOf( distInfo.get("amount")));
		long distSum =  Long.valueOf( String.valueOf( distInfo.get("dist_sum")));
		distParam.put("kao_dist_id", kaoDistId);
		// 뿌리기 상세 조회 
		List<Map<String, Object>> distDoneInfo = kaoDistributeDao.selectDistDoneDetail(distParam);
		
		resultVo.setDatecreated(datecreated);
		resultVo.setAmount(amount);
		resultVo.setDistSum(distSum);
		resultVo.setDistDetail(distDoneInfo);
		
		return resultVo;
	}

	


}
