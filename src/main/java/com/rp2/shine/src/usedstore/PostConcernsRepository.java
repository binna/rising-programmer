package com.rp2.shine.src.usedstore;

import com.rp2.shine.src.usedstore.models.SellPostingConsernInfo;
import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import com.rp2.shine.src.user.models.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostConcernsRepository extends JpaRepository<SellPostingConsernInfo, Integer> {
    List<SellPostingConsernInfo> findByConcernUserNoAndPostingNo(UsersInfo concernUserNo, SellPostingInfo PostingInfo);
}
