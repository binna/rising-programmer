package com.rp2.shine.src.review;

import com.rp2.shine.src.review.models.SellerReviewInfo;
import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerReviewRepository extends JpaRepository<SellerReviewInfo, Integer> {
    List<SellerReviewInfo> findBySellPostingInfoAndSeller(SellPostingInfo sellPostingInfo, Integer seller);
}
