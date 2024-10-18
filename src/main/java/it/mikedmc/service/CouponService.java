package it.mikedmc.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mikedmc.model.Coupon;
import it.mikedmc.model.CouponRedeemed;
import it.mikedmc.model.User;
import it.mikedmc.repository.CouponRedeemedRepository;
import it.mikedmc.repository.CouponRepository;
import jakarta.transaction.Transactional;

@Service
public class CouponService {
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Autowired
	private CouponRedeemedRepository couponRedeemedRepository;
	
	public Coupon findByCode(String code) {
		return couponRepository.findByCode(code);
	}

	public List<Coupon> findAll() {
		return couponRepository.findAll();
	}
	
    @Transactional
	public void disable(long id) {
		couponRepository.disableCouponById(id);
	}
    
    @Transactional
	public void enable(long id) {
		couponRepository.enableCouponById(id);
	}
	
	public boolean findByUserAndCoupon(User loggedUser,Coupon coupon) {
		CouponRedeemed cr = couponRedeemedRepository.findByUserAndCoupon(loggedUser, coupon);
		
		return cr!=null;
	}

	public void saveNewCouponRedemption(CouponRedeemed cr) {
		couponRedeemedRepository.save(cr);
	}

	public void removeOneUse(Coupon coupon) { // aggiungere quì un log per quando gli usi arrivano a 0.
		coupon.setUses(coupon.getUses()-1);
		couponRepository.save(coupon);
	}

	public List<Coupon> findActiveCoupons() {
		return couponRepository.findActiveCoupons(LocalDateTime.now().plusHours(2));
	}

	public List<Coupon> findInactiveCoupons() {
		return couponRepository.findInactiveCoupons(LocalDateTime.now().plusHours(2));
	}

	public void deleteById(Long id) {
		couponRepository.deleteById(id);
	}

	public Coupon findById(Long id) {
		return couponRepository.findById(id).orElse(new Coupon());
	}

	public Coupon save(Coupon c) {
		return couponRepository.save(c);
	}
	
}
