package com.saysth.excel.domain;

import com.saysth.excel.enumconstants.ReadExcelRange;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月17日 下午5:19:50
 * 
 * @Description 
 *              结束符策略。纵向读取数据时，默认规则为遇到空行(无数据无边框等任何excel设置的信息算作空行)停止读取，无法覆盖该默认规则，横向读取数据时
 *              ，无默认规则
 */
public class ReadEndStrategy {
	/**
	 * 区间读取策略：start表示从单元格横向或纵向后第start条记录开始读取
	 */
	private Integer start;
	/**
	 * 区间读取策略：end表示从单元格横向或纵向后第end条记录开始读取
	 */
	private Integer end;

	/**
	 * 结束符策略
	 */
	private ReadExcelRange readExcelRange;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public ReadExcelRange getReadExcelRange() {
		return readExcelRange;
	}

	public void setReadExcelRange(ReadExcelRange readExcelRange) {
		this.readExcelRange = readExcelRange;
	}

}
