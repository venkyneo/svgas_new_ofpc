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

import com.svgas.main.models.publishtopics;
import com.svgas.main.repository.PublishRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/publish")
public class PublishTopicsController 
{
	@Autowired
	private PublishRepository ptrepo;
	
	@GetMapping("/getptwrtsubsite/{ssid}")
	public List<publishtopics> getAllPublishTopics(@PathVariable int ssid)
	{
		return ptrepo.findBySubsitedetailsId(ssid);
	}
	
	@GetMapping("/getptlist")
	public List<publishtopics> getAllPublishTopics()
	{
		return ptrepo.findAll();
	}
	
	@PostMapping("/addpt")
	public publishtopics addPublishTopics(@RequestBody publishtopics pt)
	{
		return ptrepo.saveAndFlush(pt);
	}
	
	@PostMapping("/removept")
	public void removePublishTopic(@RequestBody publishtopics pt)
	{
		ptrepo.deleteById(pt.getId());
	}
	
	@GetMapping("/getparticularpt/{ppid}")
	public publishtopics PTByID(@PathVariable long ppid)
	{
		publishtopics pt=null;
		Optional<publishtopics> pto = ptrepo.findById(ppid);
		if(pto.isPresent())
		{
			pt = pto.get();
		}
		
		return pt;
	}
	
	@PostMapping("/updatept")
	public void updatept(@RequestBody publishtopics pt)
	{	
		Optional<publishtopics> pto = ptrepo.findById(pt.getId());
		if(pto.isPresent())
		{
			publishtopics ptt = pto.get();
			ptt.setTopicname(pt.getTopicname());
			ptt.setSubsitedetails(pt.getSubsitedetails());
			
			ptrepo.saveAndFlush(ptt);
		}
	}
}