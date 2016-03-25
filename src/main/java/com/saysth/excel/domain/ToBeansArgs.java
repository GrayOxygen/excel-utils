package com.saysth.excel.domain;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.saysth.excel.enumconstants.EntityNum;

/**
 * 
 * @author 王辉阳
 * 
 * @description 接口调用传入的参数对象(实体转换相关参数)
 * 
 * @date 2015年9月19日 下午4:19:58
 */
@SuppressWarnings("all")
public class ToBeansArgs extends AbstractArgs {
	/**
	 * 需要转换的数据
	 */
	private ExcelData excelData;

	/**
	 * “字表”说明参数
	 */
	private List<ToBeansSonArgs> sonArgs;

	private ToBeansArgs(ExcelData excelData, Class clazz, EntityNum entityNum, String appointPropertyName) {
		super();
		this.excelData = excelData;
		super.setClazz(clazz);
		super.setEntityNum(entityNum);
		super.setAppointPropertyName(appointPropertyName);
	}

	private ToBeansArgs(ExcelData excelData, Class clazz) {
		super();
		this.excelData = excelData;
		super.setClazz(clazz);
	}

	private ToBeansArgs(ExcelData excelData, Class clazz, EntityNum entityNum, String appointPropertyName, List<ToBeansSonArgs> sonArgs) {
		super();
		this.excelData = excelData;
		super.setClazz(clazz);
		super.setEntityNum(entityNum);
		super.setAppointPropertyName(appointPropertyName);
		this.sonArgs = sonArgs;
	}

	private ToBeansArgs(ExcelData excelData, Class clazz, List<ToBeansSonArgs> sonArgs) {
		super();
		this.excelData = excelData;
		super.setClazz(clazz);
		this.sonArgs = sonArgs;
	}

	/**
	 * 创建简单表结构的参数对象
	 * 
	 * 
	 * @return
	 */
	/**
	 * 
	 * @param excelData
	 *            需要转换成实体数据的原始数据
	 * @param clazz
	 *            转换成的实体数据的Class类型
	 * @param entityNum
	 *            实体个数策略
	 * @param appointPropertyName
	 *            仅在entityNum采用EntityNum.APPOINT_NUM策略时有效
	 * @return
	 */
	public static ToBeansArgs simpleArgsWithEntityNum(ExcelData excelData, Class clazz, EntityNum entityNum, String appointPropertyName) {
		return new ToBeansArgs(excelData, clazz, entityNum, appointPropertyName);
	}

	/**
	 * 当获取的excel值时，各个属性的值的个数存在不一致时，采用EntityNum.MAX_NUM策略
	 * 
	 * @param excelData
	 *            需要转换成实体数据的原始数据.实体个数策略采用默认的策略－－EntityNum.MAX_NUM
	 * @param clazz
	 *            转换成的实体数据的Class类型
	 * @return
	 */
	public static ToBeansArgs simpleArgs(ExcelData excelData, Class clazz) {
		return new ToBeansArgs(excelData, clazz, EntityNum.MAX_NUM, null);
	}

	/**
	 * 
	 * @param excelData
	 *            需要转换成实体数据的原始数据
	 * @param clazz
	 *            (父表转换成的)实体数据的Class类型
	 * @param entityNum
	 *            实体个数策略：指定（“父表”)转换成的实体数据集的实体个数
	 * @param appointPropertyName
	 *            仅在entityNum采用EntityNum.APPOINT_NUM策略时对“父表”有效
	 * @param sonArgs
	 *            "子表"说明参数
	 * @return
	 */
	public static ToBeansArgs parentSonArgsWithEntityNum(ExcelData excelData, Class clazz, EntityNum entityNum, String appointPropertyName, List<ToBeansSonArgs> sonArgs) {
		return new ToBeansArgs(excelData, clazz, entityNum, appointPropertyName, sonArgs);
	}

	/**
	 * 
	 * @param excelData
	 *            需要转换成实体数据的原始数据
	 * @param clazz
	 *            (父表转换成的)实体数据的Class类型
	 * @param entityNum
	 *            实体个数策略：指定（“父表”)转换成的实体数据集的实体个数
	 * @param appointPropertyName
	 *            仅在entityNum采用EntityNum.APPOINT_NUM策略时对“父表”有效
	 * @param sonArgs
	 *            "子表"说明参数
	 * @return
	 */
	public static ToBeansArgs parentSonArgsWithEntityNum(ExcelData excelData, Class clazz, EntityNum entityNum, String appointPropertyName, ToBeansSonArgs... sonArgs) {
		return new ToBeansArgs(excelData, clazz, entityNum, appointPropertyName, Arrays.asList(sonArgs));
	}

	/**
	 * 
	 * @param excelData
	 *            需要转换成实体数据的原始数据.实体个数策略采用默认的策略－－EntityNum.MIN_NUM
	 * @param clazz
	 *            (父表转换成的)实体数据的Class类型
	 * @param sonArgs
	 *            "子表"说明参数
	 * @return
	 */
	public static ToBeansArgs parentSonArgs(ExcelData excelData, Class clazz, ToBeansSonArgs... sonArgs) {
		return new ToBeansArgs(excelData, clazz, EntityNum.MIN_NUM, null, Arrays.asList(sonArgs));
	}

	/**
	 * 
	 * @param excelData
	 *            需要转换成实体数据的原始数据.实体个数策略采用默认的策略－－EntityNum.MIN_NUM
	 * @param clazz
	 *            (父表转换成的)实体数据的Class类型
	 * @param sonArgs
	 *            "子表"说明参数
	 * @return
	 */
	public static ToBeansArgs parentSonArgs(ExcelData excelData, Class clazz, List<ToBeansSonArgs> sonArgs) {
		return new ToBeansArgs(excelData, clazz, EntityNum.MIN_NUM, null, sonArgs);
	}

	/**
	 * 判断是否是否为子属性实例的属性
	 * 
	 * @param propertyName
	 *            属性名称
	 * @return
	 */
	public boolean existNameInSonArgs(String propertyName) {
		if (propertyName != null) {
			for (ToBeansSonArgs tempSonArg : sonArgs) {
				if (tempSonArg.getSelfPropertyNames().contains(propertyName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 父子表结构时，初始化实体数据转换个数策略
	 */
	@Override
	public void initEntityNum() {
		if (getEntityNum() == null) {
			setEntityNum(EntityNum.MIN_NUM);
		}
		if (EntityNum.APPOINT_NUM.equals(getEntityNum())) {
			if (getAppointPropertyName() == null) {
				setEntityNum(null);
			}
		}
	}

	public List<ToBeansSonArgs> getSonArgs() {
		return sonArgs;
	}

	public void setSonArgs(List<ToBeansSonArgs> sonArgs) {
		this.sonArgs = sonArgs;
	}

	public void setSonArgs(ToBeansSonArgs... sonArg) {
		this.sonArgs = Arrays.asList(sonArg);
	}

	public ExcelData getExcelData() {
		return excelData;
	}

	public void setExcelData(ExcelData excelData) {
		this.excelData = excelData;
	}

}
