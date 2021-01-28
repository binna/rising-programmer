package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.common.PhotoReq;
import com.rp2.shine.src.common.PhotoRes;
import com.rp2.shine.src.review.ReviewProvider;
import com.rp2.shine.src.review.ReviewRepository;
import com.rp2.shine.src.review.models.ReviewInfo;
import com.rp2.shine.src.usedtransactions.models.*;
import com.rp2.shine.src.user.UserInfoProvider;
import com.rp2.shine.src.user.UserInfoRepository;
import com.rp2.shine.src.user.models.UserInfo;
import com.rp2.shine.utils.JwtService;
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
    private final UserInfoRepository userInfoRepository;
    private final PostingInfoRepository postingInfoRepository;
    private final PostConcernRepository postConcernsRepository;
    private final ReviewRepository reviewRepository;
    private final UserInfoProvider userInfoProvider;
    private final UsedTransactionProvider usedTransactionProvider;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    /**
     * 중고거래 글 생성
     * @param userNo, parameters
     * @return PostUsedTransactionsRes
     * @throws BaseException
     */
    @Transactional
    public PostUsedTransactionsRes createUsedTransactions(Integer userNo, PostUsedTransactionsReq parameters) throws BaseException {
        // JWT 인증
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        // 중고거래 글 생성
        UserInfo userInfo = userInfoRepository.findById(userNo).orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
        List<SellPostingPhotoInfo> sellPostingPhotoInfoList = new ArrayList<>();
        if(parameters.getPostingPhoto() != null) {
            for (PhotoReq photo : parameters.getPostingPhoto()) {
                System.out.println(photo.toString());
                SellPostingPhotoInfo sellPostingPhotoInfo = new SellPostingPhotoInfo(photo.getFilePath(), photo.getFileName());
                sellPostingPhotoInfoList.add(sellPostingPhotoInfo);
            }
        }

        SellPostingInfo sellPostingInfo = new SellPostingInfo(userInfo, parameters.getTitle(),
                parameters.getContent(), parameters.getCategory(), parameters.getPrice());
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
     * @param postingNo, userNo, patchUsedReq
     * @return PatchUsedTransactionRes
     * @throws BaseException
     */
    @Transactional
    public PatchUsedTransactionRes updateUsedTransaction(Integer postingNo, Integer userNo, PatchUsedTransactionReq patchUsedReq) throws BaseException {
        // JWT 인증
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        // 존재하는 포스팅, 사진 확인
        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        if(sellPostingInfo.getStatus().equals("Y")) {
            sellPostingInfo.setTitle(patchUsedReq.getTitle());
            sellPostingInfo.setContent(patchUsedReq.getContent());
            sellPostingInfo.setCategory(patchUsedReq.getCategory());
            sellPostingInfo.setPrice(patchUsedReq.getPrice());
        } else {    // 이미 삭제한 포스팅
            throw new BaseException(ALREADY_DELETE_POSTING);
        }

        // 글 포스팅 작성자와 현재 로그인한 사람이 일치해야 함
        if(!sellPostingInfo.getSellerUserNo().getUserNo().equals(userNo)) {
            throw new BaseException(DO_NOT_MATCH_USERNO);
        }

        List<SellPostingPhotoInfo> newPhotoData = new ArrayList<>();
        if(patchUsedReq.getPostingPhoto() != null) {
            for (PhotoReq photo : patchUsedReq.getPostingPhoto()) {
                SellPostingPhotoInfo sellPostingPhotoInfo = new SellPostingPhotoInfo(photo.getFilePath(), photo.getFileName());
                sellPostingPhotoInfo.setSellPostingInfo(sellPostingInfo);
                newPhotoData.add(sellPostingPhotoInfo);
            }
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
     * 중고거래 글 수정
     * @param postingNo, userNo, buyerNo
     * @throws BaseException
     */
    @Transactional
    public void patchUsedTransactionSalesCompleted(Integer postingNo, Integer userNo, Integer buyerNo) throws BaseException {
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserNO(buyerNo);
        if(!sellPostingInfo.getSellerUserNo().getUserNo().equals(userNo)) {
            throw new BaseException(DO_NOT_MATCH_USERNO);
        }
        if (sellPostingInfo.getStatus().equals("Y")) {
            if(userInfo.getUserNo() == sellPostingInfo.getSellerUserNo().getUserNo()) {
                throw new BaseException(DO_NOT_MATCH_BUYER);
            } else {
                sellPostingInfo.setStatus("B");
                sellPostingInfo.setBuyerUserNo(userInfo);
            }
        } else if(sellPostingInfo.getStatus().equals("B")) {
            throw new BaseException(ALREADY_SALES_COMPLETED);
        } else if(sellPostingInfo.getStatus().equals("N")) {
            throw new BaseException(ALREADY_DELETE_POSTING);
        }

        try {
           // postingInfoRepository.save(sellPostingInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();
            throw new BaseException(FAILED_TO_PATCH_POSTING);
        }

    }

    /**
     * 중고거래 글 삭제
     * @param postingNo, userNo
     * @throws BaseException
     */
    @Transactional
    public void deleteUsedTransaction(Integer postingNo, Integer userNo) throws BaseException {
        // JWT 인증
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        // 존재하는 포스팅, 사진확인, 관심확인, 리뷰확인
        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        List<SellPostingConcernInfo> sellPostingConcernInfoList = usedTransactionProvider.retrieveConcernByPostingNo(sellPostingInfo);
        List<ReviewInfo> reviewInfoList = reviewProvider.retrieveReviewByPostingNo(sellPostingInfo);

        // 글 포스팅 작성자와 현재 로그인한 사람이 일치해야 함
        if(!sellPostingInfo.getSellerUserNo().getUserNo().equals(userNo)) {
            throw new BaseException(DO_NOT_MATCH_USERNO);
        }

        // 유효한 포스팅, 사진 : status 전부 N으로 변경
        if(sellPostingInfo.getStatus().equals("Y")) {
            List<SellPostingPhotoInfo> sellPostingPhotoInfoList = sellPostingInfo.getSellPostingPhotoInfoList();

            for (SellPostingPhotoInfo photo : sellPostingPhotoInfoList) {
                photo.setStatus("N");
            }
            for (SellPostingConcernInfo concern : sellPostingConcernInfoList) {
                concern.setStatus("N");
            }
            for (ReviewInfo review : reviewInfoList) {
                review.setStatus("N");
            }
            sellPostingInfo.setStatus("N");
        } else {    // 이미 삭제한 포스팅
            throw new BaseException(ALREADY_DELETE_POSTING);
        }

        try {
            postingInfoRepository.save(sellPostingInfo);
            for (SellPostingConcernInfo concern : sellPostingConcernInfoList) {
                postConcernsRepository.save(concern);
            }
            for (ReviewInfo review : reviewInfoList) {
                reviewRepository.save(review);
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            throw new BaseException(FAILED_TO_DELETE_POSTING);
        }
    }

    /**
     * 중고거래 관심 등록
     * @param postingNo, userNo
     * @return PostConcernRes
     * @throws BaseException
     */
    @Transactional
    public PostConcernRes createConcern(Integer postingNo, Integer userNo) throws BaseException {
        // JWT 인증
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        UserInfo usersInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);
        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        List<SellPostingConcernInfo> existsConcernInfo = usedTransactionProvider.retrieveConcernByConcernUserNoAndPostingNo(usersInfo, sellPostingInfo);

        if(sellPostingInfo.getStatus().equals("N")) {
            throw new BaseException(ALREADY_DELETE_POSTING);
        }
        if(!existsConcernInfo.isEmpty()) {
            throw new BaseException(DUPLICATED_CONCERN);
        }
        if(sellPostingInfo.getSellerUserNo().getUserNo() == userNo) {
            throw new BaseException(DO_NOT_WRITER);
        }

        SellPostingConcernInfo sellPostingConcernInfo = new SellPostingConcernInfo(usersInfo, sellPostingInfo);
        try {
            postConcernsRepository.save(sellPostingConcernInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();
            throw new BaseException(FAILED_TO_POST_CONSERN);
        }

        return new PostConcernRes(sellPostingConcernInfo.getPostingNo().getPostingNo(),
                sellPostingConcernInfo.getConcernUserNo().getUserNo(), sellPostingConcernInfo.getConcernNo());
    }

    /**
     * 중고거래 관심 삭제
     * @param postingNo, userNo
     * @throws BaseException
     */
    @Transactional
    public void deleteConcern(Integer postingNo, Integer userNo) throws BaseException {
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        UserInfo usersInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);
        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        List<SellPostingConcernInfo> sellPostingConcernInfoList = usedTransactionProvider.retrieveConcernByConcernUserNoAndPostingNo(usersInfo, sellPostingInfo);

        if(sellPostingInfo.getStatus().equals("N")) {
            throw new BaseException(ALREADY_DELETE_POSTING);
        }
        int cnt = 0;
        if(sellPostingConcernInfoList.isEmpty()) {
            throw new BaseException(ALREADY_DELETE_CONCERN);
        }
        for(SellPostingConcernInfo concern : sellPostingConcernInfoList) {
            if(concern.getConcernUserNo().getUserNo().equals(userNo)) {
                cnt++;
            }
        }
        if(cnt == 0) {
            throw new BaseException(ALREADY_DELETE_CONCERN);
        }

        try {
            for(SellPostingConcernInfo concern : sellPostingConcernInfoList) {
                postConcernsRepository.delete(concern);
            }
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_DELETE_CONSERN);
        }
    }
}