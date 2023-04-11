package io.wisoft.capstonedesign.domain.pick.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {

    /** 상세 조회 */
    @Query("select p from Pick p" +
            " join fetch p.member m" +
            " join fetch p.hospital h" +
            " where p.id = :id")
    Optional<Pick> findDetailById(@Param("id") final Long id);

}
