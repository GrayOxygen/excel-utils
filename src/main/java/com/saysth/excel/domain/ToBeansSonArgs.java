
package com.saysth.excel.domain;

import java.util.List;

import com.saysth.excel.enumconstants.EntityNum;

/**
 * 
 * @author 王辉阳
 *
 * @description “父子表”的子表说明参数，表示父类中的子类属性：必须配置clazz属性和propertyName
 *
 * @date 2015年9月19日 下午7:37:20
 */
public class ToBeansSonArgs extends AbstractArgs {
	/**
	 * 子属性实体对象的属性名称
	 */
	private List<String> selfPropertyNames;
	/**
	 * 子属性对象(在父亲对象中)的属性名称
	 */
	private String propertyName;

	private ToBeansSonArgs() {
		super();
	}

	public ToBeansSonArgs(Class clazz, String propertyName) {
		super();
		super.setClazz(clazz);
		this.propertyName = propertyName;
	}

	public List<String> getSelfPropertyNames() {
		return selfPropertyNames;
	}

	public void setSelfPropertyNames(List<String> selfPropertyNames) {
		this.selfPropertyNames = selfPropertyNames;
	}

	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * 父子表结构时，初始化子参数对象的实体转换个数策略
	 */
	@Override
	public void initEntityNum() {
		if (getEntityNum() == null) {
			setEntityNum(EntityNum.MAX_NUM);
		}
		if (EntityNum.APPOINT_NUM.equals(getEntityNum())) {
			if (getAppointPropertyName() == null) {
				setEntityNum(null);
			}
		}

	}

}
