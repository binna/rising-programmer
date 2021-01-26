package com.rp2.shine.src.review;

import com.rp2.shine.src.review.models.BuyerReviewInfo;
import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyerReviewRepository extends JpaRepository<BuyerReviewInfo, Integer> {
    List<BuyerReviewInfo> findBySellPostingInfoAndAndBuyer(SellPostingInfo sellPostingInfo, Integer buyeer);
}
