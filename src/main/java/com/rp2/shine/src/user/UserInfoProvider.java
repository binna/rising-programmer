package com.rp2.shine.src.user;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.utils.JwtService;
import com.rp2.shine.src.user.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.rp2.shine.config.BaseResponseStatus.*;

// 생성자 @Autowired 지운거랑 똑같은 효과
@RequiredArgsConstructor
@Service
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;

    /**
     * 전체 회원 조회
     * @return List<UserInfoRes>
     * @throws BaseException
     */
    public List<GetUsersRes> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<UsersInfo> userInfoList;
        try {
            if (word == null) {     // 전체 조회
                userInfoList = userInfoRepository.findByStatus("Y");
            } else {                // 검색 조회
                userInfoList = userInfoRepository.findByStatusAndNickname("Y", word);
            }
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. UserInfoRes로 변환하여 return
        return userInfoList.stream().map(userInfo -> {
            return new GetUsersRes(userInfo.getUserNo(),
                    userInfo.getNickname(), userInfo.getPhoneNumber(), userInfo.getProfilePath(), userInfo.getProfileName(),
                    userInfo.getCreateDate(), userInfo.getUpdateDate(), userInfo.getStatus());
            }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param userNo
     * @return UserInfoDetailRes
     * @throws BaseException
     */
    public GetUserRes retrieveUserInfo(Integer userNo) throws BaseException {
        // 1. DB에서 userId로 UserInfo 조회
        UsersInfo userInfo = retrieveUserInfoByUserNO(userNo);

        // 2. UserInfoRes로 변환하여 return
        String nickname = userInfo.getNickname();
        String phoneNumber = userInfo.getPhoneNumber();
        return new GetUserRes(userNo, nickname, phoneNumber);
    }

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        // 1. DB에서 PhoneNumber로 UserInfo 조회
        UsersInfo userInfo = retrieveUserInfoByPhoneNumber(postLoginReq.getPhoneNumber());

        // 2. Create JWT
        String jwt = jwtService.createJwt(userInfo.getUserNo());

        // 3. PostLoginRes 변환하여 return
        return new PostLoginRes(userInfo.getUserNo(), jwt);
    }

    /**
     * 회원 조회
     * @param userNo
     * @return UserInfo
     * @throws BaseException
     */
    public UsersInfo retrieveUserInfoByUserNO(Integer userNo) throws BaseException {
        // 1. DB에서 UserInfo 조회
        UsersInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userNo).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. 존재하는 회원인지 확인
        if (userInfo == null || !userInfo.getStatus().equals("Y")) {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return userInfo;
    }

    /**
     * 회원 조회
     *
     * @param phoneNumber
     * @return UserInfo
     * @throws BaseException
     */
    public UsersInfo retrieveUserInfoByPhoneNumber(String phoneNumber) throws BaseException {
        // findByPhoneNumber로 UserInfo 옵셔널을 받아오세요(그냥 있는지 없는지 모르는 값 포장해둔거)
        // 있다면 리턴하세요
        /*return userInfoRepository.findByPhoneNumber(phoneNumber)
                // 만약에 없으면 다음 예외를 던져주세요
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));*/

        // 1. phoneNumber을 이용해서 UserInfo DB 접근 =======================================
        List<UsersInfo> existsUserInfoList;
        try {
            existsUserInfoList = userInfoRepository.findByPhoneNumber(phoneNumber);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);    // 에러코드 : 3012
        }

        // 2. 존재하는 UserInfo가 있는지 확인
        UsersInfo userInfo;
        if (existsUserInfoList != null && existsUserInfoList.size() > 0) {
            userInfo = existsUserInfoList.get(0);
        } else {
            throw new BaseException(NOT_FOUND_USER);        // 에러코드 : 3010
        }

        // 3. UserInfo를 return
        return userInfo;
    }
}