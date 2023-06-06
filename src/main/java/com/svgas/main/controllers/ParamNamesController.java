package com.svgas.main.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.ECategoryType;
import com.svgas.main.models.parameternames;
import com.svgas.main.repository.CustomQueriesRepository;
import com.svgas.main.repository.ParameterNamesRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/pnames")
public class ParamNamesController 
{
	@Autowired
	private ParameterNamesRepository pnamesrepo;
	
	@Autowired
	private CustomQueriesRepository cqr;
	
	@GetMapping("/allpnames")
	@PreAuthorize("hasRole('ADMIN')")
	public List<parameternames> getAllPnames()
	{
		List<parameternames> pnames_list = pnamesrepo.findAll();
		return pnames_list;
	}
	
	@PostMapping("/addpnames")
	@PreAuthorize("hasRole('ADMIN')")
	public parameternames addPNames(@RequestBody parameternames pnames_gs)
	{
		int highestValue=0;
		try
		{
			System.out.println("id:"+pnames_gs.getSubsitedetails().getId());
			highestValue = pnamesrepo.findMaxParameternamesBySubsiteNative(pnames_gs.getSubsitedetails().getId());
		}
		catch(Exception et)
		{
			highestValue = 0;
		}
		System.out.println("highest_value-1:"+highestValue);
		highestValue = highestValue + 1;
		System.out.println("highest_value-2:"+highestValue);
		String pcolname = "value"+highestValue;
		pnames_gs.setParamcolname(pcolname);
		pnames_gs.setParamcolnum(highestValue);
		pnames_gs.setDatetime(new Date());
		System.out.println("-------------1");
		parameternames pnames_resp = pnamesrepo.saveAndFlush(pnames_gs);
		System.out.println("-------------2");
		
		if(pnames_gs.getCategoryid().getCategory().equals(ECategoryType.ALARM_REPORT))
		{
			System.out.println("-------------3");
			String tbname = pnames_gs.getSubsitedetails().getUniquename()+"_alarms";
			cqr.AddColumnsToUniqueTable(tbname, pcolname);
		}
		else
		{
			System.out.println("-------------4");
			cqr.AddColumnsToUniqueTable(pnames_gs.getSubsitedetails().getUniquename(), pcolname);
		}
		return pnames_resp;
	}
	
	@GetMapping("/pnameswrtsubsite/{ssid}")
	public List<parameternames> getPNamesBySubsite(@PathVariable long ssid)
	{
		return pnamesrepo.findBySubsitedetailsId(ssid);
	}
	@PostMapping("/updatepnames_n")
	public void updatePnamesNew(@RequestBody parameternames pnames)
	{
		try
		{
			parameternames pn=null;
			Optional<parameternames> params_old_op = pnamesrepo.findById(pnames.getId());
			pn = pnamesrepo.save(pnames);
		}
		catch(Exception et)
		{
			System.out.println(et.toString());
		}
	}
	
	@GetMapping("/pnameswrtsubandptype/{ssid}/{ptid}")
	public List<parameternames> getPNamesBySubsiteAndPtype(@PathVariable int ssid, @PathVariable int ptid)	
	{
		return pnamesrepo.findBySubsitedetailsIdAndParametertypesId(ssid, ptid);
	}
	
	@GetMapping("/pnameswrtid/{id}")
	public parameternames getPNamesById(@PathVariable long id)	
	{
		parameternames pn=null;
		Optional<parameternames> params_old_op = pnamesrepo.findById(id);
		if(params_old_op.isPresent())
		{
			pn = params_old_op.get();			
		}
		return pn;
	}
	
	@GetMapping("/removepnames/{pid}")
	@PreAuthorize("hasRole('ADMIN')")
	public void RemovePNames(@PathVariable long pid)
	{
		//make a logic to check that it is ADMINISTRATOR/DEVELOPER and NOT USER
		pnamesrepo.deleteById(pid);			
	}
}