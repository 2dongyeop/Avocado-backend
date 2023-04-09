package io.wisoft.capstonedesign.domain.pick.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {


    /**
     * 특정 작성자의 찜한 목록 페이징 조회
     */
    @Query(value = "select p from Pick p" +
            " join fetch p.member m" +
            " join fetch p.hospital h" +
            " where m.id = :id",
    countQuery = "select count(p) from Pick p")
    Page<Pick> findByMemberIdUsingPaging(@Param("id") final Long memberId, final Pageable pageable);


    /** 상세 조회 */
    @Query("select p from Pick p" +
            " join fetch p.member m" +
            " join fetch p.hospital h" +
            " where p.id = :id")
    Optional<Pick> findDetailById(@Param("id") final Long id);

}
