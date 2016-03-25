package com.saysth.excel.domain;

public class BeaconDto {
	private String uuid;
	private Integer major;
	private Integer minor;
	private String mac;
	private String sendTagName;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public Integer getMinor() {
		return minor;
	}

	public void setMinor(Integer minor) {
		this.minor = minor;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSendTagName() {
		return sendTagName;
	}

	public void setSendTagName(String sendTagName) {
		this.sendTagName = sendTagName;
	}

	@Override
	public String toString() {
		return "BeaconDto [uuid=" + uuid + ", major=" + major + ", minor=" + minor + ", mac=" + mac + ", sendTagName=" + sendTagName + "]";
	}
	

}
