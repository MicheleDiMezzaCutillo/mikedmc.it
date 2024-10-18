package it.mikedmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.Coupon;
import it.mikedmc.model.CouponRole;

public interface CouponRoleRepository extends JpaRepository<CouponRole,Long> {

}
