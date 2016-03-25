package com.saysth.excel.validtor;

import com.saysth.excel.enumconstants.ValidateError;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月16日 下午9:26:59
 * 
 * @Description 校验信息
 */
public class ValidatorMsg {
	private Integer code = ValidateError.OK.getValue();
	// 成功时返回OK，使用自定义校验器时，错误信息可自定义；默认校验规则，错误信息见ValidatorRule类
	private String errMsg = "OK";

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
