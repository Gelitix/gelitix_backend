package com.gelitix.backend.promoDetail.repository;

import com.gelitix.backend.promoDetail.entity.PromoDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoDetailRepository extends JpaRepository<PromoDetail, Long> {
    List<PromoDetail> findPromoDetailsByEventId(Long eventId);

    List<PromoDetail> findPromoDetailByIsReferralAndEventId (Boolean isReferral, Long eventId);
}
