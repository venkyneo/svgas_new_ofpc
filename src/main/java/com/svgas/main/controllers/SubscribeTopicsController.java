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

import com.svgas.main.models.subscribetopics;
import com.svgas.main.repository.SubscribeRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/subscribe")
public class SubscribeTopicsController 
{
	@Autowired
	private SubscribeRepository strepo;
	
	@GetMapping("/getstwrtsubsite/{ssid}")
	public List<subscribetopics> getAllSubscribeTopics(@PathVariable int ssid)
	{
		return strepo.findBySubsitedetailsId(ssid);
	}
	
	@GetMapping("/getstlist")
	public List<subscribetopics> getAllSubscribeTopics()
	{
		return strepo.findAll();
	}
	
	@PostMapping("/addst")
	public subscribetopics addSubscribeTopics(@RequestBody subscribetopics st)
	{
		return strepo.saveAndFlush(st);
	}
	
	@PostMapping("/removest")
	public void removSubscribeTopics(@RequestBody subscribetopics st)
	{
		strepo.deleteById(st.getId());
	}
	
	@GetMapping("/getparticularst/{ssid}")
	public subscribetopics STByID(@PathVariable long ssid)
	{
		subscribetopics st=null;
		Optional<subscribetopics> sto = strepo.findById(ssid);
		if(sto.isPresent())
		{
			st = sto.get();
		}
		return st;
	}
	
	@PostMapping("/updatest")
	public void updatest(@RequestBody subscribetopics st)
	{
		Optional<subscribetopics> sto = strepo.findById(st.getId());
		if(sto.isPresent())
		{
			subscribetopics stt = sto.get();
			stt.setTopicname(st.getTopicname());
			stt.setSubsitedetails(st.getSubsitedetails());
			
			strepo.saveAndFlush(stt);
		}
	}
}