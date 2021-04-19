# kakaoDistribute
카카오 뿌리기

[상세 개발 정보]
1. 개발 언어 : JAVA (1.8)
2. 사용 프레임 워크 : Spring (Maven, Mybatis)
3. 사용 DB : Postgresql
4. DB서버 : Amazon RDS
5. 서버 : TOMCAT 7.0 
6. 테스트방법 : POSTMAN Request
- kakaoDistribute/KAKAO/카카오뿌리기테스트시나리오.xlsx 참조
API 스펙
- kakaoDistribute/KAKAO/카카오뿌리기API정의서_v1.00.xlsx 참조
DB 명세서 
- kakaoDistribute/KAKAO/카카오뿌리기DB명세서_v1.00.xlsx 참조

[DB 설계 정보]
1. 뿌리기 원거래 정보 테이블 / 뿌리기 상세 거래 정보 테이블 생성
2. kao_dist_id(PK),kao_dist_detail_id(PK)
3. token 컬럼 index

[핵심 문제해결 전략]
1. 뿌리기 API 
  뿌리기 요청이 들어올 경우 랜덤 토큰 생성 후, 중복방지 로직처리 후
  원거래 정보를 뿌리기 원거래정보 테이블에 (1회), 
  요청 인원으로 금액을 분배하여 상세테이블에 각각(N회) 저장 (받는 사람을 null 로 저장) [금액 분배는 1/N, 잔액은 랜덤 1인에게 추가]
2. 받기 API
  받기 요청이 올경우 토큰/방 정보로 원거래 건 조회,
  자신의 거래건인지, 뿌리기 완료된건인지 확인
  유효한 거래일 경우 상세 정보 중 받는 사람이 null 인 거래건 조회
  null을 해당 요청자 로 업데이트 [조건에 요청자 = null 을 추가함으로써 동시성 처리 방지, 업데이트 횟수가 0개일 경우 중복처리이기떄문에 Exception(트랜잭션 처리로 롤백)]
  업데이트 해당 사용자로 해당 뿌리기에 금액을 조회 [결과값이 2개일 경우 중복처리이기 떄문에 Exception(트랜잭션 처리로 롤백)]
3. 조회 API
  원거래 테이블 / 상세조회 테이블을 조인하여 결과값 가져오기
  자신의 뿌리기 건, 7일 이내 건만 조회
  상세 거래정보는 List로 내려줌
  
  * 그외 상세 API 스펙은 API 정의서 문서에 첨부
  
  
