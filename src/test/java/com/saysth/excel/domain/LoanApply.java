package com.saysth.excel.domain;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月18日 上午10:29:08
 * 
 * @Description 测试对象：借款对象
 */
public class LoanApply {
	private String applyName;
	private String applyCode;
	private String applyReason;
	private Integer applyMoney;
	private String email;

	@Override
	public String toString() {
		return "LoanApply [applyName=" + applyName + ", applyCode=" + applyCode + ", applyReason=" + applyReason + ", applyMoney=" + applyMoney + ", email=" + email + "]";
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public Integer getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(Integer applyMoney) {
		this.applyMoney = applyMoney;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
