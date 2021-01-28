package com.rp2.shine.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    // 1000 : 요청 성공
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    SUCCESS_READ_USERS(true, 1010, "회원 전체 정보 조회에 성공하였습니다."),
    SUCCESS_READ_USER(true, 1011, "회원 정보 조회에 성공하였습니다."),
    SUCCESS_POST_USER(true, 1012, "회원가입에 성공하였습니다."),
    SUCCESS_LOGIN(true, 1013, "로그인에 성공하였습니다."),
    SUCCESS_JWT(true, 1014, "JWT 검증에 성공하였습니다."),
    SUCCESS_DELETE_USER(true, 1015, "회원 탈퇴에 성공하였습니다."),
    SUCCESS_PATCH_USER(true, 1016, "회원정보 수정에 성공하였습니다."),
    SUCCESS_READ_SEARCH_USERS(true, 1017, "회원 검색 조회에 성공하였습니다."),
    SUCCESS_NOT_SEARCH_USERS(true, 1018, "검색한 회원이 존재하지 않습니다."),
    SUCCESS_POST_POSTING(true, 1019, "중고거래 글이 등록 성공되었습니다."),
    SUCCESS_DELETE_POSTING(true, 1020, "중고거래 글이 삭제 성공하었습니다."),
    SUCCESS_PATCH_POSTING(true, 1021, "중고거래 글이 수정 성공하었습니다."),
    SUCCESS_POST_CONCERN(true, 1022, "관심 등록이 성공되었습니다."),
    SUCCESS_DELETE_CONCERN(true, 1023, "관심 삭제가 성공되었습니다."),
    SUCCESS_POST_REVIEW(true, 1024, "후기 등록이 성공되었습니다."),
    SUCCESS_DELETE_REVIEW(true, 1025, "후기 등록이 삭제되었습니다."),
    SUCCESS_SALES_COMPLETED(true, 1026, "중고거래 글의 판매 완료처리가 성공하였습니다."),

    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_USERNO(false, 2001, "회원번호를 확인해주세요."),
    EMPTY_SELLERUSERNO(false, 2002, "판매자를 확인해주세요."),
    EMPTY_BUYERUSERNO(false, 2003, "구매자를 확인해주세요."),
    EMPTY_POSTINGNO(false, 2004, "게시글 번호를 확인해주세요."),
    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2011, "유효하지 않은 JWT입니다."),
    EMPTY_NICKNAME(false, 2020, "닉네임을 입력해주세요."),
    EMPTY_PHONENUMBER(false, 2030, "휴대전화번호를 입력해주세요."),
    EMPTY_WITHDRAWAL_REASON(false, 2040, "탈퇴 이유를 입력해주세요."),
    EMPTY_TITLE(false, 2050, "제목을 입력해주세요."),
    EMPTY_CONTENT(false, 2060, "내용을 입력해주세요."),
    EMPTY_CATEGORY(false, 2070, "카테고리를 입력해주세요."),
    EMPTY_PRICE(false, 2080, "금액을 입력해주세요."),
    EMPTY_MANNERSCORE(false, 2090, "매너 점수를 선택해주세요."),
    DO_NOT_MATCH_USERNO(false, 2100, "현재 로그인한 사용자와 작성자가 일치하지 않습니다."),
    DO_NOT_WRITER(false, 2101, "작성자는 관심등록을 할 수 없습니다."),
    DO_NOT_MATCH_BUYER(false, 2102, "작성자와 구매자는 같을 수 없습니다."),
    DO_NOT_SALES_COMPLETED(false, 2103, "아직 판매가 완료되지 않았습니다."),

    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    NOT_FOUND_USER(false, 3010, "존재하지 않는 회원입니다."),
    DUPLICATED_USER(false, 3011, "이미 존재하는 회원입니다."),
    FAILED_TO_GET_USER(false, 3012, "회원 정보 조회에 실패하였습니다."),
    FAILED_TO_POST_USER(false, 3013, "회원가입에 실패하였습니다."),
    FAILED_TO_LOGIN(false, 3014, "로그인에 실패하였습니다."),
    FAILED_TO_DELETE_USER(false, 3015, "회원 탈퇴에 실패하였습니다."),
    FAILED_TO_PATCH_USER(false, 3016, "개인정보 수정에 실패하였습니다."),
    FAILED_TO_POST_POSTING(false, 3017, "중고거래 글 등록이 실패하였습니다."),
    FAILED_TO_DELETE_POSTING(false, 3017, "중고거래 글 삭제가 실패하였습니다."),
    FAILED_TO_PATCH_POSTING(false, 3018, "중고거래 글 수정이 실패하였습니다."),
    FAILED_TO_CONSERN(false, 3019, "관심등록 조회에 실패하였습니다."),
    FAILED_TO_POST_CONSERN(false, 3020, "관심등록 등록에 실패하였습니다."),
    DUPLICATED_CONCERN(false, 3021, "이미 관심등록이 되었습니다."),
    FAILED_TO_DELETE_CONSERN(false, 3022, "관심등록 실패하였습니다."),
    FAILED_TO_POST_REVIEW(false, 3022, "후기 등록이 실패하였습니다."),
    FAILED_TO_DELETE_REVIEW(false, 3023, "후기 삭제 실패하였습니다."),
    FAILED_TO_GET_MANNERSCORE(false, 3024, "매너 점수 조회에 실패하였습니다."),
    FAILED_TO_GET_REVIEW(false, 3025, "후기 검색에 실패하였습니다."),
    FAILED_TO_GET_POSTING(false, 3026, "중고거래 글 조회에 실패하였습니다."),
    ALREADY_DELETE_CONCERN(false, 3027, "이미 삭제되었거나 존재하지 않는 관심입니다."),
    ALREADY_DELETE_POSTING(false, 3028, "이미 삭제되었거나 존재하지 않는 중고 판매글입니다."),
    FAILED_SALES_COMPLETED(false, 3029, "중고거래 글의 판매 완료처리가 실패하였습니다."),
    ALREADY_SALES_COMPLETED(false, 3030, "이미 판매 완료처리된 중고 판매글입니다."),
    ALREADY_REVIEW(false, 3031, "이미 후기를 작성하셨습니다."),

    // 4000 : Database 오류
    SERVER_ERROR(false, 4000, "서버와의 통신에 실패하였습니다."),
    DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

//EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
//INVALID_EMAIL(false, 2021, "이메일 형식을 확인해주세요."),
//EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
//EMPTY_CONFIRM_PASSWORD(false, 2031, "비밀번호 확인을 입력해주세요."),
//WRONG_PASSWORD(false, 2032, "비밀번호를 다시 입력해주세요."),
//DO_NOT_MATCH_PASSWORD(false, 2033, "비밀번호와 비밀번호확인 값이 일치하지 않습니다."),
