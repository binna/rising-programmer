package com.rp2.shine.src.usedstore;

import com.rp2.shine.src.usedstore.models.SellPostingPhotoInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingPhotoRepository extends JpaRepository<SellPostingPhotoInfo, Integer> {;}
