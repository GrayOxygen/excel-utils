
package com.saysth.excel.utils;

import java.util.ArrayList;
import java.util.List;

import com.saysth.excel.domain.CellData;
import com.saysth.excel.validtor.CellValidator;
import com.saysth.excel.validtor.ValidatorMsg;

/**
 * 
 * @author 王辉阳
 *
 * @description 校验工具类
 *
 * @date 2015年9月20日 下午4:12:47
 */
public class ValidateUtil {

	/**
	 * 获取校验信息
	 * 
	 * 失败和成功都返回校验信息
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午2:23:09
	 * @param cellData
	 *            单元格
	 * @param cellValidator
	 *            校验对象
	 * @return 校验信息
	 */
	public static List<ValidatorMsg> getValidatorMsg(CellData cellData, CellValidator cellValidator) {
		List<ValidatorMsg> msgList = new ArrayList<ValidatorMsg>();
		if (cellValidator != null) {

			// 默认校验
			if (cellValidator.getValidatorRule() != null) {
				List<ValidatorMsg> tempList = cellValidator.getValidatorRule().getValidatorMsg(cellData);
				if (tempList != null && tempList.size() > 0) {
					msgList.addAll(tempList);
				}
			}
			// 自定义校验
			if (cellValidator.getCustomCellValidatorList() != null && cellValidator.getCustomCellValidatorList().size() > 0) {
				List<CellValidator> validatorList = cellValidator.getCustomCellValidatorList();
				for (CellValidator temp : validatorList) {
					ValidatorMsg msg = temp.validateCustomRules(cellData);
					if (msg != null) {
						msgList.add(msg);
					} else {
						msgList.add(new ValidatorMsg());
					}
				}
			}
		}
		return msgList;

	}
}
