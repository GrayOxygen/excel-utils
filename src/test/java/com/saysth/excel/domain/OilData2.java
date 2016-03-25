
package com.saysth.excel.domain;
/**
 * 
 * @author 王辉阳
 *
 * @description 测试父子表关系(子表)：广东油品统计数据
 *
 * @date 2015年9月19日 下午5:25:26
 */
public class OilData2 {
	private String date;
	private Double oil4;
	private Double oil5;
	private Double oil6;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getOil4() {
		return oil4;
	}

	public void setOil4(Double oil4) {
		this.oil4 = oil4;
	}

	public Double getOil5() {
		return oil5;
	}

	public void setOil5(Double oil5) {
		this.oil5 = oil5;
	}

	public Double getOil6() {
		return oil6;
	}

	public void setOil6(Double oil6) {
		this.oil6 = oil6;
	}

	@Override
	public String toString() {
		return "OilData2 [date=" + date + ", oil4=" + oil4 + ", oil5=" + oil5 + ", oil6=" + oil6 + "]";
	}

}
