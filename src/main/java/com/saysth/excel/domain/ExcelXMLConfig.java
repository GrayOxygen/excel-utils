package com.saysth.excel.domain;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.saysth.excel.enumconstants.ReadExcelDirection;
import com.saysth.excel.enumconstants.ReadExcelRange;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月17日 下午5:35:45
 * 
 * @Description excel的xml配置文件
 */
public class ExcelXMLConfig {
	/**
	 * 属性配置
	 */
	private List<PropertyConfig> propertyConfigList;

	/**
	 * 从xml配置文件中获取到的全局自定义结束符
	 * 
	 * 值得说明的是：xml配置中最多只能有一个全局自定义符，当xml配置中使用了USER_DEFINED_END，则必须配置全局自定义结束符
	 */
	private String endFlag;

	public List<PropertyConfig> getPropertyConfigList() {
		return propertyConfigList;
	}

	public void setPropertyConfigList(List<PropertyConfig> propertyConfigList) {
		this.propertyConfigList = propertyConfigList;
	}

	public void setHorizontalList(Element horizontalList) {
		if (horizontalList == null) {
			return;
		}
		setPropertyConfigList(horizontalList, ReadExcelDirection.READ_XAXIS);
	}

	public void setVerticalList(Element verticalList) {
		if (verticalList == null) {
			return;
		}
		setPropertyConfigList(verticalList, ReadExcelDirection.READ_YAXIS);
	}

	/**
	 * 设置横向／纵向的配置信息
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午2:01:47
	 * @param elementList
	 *            横向或纵向配置对应的Element数据集
	 * @param readExcelDirection
	 *            横向还是纵向
	 */
	private void setPropertyConfigList(Element ele, ReadExcelDirection readExcelDirection) {
		List<PropertyConfig> configList = new ArrayList<PropertyConfig>();
		if (ele.getTextTrim() != null) {
			// 解析映射关系和区间读取的配置
			String[] proConfigArray = ele.getTextTrim().split(";");
			for (int k = 0; k < proConfigArray.length; k++) {
				if (isFomartLegal(proConfigArray[k])) {
					PropertyConfig config = null;
					String postEqual = proConfigArray[k].split("=")[1].trim();
					if (postEqual.contains(",")) {// [2,4] 开始2-结束4
						Integer n = Integer.parseInt(postEqual.split("\\]")[0].split(",")[1].trim());

						if (n == -1) {// [1,-1]表示1到最大行
							n = Integer.MAX_VALUE;
						}

						config = new PropertyConfig(Integer.parseInt(postEqual.split("\\[")[1].split(",")[0].trim()), n);
					} else {// [x] ,只有一个数字时，默认为读取第x个位置的单元格
						config = new PropertyConfig(Integer.parseInt(postEqual.split("\\[")[1].split("\\]")[0].trim()), Integer.parseInt(postEqual.split("\\[")[1].split("\\]")[0].trim()));
					}
					config.setCellRefName(proConfigArray[k].split("=")[0].trim());
					config.setPropertyName(postEqual.split("\\[")[0].trim());
					config.setDirection(readExcelDirection);
					configList.add(config);
				}
			}
		}
		if (propertyConfigList == null) {
			setPropertyConfigList(new ArrayList<PropertyConfig>(1));
		}
		propertyConfigList.addAll(configList);
	}

	/**
	 * 属性配置是否合法，合法的如"C3 = propertyName(2,3)"
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午5:28:25
	 * @param proConfig
	 *            单个属性配置字符串
	 * @return true 参数合法 false 参数非法
	 */
	private boolean isFomartLegal(String proConfig) {
		if (proConfig.contains("=") && (proConfig.contains("[") && proConfig.contains("]"))) {
			return true;
		}
		return false;
	}

	/**
	 * 解析结束符策略配置
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午8:11:57
	 * @param proConfigList
	 *            所有的属性配置
	 * @param endStrategy
	 *            结束符策略
	 */
	public void setReadEndStrategy(List<PropertyConfig> proConfigList, Element endStrategy) {
		if (endStrategy == null) {
			return;
		}

		Element horizontalEndElement = endStrategy.getChild("horizontalEnd");
		Element verticalEndElement = endStrategy.getChild("verticalEnd");

		if (endStrategy.getAttributeValue("wholeEnd") == null && horizontalEndElement == null && verticalEndElement == null) {
			return;
		}

		// 横向、纵向上的结束符配置
		endAxisElementConfig(proConfigList, horizontalEndElement, ReadExcelDirection.READ_XAXIS);
		endAxisElementConfig(proConfigList, verticalEndElement, ReadExcelDirection.READ_YAXIS);
		// 全局结束符的配置
		if (endStrategy.getAttributeValue("wholeEnd") != null) {
			for (PropertyConfig tempConfig : proConfigList) {
				tempConfig.getReadEndStrategy().setReadExcelRange(ReadExcelRange.valueOf(endStrategy.getAttributeValue("wholeEnd").trim()));
			}
		}

		// 自定义结束符
		if (endStrategy.getAttributeValue("customEndFlag") != null) {
			endFlag = endStrategy.getAttributeValue("customEndFlag");
		}

	}

	/**
	 * 横向和纵向上的结束符号配置（既配置了全局结束符策略或者横向 纵向上的结束策略，子级的配置覆盖上级）
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:33:25
	 * @param proConfigList
	 * @param endElement
	 * @param readExcelDirection
	 */
	private void endAxisElementConfig(List<PropertyConfig> proConfigList, Element endElement, ReadExcelDirection readExcelDirection) {
		if (endElement != null) {

			// (横向／纵向的)子标签配置 －－单元格结束符配置
			if (endElement.getTextTrim() != null) {
				String[] totalArray = endElement.getTextTrim().split(";");
				for (PropertyConfig tempConfig : proConfigList) {
					for (String singleProConfig : totalArray) {
						String[] array = singleProConfig.split("=");
						if (tempConfig.getCellRefName().equals(array[0].trim())) {
							tempConfig.getReadEndStrategy().setReadExcelRange(ReadExcelRange.valueOf(array[1].trim()));
						}
					}
				}
			}

			// (横向／纵向)当前标签配置
			if (endElement.getAttributeValue("end") != null) {
				for (PropertyConfig tempConfig : proConfigList) {
					if (tempConfig.getDirection().equals(readExcelDirection)) {
						tempConfig.getReadEndStrategy().setReadExcelRange(ReadExcelRange.valueOf(endElement.getAttributeValue("end").trim()));
					}
				}
			}

		}
	}

	/**
	 * 根据单元格名称获取属性配置对象
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:24:22
	 * @param cellRefName
	 * @return
	 */
	public PropertyConfig getPropertyConfig(String cellRefName) {
		if (cellRefName == null) {
			return null;
		}
		for (PropertyConfig tempConfig : propertyConfigList) {
			if (tempConfig.getCellRefName().equals(cellRefName)) {
				return tempConfig;
			}
		}
		return null;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

}
