
package com.saysth.excel.enumconstants;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月16日 下午2:32:00
 * 
 * @Description excel读取方向枚举常量
 */
public enum ReadExcelDirection implements EnumOperaterInterface {
	READ_XAXIS(0), // 横向
	READ_YAXIS(1);// 纵向

	private int value;

	private ReadExcelDirection(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
