package com.rp2.shine.src.user;

import com.rp2.shine.src.user.models.UsersInfo;
import com.rp2.shine.utils.JwtService;
import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.user.models.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;

    /**
     * 회원가입
     * @param postUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
        UsersInfo existsUserInfo = null;

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

        UsersInfo userInfo = new UsersInfo(nickname, phoneNumber, profilePath, profileName);

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
     * 회원 정보 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param patchUserReq
     * @return PatchUserRes
     * @throws BaseException
     */
    public PatchUserRes updateUserInfo(@NonNull Integer userNo, PatchUserReq patchUserReq) throws BaseException {
        // JWT 인증
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        UsersInfo userInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);

        // 2. 해당 UserInfo의 nickname, profilePhoto or email or phoneNumber 사용자가 입력한 값으로 설정
        if(patchUserReq.getNickname() != null && !patchUserReq.getNickname().isEmpty()) {
            userInfo.setNickname(patchUserReq.getNickname());
            userInfo.setProfilePath(patchUserReq.getProfilePath());
            userInfo.setProfileName(patchUserReq.getProfileName());
        } else if(patchUserReq.getEmail() != null && !patchUserReq.getEmail().isEmpty()) {
            userInfo.setEmail(patchUserReq.getEmail());
        } else if(patchUserReq.getPhoneNumber() != null && !patchUserReq.getPhoneNumber().isEmpty()) {
            if(!userInfoRepository.findByPhoneNumber(patchUserReq.getPhoneNumber()).isEmpty()){
                throw new BaseException(DUPLICATED_USER);
            }
            userInfo.setPhoneNumber(patchUserReq.getPhoneNumber());
        } else {
            throw new BaseException(REQUEST_ERROR);
        }

        try {
            userInfoRepository.save(userInfo);
            String nickname = userInfo.getNickname();
            String profilePath = userInfo.getProfilePath();
            String profileName = userInfo.getProfileName();
            String email = userInfo.getEmail();
            String phoneNumber = userInfo.getPhoneNumber();
            return new PatchUserRes(userNo, nickname, profilePath, profileName, email, phoneNumber);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }
    }

    /**
     * 회원 탈퇴
     * @param userNo, reason
     * @throws BaseException
     */
    public void deleteUserInfo(int userNo, String reason) throws BaseException {
        // JWT 인증
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        UsersInfo userInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);

        // 2. 해당 UserInfo의 status를 N으로 설정
        userInfo.setStatus("N");
        userInfo.setWithdrawalReason(reason);

        try {
            userInfoRepository.save(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }
}