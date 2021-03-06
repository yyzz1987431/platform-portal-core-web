package com.aic.paas.web.dep.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.web.dep.bean.AppMgrInfo;
import com.aic.paas.web.dep.bean.AppResInfo;
import com.aic.paas.web.dep.bean.AppTimerInfo;
import com.aic.paas.web.dep.bean.AppZoneResInfo;
import com.aic.paas.web.dep.bean.CPcApp;
import com.aic.paas.web.dep.bean.PcApp;
import com.aic.paas.web.dep.peer.PcAppPeer;
import com.binary.core.http.HttpClient;
import com.binary.core.lang.Conver;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.util.ControllerUtils;
import com.binary.jdbc.Page;


@Controller
@RequestMapping("/dep/app")
public class PcAppMvc {
	
	
	@Autowired
	PcAppPeer appPeer;
	
	
	@Value("${project.task.root}")
	String taskRoot;
	
	
	
	
	@RequestMapping("/queryInfoPage")
	public void queryInfoPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcApp cdt, String orders) {
		Page<AppMgrInfo> page = appPeer.queryInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	
	
	@RequestMapping("/queryInfoById")
	public void queryInfoById(HttpServletRequest request,HttpServletResponse response, Long id) {
		AppMgrInfo info = appPeer.queryInfoById(id);
		ControllerUtils.returnJson(request, response, info);
	}
	
	@RequestMapping("/queryById")
	public void queryById(HttpServletRequest request,HttpServletResponse response, Long id) {
		PcApp p = appPeer.queryById(id);
		ControllerUtils.returnJson(request, response, p);
	}
	
	
	
	@RequestMapping("/queryMgrPage")
	public void queryMgrPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcApp cdt, String orders) {
		Page<PcApp> page = appPeer.queryMgrPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	
	
	@RequestMapping("/queryMgrList")
	public void queryMgrList(HttpServletRequest request,HttpServletResponse response, CPcApp cdt, String orders) {
		List<PcApp> ls = appPeer.queryMgrList(cdt, orders);
		ControllerUtils.returnJson(request, response, ls);
	}

	
	@RequestMapping("/queryMgrResInfoPage")
	public void queryMgrResInfoPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcApp cdt, String orders) {
		Page<AppResInfo> page = appPeer.queryMgrResInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	@RequestMapping("/queryAppNetZoneResInfo")
	public void queryAppNetZoneResInfo(HttpServletRequest request,HttpServletResponse response, Long appId, Long appVnoId) {
		List<AppZoneResInfo> list = appPeer.queryAppNetZoneResInfo(appId, appVnoId);
		ControllerUtils.returnJson(request, response, list);
	}
	
	
	@RequestMapping("/saveOrUpdate")
	public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response, PcApp record, String strMgrIds){
		Long appId = appPeer.saveOrUpdate(record);
		Long[] mgrIds = null;
		if(!BinaryUtils.isEmpty(strMgrIds)) {
			mgrIds = Conver.to(strMgrIds.trim().split(","), Long.class);
		}
		appPeer.setAppMgrs(appId, mgrIds);
		ControllerUtils.returnJson(request, response, appId);
	}
	
	
	
	
	@RequestMapping("/queryRunAppPage")
	public void queryRunAppPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcApp cdt, String orders) {
		if(cdt == null) cdt = new CPcApp();
//		cdt.setSetupStatus(1);
		Page<AppResInfo> page = appPeer.queryMgrResInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	
	
	@RequestMapping("/queryAppTimerPage")
	public void queryAppTimerPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcApp cdt, String orders) {
		if(cdt == null) cdt = new CPcApp();
//		cdt.setSetupStatus(1);
		Page<AppTimerInfo> page = appPeer.queryMgrAppTimerInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	
	@RequestMapping("/startDeploy")
	public void startDeploy(HttpServletRequest request,HttpServletResponse response, Long appId, Long appVnoId) {
		BinaryUtils.checkEmpty(appId, "appId");
		BinaryUtils.checkEmpty(appVnoId, "appVnoId");
		HttpClient client = HttpClient.getInstance(taskRoot);
		client.request("/dep/appimage/startDeploy?appId="+appId+"&appVnoId="+appVnoId);
		ControllerUtils.returnJson(request, response, true);
	}
	
	@RequestMapping("/updateDeploy")
	public void updateDeploy(HttpServletRequest request,HttpServletResponse response, Long appId, Long appVnoId) {
		BinaryUtils.checkEmpty(appId, "appId");
		BinaryUtils.checkEmpty(appVnoId, "appVnoId");
		HttpClient client = HttpClient.getInstance(taskRoot);
		client.request("/dep/appimage/reDeploy?appId="+appId+"&appVnoId="+appVnoId);
		ControllerUtils.returnJson(request, response, true);
	}
	
	@RequestMapping("/stopDeploy")
	public void stopDeploy(HttpServletRequest request,HttpServletResponse response, Long appId, Long appVnoId) {
		BinaryUtils.checkEmpty(appId, "appId");
		BinaryUtils.checkEmpty(appVnoId, "appVnoId");
		HttpClient client = HttpClient.getInstance(taskRoot);
		client.request("/dep/appimage/stopDeploy?appId="+appId+"&appVnoId="+appVnoId);
		ControllerUtils.returnJson(request, response, true);
	}
	
	
	
	@RequestMapping("/startApp")
	public void startApp(HttpServletRequest request,HttpServletResponse response, Long appId, Long appVnoId) {
		BinaryUtils.checkEmpty(appId, "appId");
		BinaryUtils.checkEmpty(appVnoId, "appVnoId");
		HttpClient client = HttpClient.getInstance(taskRoot);
		client.request("/dep/appimage/startApp?appId="+appId+"&appVnoId="+appVnoId);
		ControllerUtils.returnJson(request, response, true);
	}
	
	@RequestMapping("/pauseApp")
	public void pauseApp(HttpServletRequest request,HttpServletResponse response, Long appId, Long appVnoId) {
		BinaryUtils.checkEmpty(appId, "appId");
		BinaryUtils.checkEmpty(appVnoId, "appVnoId");
		HttpClient client = HttpClient.getInstance(taskRoot);
		client.request("/dep/appimage/startApp?appId="+appId+"&appVnoId="+appVnoId);
		ControllerUtils.returnJson(request, response, true);
	}
	
	
}
