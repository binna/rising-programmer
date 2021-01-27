package com.rp2.shine.src.review;

import com.rp2.shine.src.review.models.ReviewInfo;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewInfo, Integer> {
    List<ReviewInfo> findBySellPostingInfoAndWriterAndDivisionAndStatus(SellPostingInfo sellPostingInfo, Integer writer, String division, String status);
    List<ReviewInfo> findByWriterAndStatusOrderByCreateDateDesc(Integer writer, String status);
    List<ReviewInfo> findBySellPostingInfo(SellPostingInfo sellPostingInfo);
}
