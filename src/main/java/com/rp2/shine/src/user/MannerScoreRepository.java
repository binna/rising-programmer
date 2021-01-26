package com.rp2.shine.src.user;

import com.rp2.shine.src.user.models.MannerScoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MannerScoreRepository extends JpaRepository<MannerScoreInfo, Integer> {;}
