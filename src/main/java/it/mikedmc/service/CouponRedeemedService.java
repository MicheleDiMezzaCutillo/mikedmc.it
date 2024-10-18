package it.mikedmc.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mikedmc.model.Coupon;
import it.mikedmc.model.CouponRedeemed;
import it.mikedmc.model.User;
import it.mikedmc.repository.CouponRedeemedRepository;
import it.mikedmc.repository.CouponRedeemedRepository;
import jakarta.transaction.Transactional;

@Service
public class CouponRedeemedService {

	@Autowired
	private CouponRedeemedRepository couponRedeemedRepository;
	
	public List<CouponRedeemed> findCouponRoles(User user) {
		return couponRedeemedRepository.findByUserAndFinishDateAfter(user, LocalDateTime.now().plusHours(2));
	}
	
	public List<CouponRedeemed> findAll() {
		return couponRedeemedRepository.findAll();
	}

    @Transactional
	public void disable(long id) {
    	couponRedeemedRepository.disableCouponRedeemedById(id);
	}
    
    @Transactional
	public void enable(long id) {
    	couponRedeemedRepository.enableCouponRedeemedById(id);
	}
	
	public List<CouponRedeemed> findActiveCouponRedeemeds() {
		return couponRedeemedRepository.findActiveCouponRedeemeds(LocalDateTime.now().plusHours(2));
	}

	public List<CouponRedeemed> findInactiveCouponRedeemeds() {
		return couponRedeemedRepository.findInactiveCouponRedeemeds(LocalDateTime.now().plusHours(2));
	}

	public void deleteById(Long id) {
		couponRedeemedRepository.deleteById(id);
	}

	public CouponRedeemed findById(Long id) {
		return couponRedeemedRepository.findById(id).orElse(new CouponRedeemed());
	}

	public void save(CouponRedeemed couponRedeemed) {
		couponRedeemedRepository.save(couponRedeemed);
	}
}
