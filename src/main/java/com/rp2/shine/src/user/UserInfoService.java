package com.rp2.shine.src.user;

import com.rp2.shine.src.review.ReviewService;
import com.rp2.shine.src.usedtransactions.UsedTransactionProvider;
import com.rp2.shine.src.usedtransactions.UsedTransactionService;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import com.rp2.shine.src.user.models.UserInfo;
import com.rp2.shine.utils.JwtService;
import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.user.models.*;
import com.rp2.shine.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final MannerScoreRepository mannerScoreRepository;
    private final UserInfoProvider userInfoProvider;
    private final UsedTransactionService usedTransactionService;
    private final UsedTransactionProvider usedTransactionProvider;
    private final ReviewService reviewService;
    private final JwtService jwtService;
    private final ValidationRegex validationRegex;

    /**
     * 회원가입
     * @param postUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    @Transactional
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
        UserInfo existsUserInfo = null;

        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회 > 닉네임은 중복 가능하지만 휴대폰 번호는 중복 불가
            existsUserInfo = userInfoProvider.retrieveUserInfoByPhoneNumber(postUserReq.getPhoneNumber());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER(코드 : 3011)
        if (existsUserInfo != null) {
            throw new BaseException(DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        String nickname = postUserReq.getNickname();
        String phoneNumber = postUserReq.getPhoneNumber();
        String profilePath = postUserReq.getProfilePath();
        String profileName = postUserReq.getProfileName();

        UserInfo userInfo = new UserInfo(nickname, phoneNumber, profilePath, profileName);

        // 3. 유저 정보 저장
        try {
            userInfo = userInfoRepository.save(userInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            throw new BaseException(FAILED_TO_POST_USER);
        }

        // 4. UserInfoLoginRes로 변환하여 return
        return new PostUserRes(userInfo.getUserNo());
    }

    /**
     * 회원 정보 수정
     * @param patchUserReq
     * @return PatchUserRes
     * @throws BaseException
     */
    @Transactional
    public PatchUserRes updateUserInfo(PatchUserReq patchUserReq) throws BaseException {
        // 존재하는 UserInfo가 있는지 확인 후 저장
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserNO(jwtService.getUserNo());

        // 해당 UserInfo의 nickname, profilePhoto or email or phoneNumber 사용자가 입력한 값으로 설정
        if(patchUserReq.getNickname() != null && !patchUserReq.getNickname().isEmpty()) {
            userInfo.setNickname(patchUserReq.getNickname());
            userInfo.setProfilePath(patchUserReq.getProfilePath());
            userInfo.setProfileName(patchUserReq.getProfileName());
        } else if(patchUserReq.getEmail() != null && !patchUserReq.getEmail().isEmpty()) {
            if(!validationRegex.isRegexEmail(patchUserReq.getEmail())) {
                throw new BaseException(INVALID_EMAIL);
            }
            userInfo.setEmail(patchUserReq.getEmail());
        } else if(patchUserReq.getPhoneNumber() != null && !patchUserReq.getPhoneNumber().isEmpty()) {
            if(!validationRegex.isRegexPhoneNumber(patchUserReq.getPhoneNumber())) {
                throw new BaseException(INVALID_PHONENUMBER);
            } else if(!userInfoRepository.findByPhoneNumber(patchUserReq.getPhoneNumber()).isEmpty()){
                throw new BaseException(DUPLICATED_USER);
            }
            userInfo.setPhoneNumber(patchUserReq.getPhoneNumber());
        } else {
            throw new BaseException(REQUEST_ERROR);
        }

        try {
            userInfoRepository.save(userInfo);

            return new PatchUserRes(userInfo.getUserNo(), userInfo.getNickname(), userInfo.getProfilePath(),
                    userInfo.getProfileName(), userInfo.getEmail(), userInfo.getPhoneNumber());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }
    }

    /**
     * 회원 탈퇴
     * @param reason
     * @throws BaseException
     */
    @Transactional
    public void deleteUserInfo(String reason) throws BaseException {
        // 존재하는 UserInfo가 있는지 확인 후 저장
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserNO(jwtService.getUserNo());

        // 해당 UserInfo의 status를 N으로 설정
        userInfo.setStatus("N");
        userInfo.setWithdrawalReason(reason);
        
        // 중고거래 글, 사진 삭제, 후기 삭제, 관심삭제
        List<SellPostingInfo> existSellPostingList = usedTransactionProvider.retrievePostingBySellerUserNoAndStatuY(userInfo);
        for(SellPostingInfo posting : existSellPostingList) {
            usedTransactionService.deleteUsedTransaction(posting.getPostingNo());
            reviewService.deleteSellerReview(posting.getPostingNo());
            reviewService.deleteBuyerReview(posting.getPostingNo());
            usedTransactionService.concern(posting.getPostingNo());
        }

        // 매너점수
        List<MannerScoreInfo> mannerScoreInfoList = mannerScoreRepository.findByUserNo(userInfo);
        for(MannerScoreInfo manner : mannerScoreInfoList) {
            mannerScoreRepository.delete(manner);
        }

        try {
            userInfoRepository.save(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }
}