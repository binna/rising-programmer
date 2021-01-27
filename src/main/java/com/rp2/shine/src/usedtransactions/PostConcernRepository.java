package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.src.usedtransactions.models.SellPostingConcernInfo;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import com.rp2.shine.src.user.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostConcernRepository extends JpaRepository<SellPostingConcernInfo, Integer> {
    List<SellPostingConcernInfo> findByConcernUserNoAndPostingNo(UserInfo concernUserNo, SellPostingInfo PostingInfo);
    List<SellPostingConcernInfo> findByPostingNo(SellPostingInfo sellPostingInfo);
}
