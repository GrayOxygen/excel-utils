package com.saysth.excel.domain;

import com.saysth.excel.enumconstants.ReadExcelDirection;
import com.saysth.excel.validtor.CellValidator;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月16日 下午1:59:33
 * 
 * @Description 属性配置对象
 */
public class PropertyConfig {
	/**
	 * 单元格位置，如"A8"
	 */
	private String cellRefName;
	/**
	 * 属性名称
	 */
	private String propertyName;
	/**
	 * 读取方向，取值范围参考ReadExcelDirection
	 */
	private ReadExcelDirection direction;
	/**
	 * 结束读取的策略
	 */
	private ReadEndStrategy readEndStrategy;
	/**
	 * 单元格校验器
	 */
	private CellValidator cellValidator;

	PropertyConfig(Integer start, Integer end) {
		ReadEndStrategy readEndStrategy = new ReadEndStrategy();
		readEndStrategy.setStart(start);
		readEndStrategy.setEnd(end);
		this.readEndStrategy = readEndStrategy;
	}

	public PropertyConfig() {
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getCellRefName() {
		return cellRefName;
	}

	public void setCellRefName(String cellRefName) {
		this.cellRefName = cellRefName;
	}

	public CellValidator getCellValidator() {
		return cellValidator;
	}

	public void setCellValidator(CellValidator cellValidator) {
		this.cellValidator = cellValidator;
	}

	public ReadEndStrategy getReadEndStrategy() {
		return readEndStrategy;
	}

	public void setReadEndStrategy(ReadEndStrategy readEndStrategy) {
		this.readEndStrategy = readEndStrategy;
	}

	public ReadExcelDirection getDirection() {
		return direction;
	}

	public void setDirection(ReadExcelDirection direction) {
		this.direction = direction;
	}

}
