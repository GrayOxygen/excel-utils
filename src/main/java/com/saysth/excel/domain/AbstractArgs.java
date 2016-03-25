
package com.saysth.excel.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.saysth.excel.enumconstants.EntityNum;

/**
 * 
 * @author 王辉阳
 *
 * @description 抽象参数
 *
 * @date 2015年9月20日 下午2:56:56
 */
public abstract class AbstractArgs {
	/**
	 * 转换成的实体Class xml中配置了 TODO 这里删除
	 */
	private Class clazz;
	/**
	 * 最终转换成的数据集的实体个数策略
	 */
	private EntityNum entityNum;
	/**
	 * 将读取指定属性的数据个数作为最终转换成实体数据集的个数
	 * 
	 * 仅在entityNum为EntityNum.APPOINT_NUM时有效
	 */
	private String appointPropertyName;

	/**
	 * 初始化参数，设置过entityNum则使用之，没设置过则“父表”参数使用MAX_NUM，“子表”参数对象使用MIN_NUM
	 */
	public abstract void initEntityNum();

	/**
	 * 确定实体个数策略
	 * 
	 * @return
	 */
	public int finalEntityNum(Map<String, List<CellData>> map) {
		Iterator<Map.Entry<String, List<CellData>>> iterator = map.entrySet().iterator();
		int num = 0;
		if (EntityNum.MAX_NUM.equals(getEntityNum())) {
			while (iterator.hasNext()) {
				Map.Entry<String, List<CellData>> entry = iterator.next();
				String proName = entry.getKey();
				if (num < entry.getValue().size()) {
					num = entry.getValue().size();
				}
			}
		}

		if (EntityNum.MIN_NUM.equals(getEntityNum())) {
			boolean flag = true;
			while (iterator.hasNext()) {
				Map.Entry<String, List<CellData>> entry = iterator.next();
				String proName = entry.getKey();
				if (flag) {
					num = entry.getValue().size();
					flag = false;
				}
				if (num > entry.getValue().size()) {
					num = entry.getValue().size();
				}
			}
		}

		if (EntityNum.APPOINT_NUM.equals(getEntityNum())) {
			while (iterator.hasNext()) {
				Map.Entry<String, List<CellData>> entry = iterator.next();
				String proName = entry.getKey();
				if (proName.equals(getAppointPropertyName())) {
					num = entry.getValue().size();
				}
			}
		}
		return num;
	}

	public Class getClazz() {
		return clazz;
	}

	public EntityNum getEntityNum() {
		return entityNum;
	}

	public void setEntityNum(EntityNum entityNum) {
		this.entityNum = entityNum;
	}

	public String getAppointPropertyName() {
		return appointPropertyName;
	}

	public void setAppointPropertyName(String appointPropertyName) {
		this.appointPropertyName = appointPropertyName;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

}
