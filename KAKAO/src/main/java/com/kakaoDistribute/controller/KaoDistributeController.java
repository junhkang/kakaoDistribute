package com.kakaoDistribute.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.kakaoDistribute.dto.ResultColVO;
import com.kakaoDistribute.dto.ResultDistVO;
import com.kakaoDistribute.dto.ResultRetVO;
import com.kakaoDistribute.service.KaoDistributeService;

import util.StringUtil;
/**
 * Handles requests for the application home page.
 */
@RestController
public class KaoDistributeController {
	@Inject
	private KaoDistributeService kaoDistributeService;
	private static final Logger logger = LoggerFactory.getLogger(KaoDistributeController.class);
	

	// 아이디 중복확인 
		@ResponseBody
		@RequestMapping(value = "/kaoDistribute/distributeMoney.do")
		public ResultDistVO distributeMoney(@RequestBody String json, @RequestHeader HttpHeaders headers) throws Exception{
			String bodyString = json;
			String xUserId = "";
			String xRoomId = "";
			String amount = "";
			String targetCnt = "";
			String returnToken = "";
			ResultDistVO resultVo = new ResultDistVO();
			try{
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(json);
				logger.info("[STEP 1] 요청데이터 확인 시작 [Data : "+ bodyString+"]");
				xUserId = StringUtil.checkParseLong(headers.get("X-USER-ID")).trim();
				xRoomId = StringUtil.checkParseLong(headers.get("X-ROOM-ID")).trim();
				amount = StringUtil.checkParseLong(element.getAsJsonObject().get("amount"));
				targetCnt = StringUtil.checkParseLong(element.getAsJsonObject().get("target_cnt"));
				if(xUserId.equals("")){
					logger.error("[뿌리기] 사용자 식별값의 유효성이 잘못 되었습니다. ["+xUserId+"]");
					throw new InputMismatchException("사용자 식별값의 유효성이 잘못 되었습니다.");
				}
				if(xRoomId.equals("")){
					logger.error("[뿌리기] 대화방 식별값의 유효성이 잘못 되었습니다. ["+xRoomId+"]");
					throw new InputMismatchException("대화방 식별값의 유효성이 잘못 되었습니다.");
				}
				if(amount.equals("")){
					logger.error("[뿌리기] 뿌릴 금액 값의 유효성이 잘못 되었습니다. ["+amount+"]");
					throw new InputMismatchException("뿌릴 금액 값의 유효성이 잘못 되었습니다.");
				}
				if(targetCnt.equals("")){
					logger.error("[뿌리기] 뿌릴 인원 값의 유효성이 잘못 되었습니다. ["+targetCnt+"]");
					throw new InputMismatchException("뿌릴 인원 값의 유효성이 잘못 되었습니다.");
				}
				logger.info("[STEP 1] 요청데이터 확인 완료");
				Map<String, Object> param = new HashMap<String,Object>();
				param.put("xUserId", xUserId);
				param.put("xRoomId", xRoomId);
				param.put("amount", amount);
				param.put("targetCnt", targetCnt);
				try	{
					returnToken = kaoDistributeService.distribute(param);
				}	catch (Exception e) {
					throw new Exception(e);
				}
				resultVo.setToken(returnToken);
				resultVo.setResultCd("0000");
				resultVo.setResultMsg("성공");
			
			} catch (JsonParseException e)	{
				logger.error("[뿌리기] BODY데이터가 JSON 타입이 아닙니다.");
				resultVo.setResultCd("0002");
				resultVo.setResultMsg("BODY데이터가 JSON 타입이 아닙니다. ");
			} catch (InputMismatchException e)	{
				logger.error(e.toString());
				resultVo.setResultCd("0001");
				resultVo.setResultMsg(e.getLocalizedMessage());
			} catch (Exception e) {
				logger.error(e.toString());
				resultVo.setResultCd("8000");
				resultVo.setResultMsg("서버에 에러가 발생하였습니다. 다시 시도해주세요.");
			}
			return resultVo;
		}
		@ResponseBody
		@RequestMapping(value = "/kaoDistribute/collectMoney.do")
		public ResultColVO collectMoney(@RequestBody String json, @RequestHeader HttpHeaders headers) throws Exception{
			String bodyString = json;
			String xUserId = "";
			String xRoomId = "";
			String token = "";
			long collectPrice = 0;
			ResultColVO resultVo = new ResultColVO();
			try{
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(json);
				logger.info("[STEP 1] 요청데이터 확인 시작 [Data : "+ bodyString+"]");
				xUserId = StringUtil.checkParseLong(headers.get("X-USER-ID")).trim();
				xRoomId = StringUtil.checkParseLong(headers.get("X-ROOM-ID")).trim();
				token = StringUtil.checkParseNull(element.getAsJsonObject().get("token"));
				if(xUserId.equals("")){
					logger.error("[줍기] 사용자 식별값의 유효성이 잘못 되었습니다. ["+xUserId+"]");
					throw new InputMismatchException("사용자 식별값의 유효성이 잘못 되었습니다.");
				}
				if(xRoomId.equals("")){
					logger.error("[줍기] 대화방 식별값의 유효성이 잘못 되었습니다. ["+xRoomId+"]");
					throw new InputMismatchException("대화방 식별값의 유효성이 잘못 되었습니다.");
				}
				if(token.equals("") || token.length()!=3){
					logger.error("[줍기] 토큰의 유효성이 잘못 되었습니다. ["+token+"]");
					throw new InputMismatchException("토큰의 유효성이 잘못 되었습니다.");
				}
				logger.info("[STEP 1] 요청데이터 확인 완료");
				Map<String, Object> param = new HashMap<String,Object>();
				param.put("xUserId", xUserId);
				param.put("xRoomId", xRoomId);
				param.put("token", token);
				try	{
					collectPrice = kaoDistributeService.collect(param);
				}	catch (RuntimeException e) {
					throw new RuntimeException(e.getLocalizedMessage());
				}	catch (Exception e) {
					throw new Exception(e);
				}	
				
				resultVo.setPrice(collectPrice);
				resultVo.setResultCd("0000");
				resultVo.setResultMsg("성공");
			
			} catch (JsonParseException e)	{
				logger.error("[뿌리기] BODY데이터가 JSON 타입이 아닙니다.");
				resultVo.setResultCd("0002");
				resultVo.setResultMsg("BODY데이터가 JSON 타입이 아닙니다. ");
			} catch (InputMismatchException e)	{
				logger.error(e.toString());
				resultVo.setResultCd("0001");
				resultVo.setResultMsg(e.getLocalizedMessage());
				//resultVo.setResultMsg(URLEncoder.encode(e.getLocalizedMessage(), "UTF-8"));
			}	catch (RuntimeException e) {
				logger.error(e.toString());
				resultVo.setResultCd("1003");
				resultVo.setResultMsg(e.getLocalizedMessage());
				//resultVo.setResultMsg(URLEncoder.encode(e.getLocalizedMessage(), "UTF-8"));
			}	catch (Exception e) {
				logger.error(e.toString());
				resultVo.setResultCd("8000");
				resultVo.setResultMsg("서버에 에러가 발생하였습니다. 다시 시도해주세요.");
			}
			return resultVo;
		}
		@ResponseBody
		@RequestMapping(value = "/kaoDistribute/retrieveDistribute.do")
		public ResultRetVO retrieveDistribute(@RequestBody String json, @RequestHeader HttpHeaders headers) throws Exception{
			String bodyString = json;
			String xUserId = "";
			String xRoomId = "";
			String token = "";
			double collectPrice = 0;
			ResultRetVO resultVo = new ResultRetVO();
			try{
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(json);
				logger.info("[STEP 1] 요청데이터 확인 시작 [Data : "+ bodyString+"]");
				xUserId = StringUtil.checkParseLong(headers.get("X-USER-ID")).trim();
				xRoomId = StringUtil.checkParseLong(headers.get("X-ROOM-ID")).trim();
				token = StringUtil.checkParseNull(element.getAsJsonObject().get("token"));
				if(xUserId.equals("")){
					logger.error("[줍기] 사용자 식별값의 유효성이 잘못 되었습니다. ["+xUserId+"]");
					throw new InputMismatchException("사용자 식별값의 유효성이 잘못 되었습니다.");
				}
				if(xRoomId.equals("")){
					logger.error("[줍기] 대화방 식별값의 유효성이 잘못 되었습니다. ["+xRoomId+"]");
					throw new InputMismatchException("대화방 식별값의 유효성이 잘못 되었습니다.");
				}
				if(token.equals("") || token.length()!=3){
					logger.error("[줍기] 토큰의 유효성이 잘못 되었습니다. ["+token+"]");
					throw new InputMismatchException("토큰의 유효성이 잘못 되었습니다.");
				}
				logger.info("[STEP 1] 요청데이터 확인 완료");
				Map<String, Object> param = new HashMap<String,Object>();
				param.put("xUserId", xUserId);
				param.put("xRoomId", xRoomId);
				param.put("token", token);
				try	{
					resultVo = kaoDistributeService.retrieve(param);
				}	catch (RuntimeException e) {
					resultVo = new ResultRetVO();
					throw new RuntimeException(e.getLocalizedMessage());
				}	catch (Exception e) {
					resultVo = new ResultRetVO();
					throw new Exception(e);
				}	
				resultVo.setResultCd("0000");
				resultVo.setResultMsg("성공");
			
			} catch (JsonParseException e)	{
				logger.error("[뿌리기] BODY데이터가 JSON 타입이 아닙니다.");
				resultVo.setResultCd("0002");
				resultVo.setResultMsg("BODY데이터가 JSON 타입이 아닙니다. ");
			} catch (InputMismatchException e)	{
				logger.error(e.toString());
				resultVo.setResultCd("0001");
				resultVo.setResultMsg(e.getLocalizedMessage());
				//resultVo.setResultMsg(URLEncoder.encode(e.getLocalizedMessage(), "UTF-8"));
			}	catch (RuntimeException e) {
				logger.error(e.toString());
				resultVo.setResultCd("1004");
				resultVo.setResultMsg(e.getLocalizedMessage());
				//resultVo.setResultMsg(URLEncoder.encode(e.getLocalizedMessage(), "UTF-8"));
			}	catch (Exception e) {
				logger.error(e.toString());
				resultVo.setResultCd("8000");
				resultVo.setResultMsg("서버에 에러가 발생하였습니다. 다시 시도해주세요.");
			}
			return resultVo;
		}
		
}
