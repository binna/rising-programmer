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
     * @param parameters
     * @return PostUsedTransactionsRes
     * @throws BaseException
     */
    @Transactional
    public PostUsedTransactionsRes createUsedTransactions(PostUsedTransactionsReq parameters) throws BaseException {
        // 중고거래 글 생성
        UserInfo userInfo = userInfoRepository.findById(jwtService.getUserNo()).orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
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
     * @param postingNo, patchUsedReq
     * @return PatchUsedTransactionRes
     * @throws BaseException
     */
    @Transactional
    public PatchUsedTransactionRes updateUsedTransaction(Integer postingNo, PatchUsedTransactionReq patchUsedReq) throws BaseException {
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
        if(!sellPostingInfo.getSellerUserNo().getUserNo().equals(jwtService.getUserNo())) {
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
     * 중고거래 글 판매완료 처리
     * @param postingNo, buyerNo
     * @throws BaseException
     */
    @Transactional
    public void patchUsedTransactionSalesCompleted(Integer postingNo, Integer buyerNo) throws BaseException {
        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserNO(buyerNo);
        if(!sellPostingInfo.getSellerUserNo().getUserNo().equals(jwtService.getUserNo())) {
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
     * @param postingNo
     * @return PostConcernRes
     * @throws BaseException
     */
    @Transactional
    public PostConcernRes concern(Integer postingNo) throws BaseException {
        UserInfo usersInfo = userInfoProvider.retrieveUserInfoByUserNO(jwtService.getUserNo());
        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        List<SellPostingConcernInfo> existsConcernInfo = usedTransactionProvider.retrieveConcernByConcernUserNoAndPostingNo(usersInfo, sellPostingInfo);

        if(sellPostingInfo.getStatus().equals("N")) {
            throw new BaseException(ALREADY_DELETE_POSTING);
        }

        if(sellPostingInfo.getSellerUserNo().getUserNo() == jwtService.getUserNo()) {
            throw new BaseException(DO_NOT_WRITER);
        }

        // 관심 존재하면 삭제
        if(!existsConcernInfo.isEmpty()) {
            try {
                for(SellPostingConcernInfo concern : existsConcernInfo) {
                    postConcernsRepository.delete(concern);
                }
            } catch (Exception exception) {
                throw new BaseException(FAILED_TO_DELETE_CONSERN);
            }
        }

        // 관심 존재하지 않는다면 생성
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
}