package com.saysth.excel.validtor;

import com.saysth.excel.domain.CellData;
import com.saysth.excel.enumconstants.ValidateError;

/**
 * 演示自定义校验器：长度校验器
 */
@Deprecated
public class MoneyValidator extends CellValidator {

	@Override
	public ValidatorMsg validateCustomRules(CellData cellData) {
		ValidatorMsg msg = null;// 没有校验信息最好返回空
		if (cellData != null&&Double.parseDouble(cellData.getValue().toString())>100000D) {
			msg = new ValidatorMsg();
			msg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			msg.setErrMsg(getCellRefName() + "金额不在申请范围内！");
		}

		return msg;
	}

}