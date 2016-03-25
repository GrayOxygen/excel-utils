
package com.saysth.excel.domain;

import java.util.List;

/**
 * 
 * @author 王辉阳
 *
 * @description 测试父子表关系(父表)：油品统计数据
 * 
 *              (实际场景中可能需要这样的dto来展示数据，尤其是表现一对多的关系)
 * 
 *              这里一个组织对应多条以天为单位的数据
 *
 * @date 2015年9月19日 下午5:23:41
 */
public class OilStatistics {
	private String orgName;
	// 湖南油数据统计
	private List<OilData> oilDataList;
	// 广东油数据统计
	private List<OilData2> oilDataList2;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<OilData> getOilDataList() {
		return oilDataList;
	}

	public void setOilDataList(List<OilData> oilDataList) {
		this.oilDataList = oilDataList;
	}

	@Override
	public String toString() {
		return "OilStatistics [orgName=" + orgName + ", oilDataList=" + oilDataList + ", oilDataList2=" + oilDataList2 + "]";
	}

	public List<OilData2> getOilDataList2() {
		return oilDataList2;
	}

	public void setOilDataList2(List<OilData2> oilDataList2) {
		this.oilDataList2 = oilDataList2;
	}


}
