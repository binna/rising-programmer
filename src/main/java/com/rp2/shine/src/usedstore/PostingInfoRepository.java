package com.rp2.shine.src.usedstore;

import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingInfoRepository extends JpaRepository<SellPostingInfo, Integer> {

}