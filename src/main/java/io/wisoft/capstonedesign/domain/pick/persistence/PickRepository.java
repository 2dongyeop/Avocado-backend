package io.wisoft.capstonedesign.domain.pick.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {


    /**
     * 특정 작성자의 찜한 목록 내림차순 조회
     */
    @Query("select p from Pick p join fetch p.member m where m.id = :id order by p.createAt desc")
    List<Pick> findByMemberIdOrderByCreateAtDesc(@Param("id") final Long memberId);

    /**
     * 특정 작성자의 찜한 목록 오름차순 조회
     */
    @Query("select p from Pick p join fetch p.member m where m.id = :id order by p.createAt asc")
    List<Pick> findByMemberIdOrderByCreateAtAsc(@Param("id") final Long memberId);
}
