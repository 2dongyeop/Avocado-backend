package io.wisoft.capstonedesign.domain.staff.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("select s from Staff s join fetch s.hospital h")
    List<Staff> findAllByHospital();


    /** 상세 조회 */
    @Query("select s from Staff s" +
            " join fetch s.hospital h" +
            " join s.boardReplyList br" +
            " join s.healthInfoList hi" +
            " where s.id = :id")
    Optional<Staff> findDetailById(@Param("id") final Long id);

    List<Staff> findValidateByEmail(final String email);

    Optional<Staff> findStaffByEmail(final String email);

    Optional<Staff> findByEmail(final String email);
}