package it.mikedmc.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String code; // generato a caso? una parola che voglio io e dei numri a caso?
    
    private boolean permanent; // se true, il ruolo lo aggiunge all'utente.
    private boolean disabled = false; // se è true, quando qualcuno utilizza il codice, gli dirà "è disabilitato"
    
    private int durationDays; // per quanti giorni otterrai i ruoli.
    
    private int uses; // usi rimasti
    private int totalUses; // usi totali

    private LocalDateTime couponExpiration; // quando scade (che se usato, non funzionerà +)
    private LocalDateTime couponCreation; // quando creo il coupon
    

    @OneToMany(mappedBy = "coupon", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CouponRole> couponRoles;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public boolean isPermanent() {
		return permanent;
	}


	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}


	public int getDurationDays() {
		return durationDays;
	}


	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}


	public int getUses() {
		return uses;
	}


	public void setUses(int uses) {
		this.uses = uses;
	}


	public int getTotalUses() {
		return totalUses;
	}


	public void setTotalUses(int totalUses) {
		this.totalUses = totalUses;
	}


	public LocalDateTime getCouponExpiration() {
		return couponExpiration;
	}


	public void setCouponExpiration(LocalDateTime couponExpiration) {
		this.couponExpiration = couponExpiration;
	}


	public LocalDateTime getCouponCreation() {
		return couponCreation;
	}


	public void setCouponCreation(LocalDateTime couponCreation) {
		this.couponCreation = couponCreation;
	}


	public Set<CouponRole> getCouponRoles() {
		return couponRoles;
	}


	public void setCouponRoles(Set<CouponRole> couponRoles) {
		this.couponRoles = couponRoles;
	}


	public boolean isDisabled() {
		return disabled;
	}


	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
