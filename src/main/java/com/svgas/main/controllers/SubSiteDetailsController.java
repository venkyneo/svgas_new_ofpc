package com.svgas.main.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.subsitedetails;
import com.svgas.main.repository.CustomQueriesRepository;
import com.svgas.main.repository.SubSiteDetailsRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/subsite")
public class SubSiteDetailsController 
{
	@Autowired
	private SubSiteDetailsRepository ssdrepo;
	
	@Autowired
	private CustomQueriesRepository cqr;
	
	@PostMapping("/addsubsite")
	public subsitedetails addNewSubSite(@RequestBody subsitedetails ssd)
	{
		String RString = RandomString_test();
		ssd.setUniquename(RString);
		subsitedetails ssdr = ssdrepo.save(ssd);
		cqr.CreateUniqueNameTable(RString);
		
		return ssdr;
	}
	
	@GetMapping("/getsubsitewrtsite/{sid}")
	public List<subsitedetails> getAllSubSite(@PathVariable long sid)
	{
		System.out.println("ABN2:"+sid);
		return ssdrepo.findBySitedetailsId(sid);
	}
	
	@GetMapping("/getsubsitewrtid/{ssid}")
	public subsitedetails getAllSubsite(@PathVariable long ssid)
	{
		System.out.println("ABN:"+ssid);
		subsitedetails ssd = null;
		
		Optional<subsitedetails> ssdoptional = ssdrepo.findById(ssid);
		if(ssdoptional.isPresent())
		{
			ssd = ssdoptional.get();
		}
		return ssd;
	}
	
	@GetMapping("/allsubsite")
	public List<subsitedetails> getAllSubSiteDetails()
	{
		return ssdrepo.findAll();
	}
	
	@PostMapping("/removesubsite")
	public void removeSubSite(@RequestBody subsitedetails ssd)
	{
		//there should be procedure to check if the logged in user is admin or not then only delete the row
		
		System.out.println("id:"+ssd.getId());
		ssdrepo.deleteById(ssd.getId());
		//ssdrepo.deleteById(ssd.getId());
	}
	
	@PostMapping("/updatesubsite")
	public void updateSubsite(@RequestBody subsitedetails ssd)
	{
		Optional<subsitedetails> ssd_o = ssdrepo.findById(ssd.getId());
		subsitedetails ssds = null;
		if(ssd_o.isPresent())
		{
			ssds = ssd_o.get();
			ssds.setModemtype(ssd.getModemtype());
			ssds.setSitedetails(ssd.getSitedetails());
			ssds.setSubsitelocation(ssd.getSubsitelocation());
			ssds.setSubsitename(ssd.getSubsitename());
			ssdrepo.saveAndFlush(ssds);
		}
	}
	
	public String RandomString_test() {
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
}