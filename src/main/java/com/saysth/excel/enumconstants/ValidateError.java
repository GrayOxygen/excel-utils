
package com.saysth.excel.enumconstants;

public enum ValidateError implements EnumOperaterInterface {
	OK(0), // 校验通过
	NOT_PASS_VALID_ERROR(1);// 校验失败
	private int value;

	private ValidateError(int value) {
		this.value = value;
	}
	@Override
	public int getValue() {
		return value;
	}
}
