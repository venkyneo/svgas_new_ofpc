package com.svgas.main.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.sitedetails;
import com.svgas.main.repository.SiteDetailsRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/site")
public class SiteDetailsController 
{
	@Autowired
	private SiteDetailsRepository sitedetailsrepo;
	
	@GetMapping("/allsite")
	public List<sitedetails> GetAllSiteDetails()
	{
		return sitedetailsrepo.findAll();
	}
	
	@PostMapping("/addsite")
	public sitedetails addNewSite(@RequestBody sitedetails sitedetails)
	{
		return sitedetailsrepo.saveAndFlush(sitedetails);
	}
	
	@PostMapping("/removesite")
	public void removeSelectedSite(@RequestBody sitedetails sitedetails)
	{
		sitedetailsrepo.deleteById(sitedetails.getId());
	}
	
	@GetMapping("/getsitewrtid/{sid}")
	public sitedetails getSiteDetailsById(@PathVariable long sid)
	{
		sitedetails sd = null;
		Optional<sitedetails> osd = sitedetailsrepo.findById(sid);
		if(osd.isPresent())
		{
			sd = osd.get();
		}
		
		return sd;
	}
	
	@PostMapping("/updatesite")
	public void updateSelectedSite(@RequestBody sitedetails sdetails)
	{
		Optional<sitedetails> sdOptional = sitedetailsrepo.findById(sdetails.getId());
		if(sdOptional.isPresent())
		{	
			sitedetails sd = sdOptional.get();
			sd.setSitename(sdetails.getSitename());
			sd.setCity(sdetails.getCity());
			sd.setCountry(sdetails.getCountry());
			sd.setState(sdetails.getState());
			sd.setAddress(sdetails.getAddress());
			sd.setGroupid(sdetails.getGroupid());
			
			sitedetailsrepo.saveAndFlush(sd);
		}
	}
}