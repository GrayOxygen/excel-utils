package com.saysth.excel.validtor;

import java.util.List;

import com.saysth.excel.domain.CellData;
import com.saysth.excel.domain.PropertyConfig;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月17日 下午2:04:41
 * 
 * @Description 单元格校验器：数据+校验规则
 */
public class CellValidator {
	private PropertyConfig propertyConfig;

	// 默认的校验规则
	private ValidatorRule validatorRule;

	// 自定义校验器
	private List<CellValidator> customCellValidatorList;

	/**
	 * 自定义校验器的校验执行方法，自定义校验器复写该方法
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:42:48
	 * @param cellData
	 * @return
	 */
	public ValidatorMsg validateCustomRules(CellData cellData) {
		return null;
	}

	/**
	 * 根据单元格名称
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:43:10
	 * @return
	 */
	public String getCellRefName() {
		return propertyConfig.getCellRefName();
	}

	public PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}

	public void setPropertyConfig(PropertyConfig propertyConfig) {
		this.propertyConfig = propertyConfig;
	}

	public ValidatorRule getValidatorRule() {
		return validatorRule;
	}

	public void setValidatorRule(ValidatorRule validatorRule) {
		this.validatorRule = validatorRule;
	}

	public List<CellValidator> getCustomCellValidatorList() {
		return customCellValidatorList;
	}

	public void setCustomCellValidatorList(List<CellValidator> customCellValidatorList) {
		this.customCellValidatorList = customCellValidatorList;
	}

}
