package com.gelitix.backend.promoDetail.repository;

import com.gelitix.backend.promoDetail.entity.PromoDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoDetailRepository extends JpaRepository<PromoDetail, Long> {
}
