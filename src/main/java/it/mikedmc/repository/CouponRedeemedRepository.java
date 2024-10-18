package it.mikedmc.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.mikedmc.model.Coupon;
import it.mikedmc.model.CouponRedeemed;
import it.mikedmc.model.User;

public interface CouponRedeemedRepository extends JpaRepository<CouponRedeemed,Long> {
	List<CouponRedeemed> findByUserAndFinishDateAfter(User user, LocalDateTime date);
	CouponRedeemed findByUserAndCoupon(User user, Coupon coupon);
	
	@Query("SELECT COUNT(c) FROM CouponRedeemed c")
    long countCoupons();
	
	@Query("SELECT COUNT(c) FROM CouponRedeemed c WHERE c.active = true AND c.finishDate > :adjustedTime")
	long countActiveCoupons(@Param("adjustedTime") LocalDateTime adjustedTime);

	// Restituisce tutti i coupon
    @Query("SELECT c FROM CouponRedeemed c")
    List<CouponRedeemed> findAllCouponRedeemeds();

    // Restituisce tutti i coupon attivi (disabled=false, uses>0, couponExpiration > adjustedTime)
    @Query("SELECT c FROM CouponRedeemed c WHERE c.active = true AND c.finishDate > :adjustedTime")
    List<CouponRedeemed> findActiveCouponRedeemeds(@Param("adjustedTime") LocalDateTime adjustedTime);

    // Restituisce tutti i coupon disattivati (disabled=true, oppure uses=0, oppure couponExpiration <= adjustedTime)
    @Query("SELECT c FROM CouponRedeemed c WHERE c.active = false OR c.finishDate <= :adjustedTime")
    List<CouponRedeemed> findInactiveCouponRedeemeds(@Param("adjustedTime") LocalDateTime adjustedTime);
    

    @Modifying
    @Query("UPDATE CouponRedeemed c SET c.active = false WHERE c.id = :id")
    void disableCouponRedeemedById(@Param("id") Long id);

    // Metodo per abilitare un coupon (imposta disabled su false)
    @Modifying
    @Query("UPDATE CouponRedeemed c SET c.active = true WHERE c.id = :id")
    void enableCouponRedeemedById(@Param("id") Long id);
}
