package org.activage.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.activage.controllers.MainViewController;
import org.activage.entities.Device;
import org.activage.entities.DeviceSimilarityComparator;
import org.activage.entities.Platform;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "mainView")
@ViewScoped
public class MainView {
	
	private static final String CLIENT_ID = "myclient";
	
	private List<Platform> platforms = new ArrayList<Platform>();
	private List<Device> devices = new ArrayList<Device>();
	private List<Device> devicesFromSearch = new ArrayList<Device>();
	
	private String allWordsSearch;
	private String specificWordSearch;
	private String anyWordsSearch;
	private String noneWordsSearch;
	
	private List<String> deviceTypes = new ArrayList<String>();
	private List<String> selectedDeviceTypes = new ArrayList<String>();
	
	private List<String> hostedBy = new ArrayList<String>();
	private List<String> selectedHostedBy = new ArrayList<String>();
	
	private List<String> host = new ArrayList<String>();
	private List<String> selectedHost = new ArrayList<String>();
	
	private List<String> observes = new ArrayList<String>();
	private List<String> selectedObserves = new ArrayList<String>();
	
	private static final double SIMILARITY_THRESHOLD = 0.7;
	private static final int SIMILARITY_RESULT_UPPER_LIMIT = 5;
	
	@ManagedProperty(value = "#{mainViewController}")
	protected MainViewController mainViewController;
	
	private Device selectedDevice;
	
	@PostConstruct
	public void init() {
		loadData();
	}
	
	public void loadData(){
		platforms = new ArrayList<Platform>();
		devices = new ArrayList<Device>();
		deviceTypes = new ArrayList<String>();
		hostedBy = new ArrayList<String>();
		host = new ArrayList<String>();
		observes = new ArrayList<String>();

		try {
			platforms = mainViewController.getPlatforms(CLIENT_ID);
			devices = mainViewController.getDevices(CLIENT_ID, platforms);
			for (Device d : devices){
				for (String dt : d.getDeviceTypes()){
					if (!deviceTypes.contains(dt)){
						deviceTypes.add(dt);
					}
				}
				if (!hostedBy.contains(d.getHostedBy())){
					hostedBy.add(d.getHostedBy());
				}
				for (String h : d.getHosts()){
					if (!host.contains(h)){
						host.add(h);
					}
				}
				for (String ob : d.getObserves()){
					if (!observes.contains(ob)){
						observes.add(ob);
					}
				}
			}
			Collections.sort(deviceTypes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void openSettingsDialog(){
		RequestContext.getCurrentInstance().execute(
				"settingsDialog.show();");
	}
	
	public void openViewDeviceDetailsDialog(){
		RequestContext.getCurrentInstance().execute(
				"viewDeviceDetailsDialog.show();");
	}
	
	public void searchForSimilar(){
		clearAll();
		devicesFromSearch = new ArrayList<Device>();
		for (Device d : devices){
			if (!d.equals(selectedDevice)){
				double similarity  = selectedDevice.findSimilarityScore(d);
				d.setSimilarityValue(similarity);
				if (d.getSimilarityValue() > SIMILARITY_THRESHOLD){
					devicesFromSearch.add(d);
				}
			}
		}
		Collections.sort(devicesFromSearch, new DeviceSimilarityComparator());
		if (devicesFromSearch.size() > SIMILARITY_RESULT_UPPER_LIMIT){
			devicesFromSearch = devicesFromSearch.subList(0, SIMILARITY_RESULT_UPPER_LIMIT);
		}		
	}
	
	public void search(){
		if (nullOrEmpty(allWordsSearch) && nullOrEmpty(specificWordSearch) && nullOrEmpty(anyWordsSearch) && nullOrEmpty(noneWordsSearch)
				&& selectedDeviceTypes.isEmpty() && selectedHostedBy.isEmpty() && selectedHost.isEmpty() && observes.isEmpty()){
			return;
		}
		
		List<Device> devicesToSearch = new ArrayList<Device>();
		for (Device d : devices){
			if (!selectedDeviceTypes.isEmpty()){
				for (String s : selectedDeviceTypes){
					if (d.getDeviceTypes().contains(s)){
						devicesToSearch.add(d);
						break;
					}
				}
			}
			if (!selectedHostedBy.isEmpty()){
				for (String s : selectedHostedBy){
					if (d.getHostedBy().equals(s)){
						devicesToSearch.add(d);
						break;
					}
				}
			}
			if (!selectedHost.isEmpty()){
				for (String s : selectedHost){
					if (d.getHosts().equals(s)){
						devicesToSearch.add(d);
						break;
					}
				}
			}
			if (!observes.isEmpty()){
				for (String s : observes){
					if (d.getObserves().equals(s)){
						devicesToSearch.add(d);
						break;
					}
				}
			}
		}
		
		if (devicesToSearch.isEmpty()){
			devicesToSearch = new ArrayList<Device>(devices);
		}
		
		if (nullOrEmpty(allWordsSearch) && nullOrEmpty(specificWordSearch) && nullOrEmpty(anyWordsSearch) && nullOrEmpty(noneWordsSearch)){
			devicesFromSearch = new ArrayList<Device>(devicesToSearch);
			return;
		}
		
		devicesFromSearch = new ArrayList<Device>();
		if (allWordsSearch != null && !allWordsSearch.isEmpty()){
			String[] split = allWordsSearch.split(",");
			for (Device d : devicesToSearch){
				String name = d.getName();
				if (!nullOrEmpty(name)){
					int contains = 0;
					for (String s : split){
						if (name.trim().toLowerCase().contains(s.trim().toLowerCase())){
							contains++;
						}
					}
					if (contains == split.length){
						devicesFromSearch.add(d);
					}
				}
			}
		}
		else if (specificWordSearch != null && !specificWordSearch.isEmpty()){
			for (Device d : devicesToSearch){
				String name = d.getName();
				if (!nullOrEmpty(name)){
					if (name.trim().toLowerCase().contains(specificWordSearch.trim().toLowerCase())){
						devicesFromSearch.add(d);
					}
				}
			}
		}
		else if (anyWordsSearch != null && !anyWordsSearch.isEmpty()){
			String[] split = anyWordsSearch.split(",");
			for (Device d : devicesToSearch){
				String name = d.getName();
				if (!nullOrEmpty(name)){
					for (String s : split){
						if (name.trim().toLowerCase().contains(s.trim().toLowerCase())){
							devicesFromSearch.add(d);
							break;
						}
					}
				}
			}
		}
		else if (noneWordsSearch != null && !noneWordsSearch.isEmpty()){
			String[] split = noneWordsSearch.split(",");
			for (Device d : devicesToSearch){
				String name = d.getName();
				if (!nullOrEmpty(name)){
					int contains = 0;
					for (String s : split){
						if (!name.trim().toLowerCase().contains(s.trim().toLowerCase())){
							contains++;
						}
					}
					if (contains == split.length){
						devicesFromSearch.add(d);
					}
				}
			}
		}
	}
	
	public boolean nullOrEmpty(String s){
		return s == null || s.isEmpty();
	}
	
	public void clearAll(){
		allWordsSearch = null;
		specificWordSearch = null;
		anyWordsSearch = null;
		noneWordsSearch = null;
		selectedDeviceTypes = new ArrayList<String>();
		selectedHostedBy = new ArrayList<String>();
		selectedHost = new ArrayList<String>();
		observes = new ArrayList<String>();
		devicesFromSearch = new ArrayList<Device>();
	}
	
	public void handleKeyEvent() {
//		FacesContext ctx = FacesContext.getCurrentInstance();
//		List<UIComponent> components = ctx.getViewRoot().getChildren();
//		for (UIComponent c : components){
//			System.out.println("--> " + c.getId());
//		}
		
	}

	public MainViewController getMainViewController() {
		return mainViewController;
	}

	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	public List<Platform> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<Platform> platforms) {
		this.platforms = platforms;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Device getSelectedDevice() {
		return selectedDevice;
	}

	public void setSelectedDevice(Device selectedDevice) {
		this.selectedDevice = selectedDevice;
	}

	public List<Device> getDevicesFromSearch() {
		return devicesFromSearch;
	}

	public void setDevicesFromSearch(List<Device> devicesFromSearch) {
		this.devicesFromSearch = devicesFromSearch;
	}

	public String getAllWordsSearch() {
		return allWordsSearch;
	}

	public void setAllWordsSearch(String allWordsSearch) {
		this.allWordsSearch = allWordsSearch;
	}

	public String getSpecificWordSearch() {
		return specificWordSearch;
	}

	public void setSpecificWordSearch(String specificWordSearch) {
		this.specificWordSearch = specificWordSearch;
	}

	public String getAnyWordsSearch() {
		return anyWordsSearch;
	}

	public void setAnyWordsSearch(String anyWordsSearch) {
		this.anyWordsSearch = anyWordsSearch;
	}

	public String getNoneWordsSearch() {
		return noneWordsSearch;
	}

	public void setNoneWordsSearch(String noneWordsSearch) {
		this.noneWordsSearch = noneWordsSearch;
	}

	public List<String> getDeviceTypes() {
		return deviceTypes;
	}

	public void setDeviceTypes(List<String> deviceTypes) {
		this.deviceTypes = deviceTypes;
	}

	public List<String> getSelectedDeviceTypes() {
		return selectedDeviceTypes;
	}

	public void setSelectedDeviceTypes(List<String> selectedDeviceTypes) {
		this.selectedDeviceTypes = selectedDeviceTypes;
	}

	public List<String> getHostedBy() {
		return hostedBy;
	}

	public void setHostedBy(List<String> hostedBy) {
		this.hostedBy = hostedBy;
	}

	public List<String> getSelectedHostedBy() {
		return selectedHostedBy;
	}

	public void setSelectedHostedBy(List<String> selectedHostedBy) {
		this.selectedHostedBy = selectedHostedBy;
	}

	public List<String> getHost() {
		return host;
	}

	public void setHost(List<String> host) {
		this.host = host;
	}

	public List<String> getSelectedHost() {
		return selectedHost;
	}

	public void setSelectedHost(List<String> selectedHost) {
		this.selectedHost = selectedHost;
	}

	public List<String> getObserves() {
		return observes;
	}

	public void setObserves(List<String> observes) {
		this.observes = observes;
	}

	public List<String> getSelectedObserves() {
		return selectedObserves;
	}

	public void setSelectedObserves(List<String> selectedObserves) {
		this.selectedObserves = selectedObserves;
	}
}
