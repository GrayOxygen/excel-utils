package com.saysth.excel.domain;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月17日 下午5:35:45
 * 
 * @Description 数据集对象，读取到的原始数据
 */
public class ExcelData {
	/**
	 * 原始数据：key为属性名，value为数据集
	 */
	private Map<String, List<CellData>> map;
	/**
	 * 所有数据是否校验通过标识：true表示验证通过，false表示校验失败
	 * 
	 * 读取过程中，遇到校验失败则停止读取，返回校验失败的单元格数据，即map属性中仅有一条数据，且value长度为1
	 */
	private boolean passValid = true;
	/**
	 * passValid为false时，errorData表示校验失败的单元格对象
	 */
	private CellData errorData;

	public Map<String, List<CellData>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<CellData>> map) {
		this.map = map;
	}

	public boolean isPassValid() {
		return passValid;
	}

	public void setPassValid(boolean passValid) {
		this.passValid = passValid;
	}

	/**
	 * 没有错误信息返回null，有则返回错误的单元格对象
	 * 
	 * @return 校验失败的单元格对象
	 */
	public CellData getErrorData() {
		if (!passValid) {
			return errorData;
		}
		return null;
	}

	/**
	 * 错误信息
	 * 
	 * @author 王辉阳
	 * @date 2016年3月25日 下午1:48:30
	 * @return
	 */
	public String getErrorMsg() {
		if (!passValid) {
			return errorData.getValidatorMsgList().get(0).getErrMsg();
		}
		return "OK";
	}

	public void setErrorData(CellData errorData) {
		this.errorData = errorData;
	}

}