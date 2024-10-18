package it.mikedmc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mikedmc.model.Advertising;
import it.mikedmc.repository.AdvertisingRepository;

@Service
public class AdvertisingService {

	@Autowired
	private AdvertisingRepository advertisingRepository;
	
	public List<Advertising> getAllAdvertising() {
        return advertisingRepository.findAll();
    }
	public Advertising getAdvertisingById(Integer id) {
        return advertisingRepository.findById(id).orElse(null);
    }
	public Integer createAdvertising(Advertising advertising) {
        return advertisingRepository.save(advertising).getId();
    }

    public void updateAdvertising(Advertising advertisingNuovo) {
    	Advertising advertising = getAdvertisingById(advertisingNuovo.getId());
        if (advertising != null) {
        	advertising.setAuthor(advertisingNuovo.getAuthor());
    		advertising.setImageLink(advertisingNuovo.getImageLink());
    		advertising.setAuthorLink(advertisingNuovo.getAuthorLink());
    		
            advertisingRepository.save(advertising);
        }
    }

    public void deleteAdvertising(Integer id) {
        if (advertisingRepository.existsById(id)) {
        	advertisingRepository.deleteById(id);
        }
    }
}
