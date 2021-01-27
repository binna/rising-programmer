package com.rp2.shine.src.user;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.review.ReviewProvider;
import com.rp2.shine.src.review.models.ReviewInfo;
import com.rp2.shine.src.review.models.GetReviewRes;
import com.rp2.shine.src.usedtransactions.UsedTransactionProvider;
import com.rp2.shine.utils.JwtService;
import com.rp2.shine.src.user.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.rp2.shine.config.BaseResponseStatus.*;

// 생성자 @Autowired 지운거랑 똑같은 효과
@RequiredArgsConstructor
@Service
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
    private final MannerScoreRepository mannerScoreRepository;
    private final UsedTransactionProvider usedTransactionsProvider;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    /**
     * 전체 회원 조회, 닉네임 회원 검색 조회
     * @param word
     * @return List<GetUsersRes>
     * @throws BaseException
     * @comment param word 필수 아님, 간단한 회원정보
     */
    @Transactional
    public List<GetUsersRes> retrieveUserInfoList(String word) throws BaseException {
        List<UserInfo> userInfoList;
        try {
            if (word == null) {     // 전체 조회
                userInfoList = userInfoRepository.findByStatus("Y");
            } else {                // 검색 조회
                userInfoList = userInfoRepository.findByStatusAndNickname("Y", word);
            }
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        return userInfoList.stream().map(userInfo -> {
            return new GetUsersRes(userInfo.getNickname(), userInfo.getPhoneNumber(),
                    userInfo.getProfilePath(), userInfo.getProfileName());
            }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param userNo
     * @return GetUserRes
     * @throws BaseException
     */
    @Transactional
    public GetUserRes retrieveUserInfo(Integer userNo) throws BaseException {
        UserInfo userInfo = retrieveUserInfoByUserNO(userNo);

        return new GetUserRes(userNo, userInfo.getNickname(), userInfo.getPhoneNumber(),
                userInfo.getProfilePath(), userInfo.getProfileName(),
                userInfo.getCreateDate(), userInfo.getUpdateDate(), userInfo.getStatus());
    }

    /**
     * 회원 조회
     * @param userNo
     * @return GetDetailRes
     * @throws BaseException
     * @comment 디테일한 회원정보 -> 프로필 정보
     */
    @Transactional
    public GetDetailRes retrieveDetailUserInfo(Integer userNo) throws BaseException {
        // JWT 인증
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        // 회원 기본 정보 노출
        UserInfo userInfo = retrieveUserInfoByUserNO(userNo);

        // 매너 온도, 매너 평가 노출
        List<MannerScoreInfo> mannerScoreInfoList = retrieveMannerScoreInfoByUserNo(userInfo);
        HashMap<String, Integer> mannerScore = new HashMap<>();
        int plusCnt = 0, minusCnt = 0;
        int[] plus = new int[8];
        int[] minus = new int[8];
        String[] message = {
                "친절하고 매너가 좋아요",
                "시간 약속을 잘 지켜요",
                "상품상태가 설명한 것과 같아요",
                "상품설명이 자세해요",
                "좋은 상품을 저렴하게 판매해요",
                "응답이 빨라요",
                "제가 있는 곳까지 와서 거래했어요",
                "무료로 나눠주셨어요",
                "불친절해요",
                "시간 약속을 지키지 않아요",
                "상품상태가 설명한 것과 달라요",
                "상품 설명이 성의 없어요",
                "구매 가격보다 비싼 가격으로 판매해요",
                "너무 늦은 시간이나 새벽에 연락해요",
                "구매의사 없이 계속 찔러봐요",
                "질문해도 답이 없어요",
        };
        for (MannerScoreInfo manner : mannerScoreInfoList) {
            if(manner.getTakeManner() > 0) {
                plus[manner.getTakeManner() - 1]++;
                plusCnt++;
            } else {
                minus[manner.getTakeManner() - 1]++;
                minusCnt++;
            }
        }
        double temperature = 36.5 + (plusCnt * 0.1) - (minusCnt * 0.1);

        for(int i = 0; i < 8; i++) {
            if(plus[i] > 0) {
                mannerScore.put(message[i], plus[i]);
            }
        }
        for(int i = 0; i < 8; i++) {
            if(minus[i] > 0) {
                mannerScore.put(message[8 + i], minus[i]);
            }
        }

        // SellPosting 개수 조회
        int sellpostingCnt = usedTransactionsProvider.retrieveSellPostingInfoBySellerUserNo(userInfo).size();

        // review
        List<ReviewInfo> reviewInfoList = reviewProvider.retrieveReviewALL(userNo);
        List<GetReviewRes> reviews = new ArrayList<>();

        for (ReviewInfo review : reviewInfoList) {
            reviews.add(new GetReviewRes(review.getWriter(), review.getFileName(), review.getFilePath(), review.getContent(), review.getCreateDate()));
        }

        return new GetDetailRes(userInfo.getUserNo(), userInfo.getNickname(), userInfo.getPhoneNumber(),
                userInfo.getProfilePath(), userInfo.getProfileName(),
                userInfo.getCreateDate(), userInfo.getUpdateDate(), userInfo.getStatus(),
                sellpostingCnt, temperature, mannerScore, reviews);
    }

    /**
     * 회원별 매너 점수 검색
     * @param userNo
     * @return List<MannerScoreInfo>
     * @throws BaseException
     */
    @Transactional
    public List<MannerScoreInfo> retrieveMannerScoreInfoByUserNo(UserInfo userNo) throws BaseException {
        List<MannerScoreInfo> mannerScoreInfoList;
        try {
            mannerScoreInfoList = mannerScoreRepository.findByUserNo(userNo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_MANNERSCORE);
        }
        return mannerScoreInfoList;
    }

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    @Transactional
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        // DB에서 PhoneNumber로 UserInfo 조회
        UserInfo userInfo = retrieveUserInfoByPhoneNumber(postLoginReq.getPhoneNumber());

        // Create JWT
        String jwt = jwtService.createJwt(userInfo.getUserNo());

        // PostLoginRes 변환하여 return
        return new PostLoginRes(userInfo.getUserNo(), jwt);
    }

    /**
     * 회원 조회
     * @param userNo
     * @return UserInfo
     * @throws BaseException
     * @comment UserNo로 회원조회
     */
    @Transactional
    public UserInfo retrieveUserInfoByUserNO(Integer userNo) throws BaseException {
        // DB에서 UserInfo 조회
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userNo).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 존재하는 회원인지 확인
        if (userInfo == null || !userInfo.getStatus().equals("Y")) {
            throw new BaseException(NOT_FOUND_USER);
        }

        // UserInfo를 return
        return userInfo;
    }

    /**
     * 회원 조회
     * @param phoneNumber
     * @return UsersInfo
     * @throws BaseException
     * @comment phoneNumber로 회원조회
     */
    @Transactional
    public UserInfo retrieveUserInfoByPhoneNumber(String phoneNumber) throws BaseException {
        // findByPhoneNumber로 UserInfo 옵셔널을 받아오세요(그냥 있는지 없는지 모르는 값 포장해둔거)
        // 있다면 리턴하세요
        /*return userInfoRepository.findByPhoneNumber(phoneNumber)
                // 만약에 없으면 다음 예외를 던져주세요
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));*/

        // phoneNumber을 이용해서 UserInfo DB 접근 =======================================
        List<UserInfo> existsUserInfoList;
        try {
            existsUserInfoList = userInfoRepository.findByPhoneNumber(phoneNumber);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 존재하는 UserInfo가 있는지 확인
        UserInfo userInfo;
        if (existsUserInfoList != null && existsUserInfoList.size() > 0) {
            userInfo = existsUserInfoList.get(0);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }

        // UserInfo를 return
        return userInfo;
    }
}