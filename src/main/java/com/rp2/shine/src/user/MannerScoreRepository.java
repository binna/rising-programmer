package com.rp2.shine.src.user;

import com.rp2.shine.src.user.models.MannerScoreInfo;
import com.rp2.shine.src.user.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MannerScoreRepository extends JpaRepository<MannerScoreInfo, Integer> {
    List<MannerScoreInfo> findByUserNo(UserInfo userNo);
}
