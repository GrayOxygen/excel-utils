
package com.saysth.excel.utils;

import java.io.FileNotFoundException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.saysth.excel.domain.ExcelXMLConfig;
import com.saysth.excel.domain.PropertyConfig;
import com.saysth.excel.validtor.CellValidator;
import com.saysth.excel.validtor.ValidatorRule;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月17日 下午5:45:47
 * 
 * @Description xml配置文件解析工具类
 */
public class XMLParserUtil {
	/**
	 * 解析xml配置文件
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午8:16:29
	 * @param xmlInputStream
	 *            xml输入流
	 * @return
	 */
	public static ExcelXMLConfig parseXML(InputStream xmlInputStream) {
		try {
			InputStream is = xmlInputStream;
			if (is == null) {
				throw new FileNotFoundException("Excel的xml配置文件 : " + xmlInputStream + " 未找到.");
			}
			SAXBuilder saxBuilder = new SAXBuilder(false);
			Document document = saxBuilder.build(is);
			// 根节点
			Element root = document.getRootElement();

			Element horizontal = root.getChild("horizontal");
			Element vertical = root.getChild("vertical");
			Element endStrategy = root.getChild("endStrategy");
			Element validators = root.getChild("validators");

			ExcelXMLConfig excelXMLConfig = new ExcelXMLConfig();

			// 读取数据映射配置
			excelXMLConfig.setHorizontalList(horizontal);
			excelXMLConfig.setVerticalList(vertical);

			// 读取结束符配置
			excelXMLConfig.setReadEndStrategy(excelXMLConfig.getPropertyConfigList(), endStrategy);

			// 校验器配置
			setValidators(excelXMLConfig.getPropertyConfigList(), validators);

			is.close();

			return excelXMLConfig;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置校验器，加载校验器，配置校验器与单元格的关联
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午1:51:06
	 * @param propertyConfigs
	 * @param validators
	 * @throws Exception
	 */
	public static void setValidators(List<PropertyConfig> propertyConfigs, Element validators) throws Exception {
		if (validators == null) {
			return;
		}

		Element cellValidators = validators.getChild("cellValidators");
		if (cellValidators == null) {
			return;
		}
		List<Element> cellValidator = cellValidators.getChildren("cellValidator");
		List<Element> loadCustomValidator = validators.getChildren("loadCustomValidator");
		// 最终的校验器(单元格数据+校验规则)
		List<CellValidator> finalValitor = new ArrayList<CellValidator>();

		// 读取校验器
		for (Element ele : cellValidator) {
			CellValidator oneCellOneValidator = new CellValidator();

			// 自定义校验器
			List<CellValidator> cutomValitor = null;

			List<Attribute> attrList = ele.getAttributes();
			ValidatorRule validatorRule = new ValidatorRule();

			PropertyConfig propertyConfig = new PropertyConfig();
			propertyConfig.setCellRefName(ele.getAttributeValue("cellName"));

			for (Attribute attr : attrList) {
				if (attr.getName().equals("customValidator")) {// 一个单元格配置有且仅有一个自定义校验器属性
					cutomValitor = new ArrayList<CellValidator>();
					String[] validatorNames = attr.getValue().split(",");

					for (Element tempEle : loadCustomValidator) {
						for (String name : validatorNames) {
							if (name.equals(tempEle.getAttribute("name").getValue())) {
								CellValidator tempValidator = (CellValidator) Class.forName(tempEle.getAttribute("classPath").getValue()).newInstance();
								tempValidator.setPropertyConfig(propertyConfig);
								cutomValitor.add(tempValidator);
							}
						}
					}

				} else {
					BeanUtils.setProperty(validatorRule, attr.getName(), attr.getValue());
				}
			}
			oneCellOneValidator.setPropertyConfig(propertyConfig);
			oneCellOneValidator.setValidatorRule(validatorRule);

			oneCellOneValidator.setCustomCellValidatorList(cutomValitor);
			finalValitor.add(oneCellOneValidator);
		}

		// 读取校验器关联关系
		for (PropertyConfig config : propertyConfigs) {
			for (CellValidator temp : finalValitor) {
				if (temp.getPropertyConfig().getCellRefName().equals(config.getCellRefName())) {
					config.setCellValidator(temp);
				}
			}
		}

	}
}
