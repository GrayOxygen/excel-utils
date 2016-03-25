package com.saysth.excel.enumconstants;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月17日 下午7:27:43
 * 
 * @Description
 * 
 *              excelData数据转换成实体类数据集时，各个属性值的数据值个数可能不一致，该类表示最终转换成的实体个数机制
 *              (如A属性读取3个数值，B属性读取4个数值，那么这个实体集生成3个或者4个，依赖的是该枚举类的策略)
 * 				
 *              非父子表结构时，默认MAX_NUM
 * 				
 *              父子表结构时，父方默认机制MIN_NUM，子默认机制MAX_NUM
 * 				
 */
public enum EntityNum implements EnumOperaterInterface {
	MIN_NUM(0), // 以属性值数据个数最小的作为最终转换的实体数据集的长度
	MAX_NUM(1), // 以属性值数据个数最大的为准
	APPOINT_NUM(2);// 指定一个属性，以它的数据值的个数为准

	private int value;

	private EntityNum(int value) {
	}

	@Override
	public int getValue() {
		return value;
	}

}
