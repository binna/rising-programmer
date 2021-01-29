package com.rp2.shine.src.user;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.utils.JwtService;
import com.rp2.shine.src.user.models.*;
import com.rp2.shine.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final ValidationRegex validationRegex;

    /**
     * 회원 전체 조회 API
     * [GET] /users
     * 회원 닉네임 검색 조회 API
     * [GET] /users?word=
     * @return BaseResponse<List<GetUsersRes>>
     */
    @GetMapping("") // (GET) 127.0.0.1:9000/
    public BaseResponse<List<GetUsersRes>> getUsers(@RequestParam(required = false) String word) {
        try {
            List<GetUsersRes> getUsersResList = userInfoProvider.retrieveUserInfoList(word);

            if(!getUsersResList.isEmpty() && word == null) {
                return new BaseResponse<>(SUCCESS_READ_USERS, getUsersResList);         // 회원 전체 검색
            } else if (!getUsersResList.isEmpty()) {
                return new BaseResponse<>(SUCCESS_READ_SEARCH_USERS, getUsersResList);  // 닉네임 회원 검색
            } else {
                return new BaseResponse<>(SUCCESS_NOT_SEARCH_USERS);                    // 닉네임으로 검색했는데 검색 결과 없을때
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 조회 API
     * [GET] /users/:userNo
     * @PathVariable userNo
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/{userNo}")
    public BaseResponse<GetUserRes> getUser(@PathVariable Integer userNo) {
        if (userNo == null || userNo <= 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            GetUserRes getUserRes = userInfoProvider.retrieveUserInfo(userNo);
            return new BaseResponse<>(SUCCESS_READ_USER, getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 상세 조회 API
     * [GET] /users/:userNo/detail
     * @PathVariable userNo
     * @return BaseResponse<GetDetailRes>
     */
    @ResponseBody
    @GetMapping("/{userNo}/detail")
    public BaseResponse<GetDetailRes> getDetailUser(@PathVariable Integer userNo) {
        if (userNo == null || userNo <= 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            GetDetailRes getDetailRes = userInfoProvider.retrieveDetailUserInfo(userNo);
            return new BaseResponse<>(SUCCESS_READ_USER, getDetailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    // @ResponseBody 이걸 파라미터 앞 (PostUserReq 앞)에 두면 json타입 같은거 받아오고 폼타입은 빼는 것
    // Json 같은 건 생성자만 있어도 되는데, 폼데이터의 경우엔 @Setter가 있어야 받아올 수 있음!
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> postUser(@RequestBody PostUserReq parameters) {
        if (parameters.getNickname() == null || parameters.getNickname().length() == 0) {
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        if (parameters.getPhoneNumber() == null || parameters.getPhoneNumber().length() == 0) {
            return new BaseResponse<>(EMPTY_PHONENUMBER);
        }
        if(!validationRegex.isRegexPhoneNumber(parameters.getPhoneNumber())) {
            return new BaseResponse<>(INVALID_PHONENUMBER);
        }

        try {
            PostUserRes postUserRes = userInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API
     * [PATCH] /users/:userNo/profile
     * @PathVariable userNo
     * @RequestBody PatchUserReq
     * @return BaseResponse<PatchUserRes>
     */
    @ResponseBody
    @PatchMapping("/{userNo}/profile")
    public BaseResponse<PatchUserRes> patchUser(@PathVariable Integer userNo, @RequestBody PatchUserReq parameters) {
        if (userNo == null || userNo <= 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            return new BaseResponse<>(SUCCESS_PATCH_USER, userInfoService.updateUserInfo(userNo, parameters));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그인 API
     * [POST] /users/login
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq parameters) {
        if (parameters.getPhoneNumber() == null || parameters.getPhoneNumber().length() == 0) {
            return new BaseResponse<>(EMPTY_PHONENUMBER);
        }

        try {
            PostLoginRes postLoginRes = userInfoProvider.login(parameters);
            return new BaseResponse<>(SUCCESS_LOGIN, postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /users/:userNo
     * @PathVariable userNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/{userNo}")
    public BaseResponse<Void> deleteUser(@PathVariable Integer userNo, @RequestParam(required = false) String reason) {
        if (userNo == null || userNo <= 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (reason == null || reason.isEmpty()) {
            return new BaseResponse<>(EMPTY_WITHDRAWAL_REASON);
        }

        try {
            userInfoService.deleteUserInfo(userNo, reason);
            return new BaseResponse<>(SUCCESS_DELETE_USER);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * JWT 검증 API
     * [GET] /users/jwt
     * @return BaseResponse<Void>
     */
    @GetMapping("/jwt")
    public BaseResponse<Void> jwt() {
        try {
            int userId = jwtService.getUserNo();
            userInfoProvider.retrieveUserInfo(userId);
            return new BaseResponse<>(SUCCESS_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}