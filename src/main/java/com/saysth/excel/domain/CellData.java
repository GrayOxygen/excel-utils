package com.saysth.excel.domain;

import java.util.List;

import com.saysth.excel.enumconstants.ValidateError;
import com.saysth.excel.validtor.ValidatorMsg;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月16日 下午8:55:23
 * 
 * @Description 单元格对象
 */
public class CellData {
	/**
	 * 单元格位置，如B2
	 */
	private String proConfigCellRefName;
	/**
	 * 单元格数据值：读取到的excel只有double string date boolean四种类型
	 */
	private Object value;
	/**
	 * 单元格对应的属性名称，如name，age
	 */
	private String propertyName;
	/**
	 * 校验的结果信息
	 */
	private List<ValidatorMsg> validatorMsgList;

	/**
	 * 验证是否通过：validatorMsgList中所有校验信息都为成功才算通过
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午3:10:54
	 * @return true表示验证通过，false表示校验失败
	 */
	public boolean isAllValid() {
		if (validatorMsgList != null) {
			for (ValidatorMsg msg : validatorMsgList) {
				if (ValidateError.OK.getValue() != msg.getCode()) {
					return false;
				}
			}
		}
		return true;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getProConfigCellRefName() {
		return proConfigCellRefName;
	}

	public void setProConfigCellRefName(String proConfigCellRefName) {
		this.proConfigCellRefName = proConfigCellRefName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public List<ValidatorMsg> getValidatorMsgList() {
		return validatorMsgList;
	}

	public void setValidatorMsgList(List<ValidatorMsg> validatorMsgList) {
		this.validatorMsgList = validatorMsgList;
	}

}