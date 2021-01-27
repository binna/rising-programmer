package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.common.PhotoReq;
import com.rp2.shine.src.common.PhotoRes;
import com.rp2.shine.src.usedtransactions.models.*;
import com.rp2.shine.src.user.UserInfoProvider;
import com.rp2.shine.src.user.UserInfoRepository;
import com.rp2.shine.src.user.models.UserInfo;
import com.rp2.shine.utils.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UsedTransactionService {
    private final PostingInfoRepository postingInfoRepository;
    private final PostConcernRepository postConcernsRepository;
    private final UserInfoRepository userInfoRepository;
    private final UsedTransactionProvider usedStoreProvider;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;

    /**
     * 중고거래 글 생성
     * @param postUsedStoreReq
     * @return PostUsedStoreRes
     * @throws BaseException
     */
    @Transactional
    public PostUsedTransactionsRes createUsedTransactions(Integer sellerUserNo, PostUsedTransactionsReq postUsedStoreReq) throws BaseException {
        // TODO JWT 인증

        // 중고거래 글 생성
        UserInfo userInfo = userInfoRepository.findById(sellerUserNo).orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
        List<SellPostingPhotoInfo> sellPostingPhotoInfoList = new ArrayList<>();
        for(PhotoReq photo : postUsedStoreReq.getSellPostingPhoto()) {
            SellPostingPhotoInfo sellPostingPhotoInfo = new SellPostingPhotoInfo(photo.getFilePath(), photo.getFileName());
            sellPostingPhotoInfoList.add(sellPostingPhotoInfo);
        }

        SellPostingInfo sellPostingInfo = new SellPostingInfo(userInfo, postUsedStoreReq.getTitle(),
                postUsedStoreReq.getContent(), postUsedStoreReq.getCategory(), postUsedStoreReq.getPrice());
        sellPostingInfo.setSellPostingPhotoInfo(sellPostingPhotoInfoList);

        // 중고거래 글 등록
        try {
            sellPostingInfo = postingInfoRepository.save(sellPostingInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            throw new BaseException(FAILED_TO_POST_POSTING);
        }

        // UserInfoLoginRes로 변환하여 return
        List<PhotoRes> postPhotoResList = sellPostingInfo.getSellPostingPhotoInfoList().stream()
                .map(sellPostingPhotoInfo -> new PhotoRes(sellPostingPhotoInfo.getPhotoNo(),
                        sellPostingPhotoInfo.getFilePath(), sellPostingPhotoInfo.getFileName(), sellPostingPhotoInfo.getStatus()))
                .collect(Collectors.toList());

        return new PostUsedTransactionsRes(sellPostingInfo.getPostingNo(), userInfo.getUserNo(),
                sellPostingInfo.getTitle(), sellPostingInfo.getContent(), sellPostingInfo.getCategory(),
                sellPostingInfo.getCreateDate(), sellPostingInfo.getUpdateDate(), sellPostingInfo.getStatus(),
                sellPostingInfo.getPrice(), postPhotoResList);
    }

    /**
     * 중고거래 글 수정
     * @param patchUsedReq
     * @return PatchUsedStoreRes
     * @throws BaseException
     */
    @Transactional
    public PatchUsedTransactionRes updateUsedStoreInfo(@NonNull Integer sellerUserNo, @NonNull Integer sellPostingNo, PatchUsedTransactionReq patchUsedReq) throws BaseException {
        // TODO JWT 인증

        // 존재하는 포스팅, 사진 확인
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(sellPostingNo);
        //System.out.println(sellPostingInfo.toString());

        if(sellPostingInfo.getStatus().equals("Y")) {
            sellPostingInfo.setTitle(patchUsedReq.getTitle());
            sellPostingInfo.setContent(patchUsedReq.getContent());
            sellPostingInfo.setCategory(patchUsedReq.getCategory());
            sellPostingInfo.setPrice(patchUsedReq.getPrice());
        } else {    // 이미 삭제한 포스팅
            throw new BaseException(EMPTY_USEDSTORE);
        }

        List<SellPostingPhotoInfo> newPhotoData = new ArrayList<>();
        for(PhotoReq photo : patchUsedReq.getSellPostingPhoto()) {
            SellPostingPhotoInfo sellPostingPhotoInfo = new SellPostingPhotoInfo(photo.getFilePath(), photo.getFileName());
            sellPostingPhotoInfo.setSellPostingInfo(sellPostingInfo);
            newPhotoData.add(sellPostingPhotoInfo);
        }
        sellPostingInfo.getSellPostingPhotoInfoList().clear();
        sellPostingInfo.getSellPostingPhotoInfoList().addAll(newPhotoData);

        try {
            postingInfoRepository.save(sellPostingInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();
            throw new BaseException(FAILED_TO_PATCH_POSTING);
        }

        List<PhotoRes> postPhotoResList = new ArrayList<>();
        for (SellPostingPhotoInfo photo : sellPostingInfo.getSellPostingPhotoInfoList()) {
            postPhotoResList.add(new PhotoRes(photo.getPhotoNo(), photo.getFilePath(), photo.getFileName(), photo.getStatus()));
        }
        return new PatchUsedTransactionRes(sellPostingInfo.getPostingNo(), sellPostingInfo.getPostingNo(),
                sellPostingInfo.getTitle(), sellPostingInfo.getContent(), sellPostingInfo.getCategory(),
                sellPostingInfo.getCreateDate(), sellPostingInfo.getUpdateDate(), sellPostingInfo.getStatus(),
                sellPostingInfo.getPrice(), postPhotoResList);
    }

    /**
     * 중고거래 글 삭제
     * @param sellPostingNo
     * @throws BaseException
     */
    @Transactional
    public void deleteUsedStore(Integer sellPostingNo) throws BaseException {
        // TODO JWT 인증

        // 존재하는 포스팅, 사진 확인
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(sellPostingNo);

        System.out.println(sellPostingInfo.toString());

        // 유효한 포스팅, 사진 : status 전부 N으로 변경
        if(sellPostingInfo.getStatus().equals("Y")) {
            List<SellPostingPhotoInfo> sellPostingPhotoInfoList = sellPostingInfo.getSellPostingPhotoInfoList();

            for (SellPostingPhotoInfo photo : sellPostingPhotoInfoList) {
                photo.setStatus("N");
            }
            sellPostingInfo.setStatus("N");
        } else {    // 이미 삭제한 포스팅
            throw new BaseException(EMPTY_USEDSTORE);
        }

        try {
            postingInfoRepository.save(sellPostingInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();
            throw new BaseException(FAILED_TO_DELETE_POSTING);
        }
    }

    /**
     * 중고거래 관심 등록
     * @param potingNo, userNo
     * @return PostConcernRes
     * @throws BaseException
     */
    @Transactional
    public PostConcernRes createConcern(Integer potingNo, Integer userNo) throws BaseException {
        // TODO JWT 인증

        SellPostingConsernInfo existsConsernInfo = null;
        UserInfo usersInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(potingNo);
        try {
            existsConsernInfo = usedStoreProvider.retrieveSellPostingConsernInfoByConcernUserNo(usersInfo, sellPostingInfo);
        } catch (BaseException exception) {
            if (exception.getStatus() != EMPTY_USEDSTORE) {
                throw exception;
            }
        }

        if(existsConsernInfo != null) {
            throw new BaseException(DUPLICATED_CONCERN);
        }

        SellPostingConsernInfo sellPostingConsernInfo = new SellPostingConsernInfo(usersInfo, sellPostingInfo);
        try {
            postConcernsRepository.save(sellPostingConsernInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();
            throw new BaseException(FAILED_TO_POST_CONSERN);
        }

        return new PostConcernRes(sellPostingConsernInfo.getPostingNo().getPostingNo(),
                sellPostingConsernInfo.getConcernUserNo().getUserNo(), sellPostingConsernInfo.getConcernNo());
    }

    /**
     * 중고거래 관심 삭제
     * @param potingNo, userNo
     * @throws BaseException
     */
    @Transactional
    public void deleteConcern(Integer potingNo, Integer userNo) throws BaseException {
        // TODO JWT 인증

        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        UserInfo usersInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(potingNo);
        SellPostingConsernInfo sellPostingConsernInfo = usedStoreProvider.retrieveSellPostingConsernInfoByConcernUserNo(usersInfo, sellPostingInfo);

        try {
            postConcernsRepository.delete(sellPostingConsernInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_DELETE_CONSERN);
        }

    }
}