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

public interface CouponRepository extends JpaRepository<Coupon,Long> {
	public Coupon findByCode(String code);
	
	@Query("SELECT COUNT(c) FROM Coupon c")
    long countCoupons();
	
	@Query("SELECT COUNT(c) FROM Coupon c WHERE c.disabled = false AND c.uses > 0 AND c.couponExpiration > :adjustedTime")
	long countActiveCoupons(@Param("adjustedTime") LocalDateTime adjustedTime);

	// Restituisce tutti i coupon
    @Query("SELECT c FROM Coupon c")
    List<Coupon> findAllCoupons();

    // Restituisce tutti i coupon attivi (disabled=false, uses>0, couponExpiration > adjustedTime)
    @Query("SELECT c FROM Coupon c WHERE c.disabled = false AND c.uses > 0 AND c.couponExpiration > :adjustedTime")
    List<Coupon> findActiveCoupons(@Param("adjustedTime") LocalDateTime adjustedTime);

    // Restituisce tutti i coupon disattivati (disabled=true, oppure uses=0, oppure couponExpiration <= adjustedTime)
    @Query("SELECT c FROM Coupon c WHERE c.disabled = true OR c.uses = 0 OR c.couponExpiration <= :adjustedTime")
    List<Coupon> findInactiveCoupons(@Param("adjustedTime") LocalDateTime adjustedTime);
    
    @Modifying
    @Query("UPDATE Coupon c SET c.disabled = true WHERE c.id = :id")
    void disableCouponById(@Param("id") Long id);

    // Metodo per abilitare un coupon (imposta disabled su false)
    @Modifying
    @Query("UPDATE Coupon c SET c.disabled = false WHERE c.id = :id")
    void enableCouponById(@Param("id") Long id);
}
