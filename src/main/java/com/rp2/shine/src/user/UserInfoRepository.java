package com.rp2.shine.src.user;

import com.rp2.shine.src.user.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    // find By 뒤에 컬럼명을 붙여주면 이를 이용한 검색이 가능
    // Optional : 검색한 게 하나일때 사용, List : 여러개 검색할 때 사용
    //Optional<UserInfo> findByPhoneNumber(String phoneNumber);
    List<UserInfo> findByPhoneNumber(String phoneNumber);
    List<UserInfo> findByStatus(String status);
    List<UserInfo> findByStatusAndNickname(String status, String word);
}