package com.saysth.excel.enumconstants;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月16日 下午2:32:00
 * 
 * @Description excel读取的范围 常量(结束控制标记)
 * 
 *              默认的规则（不可剔除）：读取的行为空时，下面的规则失效（因为api无法继续向下读取了）
 * 
 */
public enum ReadExcelRange implements EnumOperaterInterface {
	// INFINITELY_END(0), // 一直读到sheet最后（当行或当列最后一个记录）
	EMPTY_CELL_END(0), // TODO 横向和纵向的验证     读取到空单元格结束（null或者无内容），单元格为空即结束
	// EMPTY_WHOLE_ROW_OR_COLUMN_END(2), // 读取到空行结束，整行或整列都没数据为空行 默认的读取结束符策略
	USER_DEFINED_END(1);// 读取到自定义的“结束符”才结束。具体在xml中定义结束符

	private int value;

	private ReadExcelRange(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getNameByValue(int value) {
		for (ReadExcelRange temp : ReadExcelRange.values()) {
			if (value == temp.getValue()) {
				return temp.name();
			}
		}
		return null;
	}

}
