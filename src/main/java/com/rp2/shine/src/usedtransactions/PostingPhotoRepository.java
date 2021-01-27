package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.src.usedtransactions.models.SellPostingPhotoInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingPhotoRepository extends JpaRepository<SellPostingPhotoInfo, Integer> {;}
