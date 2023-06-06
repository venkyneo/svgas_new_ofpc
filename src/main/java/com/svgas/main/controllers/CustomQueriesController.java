package com.svgas.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.Reports_GS;
import com.svgas.main.models.cumulativetotal_gs;
import com.svgas.main.models.reportdata_gs;
import com.svgas.main.pojo.admin_page_data;
import com.svgas.main.pojo.barChartData;
import com.svgas.main.repository.CustomQueriesRepository;
import com.svgas.main.repository.ExcelHelper;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/reports")
public class CustomQueriesController 
{
	@Autowired
	ReportService rs;
	
	@Autowired
	CustomQueriesRepository cqr;
	
	@PostMapping("/ViewReports")
	public reportdata_gs ReportView(@RequestBody Reports_GS rgs)
	{
		return rs.getHistoryData(rgs);
	}
	
	@PostMapping("/ViewCumulative")
	public cumulativetotal_gs CumulativeData(@RequestBody Reports_GS rgs)
	{
		return rs.getCumulativeData(rgs);
	}
	
	@PostMapping("/DownloadReports")
	public ResponseEntity<Resource> ReportsDownloadController(@RequestBody Reports_GS rgs)
	{
		String FileName = "Report.xlsx";
		reportdata_gs rdgs = rs.getHistoryData(rgs);
		
		ExcelHelper eh = new ExcelHelper();
		InputStreamResource file = new InputStreamResource(eh.ExcelReport_Stream(rdgs));
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FileName)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(file);
	}
	
	@GetMapping("/last10alarms/{sid}/{almgp}")
	public reportdata_gs getLast10Alarms(@PathVariable String sid, @PathVariable String almgp)
	{
		return rs.getLast10Alarms(sid, almgp);
	}
	
	@PostMapping("/past7days")
	public barChartData getBarChartData(@RequestBody long sid)
	{
		return cqr.last7DaysParamData(sid);
	}
	
	@GetMapping("/getAdminDashData")
	public List<admin_page_data> getAdminDashData()
	{
		return cqr.getAdminPageStatusData_new();
	}
}