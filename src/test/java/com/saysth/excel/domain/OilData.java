
package com.saysth.excel.domain;

/**
 * 
 * @author 王辉阳
 *
 * @description 测试父子表关系(子表)：湖南油品统计数据
 *
 * @date 2015年9月19日 下午5:25:26
 */
public class OilData {
	private String date;
	private Double oil1;
	private Double oil2;
	private Double oil3;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getOil1() {
		return oil1;
	}

	public void setOil1(Double oil1) {
		this.oil1 = oil1;
	}

	public Double getOil2() {
		return oil2;
	}

	public void setOil2(Double oil2) {
		this.oil2 = oil2;
	}

	public Double getOil3() {
		return oil3;
	}

	public void setOil3(Double oil3) {
		this.oil3 = oil3;
	}

	@Override
	public String toString() {
		return "OilData [date=" + date + ", oil1=" + oil1 + ", oil2=" + oil2 + ", oil3=" + oil3 + "]";
	}

}
