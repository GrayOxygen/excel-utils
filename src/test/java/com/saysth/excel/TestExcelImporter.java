package com.saysth.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;

import com.saysth.excel.api.ExcelImporter;
import com.saysth.excel.domain.Beacon;
import com.saysth.excel.domain.CellData;
import com.saysth.excel.domain.ExcelData;
import com.saysth.excel.domain.LoanApply;
import com.saysth.excel.domain.OilData;
import com.saysth.excel.domain.OilData2;
import com.saysth.excel.domain.OilStatistics;
import com.saysth.excel.domain.ToBeansArgs;
import com.saysth.excel.domain.ToBeansSonArgs;
import com.saysth.excel.enumconstants.EntityNum;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@SuppressWarnings("all")
public class TestExcelImporter extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public TestExcelImporter(String testName) {
		super(testName);
		System.out.println("excel importer test");
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		System.out.println("suite : 同时执行每个测试方法");
		return new TestSuite(TestExcelImporter.class);
	}

	/**
	 * 横向读取数据测试（最简单配置，仅含属性映射配置）
	 * 
	 * 读取单个单元格数据
	 * 
	 * @throws Exception
	 */
	public void testSimplestXAXisRead() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取横向数据.xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取横向数据(最简单配置).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		Assert.assertNotNull(excelData);
		Assert.assertTrue(excelData.isPassValid());

		print(excelData);
	}

	/**
	 * 横向读取数据测试（数据映射配置+校验器）
	 * 
	 * @throws Exception
	 */
	public void testValidateXAXisRead() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取横向数据.xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取横向数据(数据映射＋校验器).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);
		// 预期：自定义校验器失败
		Assert.assertTrue(!excelData.isPassValid());
		Assert.assertNotNull(excelData);
		Assert.assertNotNull(excelData.getErrorData());
		Assert.assertNotNull(excelData.getErrorData().getValidatorMsgList());
		Assert.assertNotNull(excelData.getErrorData().getValidatorMsgList().get(0));
		Assert.assertNotNull(excelData.getErrorData().getValidatorMsgList().get(0).getErrMsg());

		System.out.println(excelData.getErrorData().getPropertyName() + "-->" + excelData.getErrorData().getValue() + "--->" + excelData.getErrorData().getValidatorMsgList().get(0).getErrMsg());
	}

	/**
	 * 纵向读取数据测试（最简单配置，仅含数据映射配置）
	 * 
	 * @throws Exception
	 */
	public void testSimplestYAXisRead() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据.xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(最简单配置).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		Assert.assertNotNull(excelData);
		print(excelData);

	}

	/**
	 * 纵向读取数据测试（数据映射配置＋校验器）
	 * 
	 * @throws Exception
	 */
	public void testValidateYAXisRead() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(数据映射＋校验器).xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(数据映射＋校验器).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);
		Assert.assertTrue(!excelData.isPassValid());

	}

	/**
	 * 纵向读取数据测试（结束符策略：单元格策略）
	 * 
	 * 自定义的结束符时候
	 * 
	 * @throws Exception
	 */
	public void testReadEndStrategyYAXisRead1() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(结束符策略-单元格结束符).xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(结束符策略-单元格结束符).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		Assert.assertNotNull(excelData);
		Assert.assertTrue(excelData.isPassValid());
		print(excelData);

	}

	/**
	 * 纵向读取数据测试（结束符策略：纵向策略）
	 * 
	 * 空的单元格为结束位置(纵向读取时候，这是默认的规则，指定不指定都会执行，无法覆盖该策略 )
	 * 
	 * @throws Exception
	 */
	public void testReadEndStrategyYAXisRead2() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(结束符策略-纵向全局结束符).xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(结束符策略-纵向全局结束符).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		Assert.assertNotNull(excelData);
		Assert.assertTrue(excelData.isPassValid());
		print(excelData);

	}

	/**
	 * 纵向读取数据测试（结束符策略：全局策略）
	 * 
	 * 全局策略优先级最高，设置了全局策略，其他策略则被忽略
	 * 
	 * 
	 * @throws Exception
	 */
	public void testReadEndStrategyYAXisRead3() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(结束符策略-全局结束符).xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(结束符策略-全局结束符).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		Assert.assertNotNull(excelData);
		Assert.assertTrue(excelData.isPassValid());
		print(excelData);

	}

	/**
	 * 打印方法
	 * 
	 * @author 王辉阳
	 * @date 2016年3月25日 下午4:52:21
	 * @param excelData
	 */
	private void print(ExcelData excelData) {
		Map<String, List<CellData>> map = excelData.getMap();
		if (null == map || map.size() < 1) {
			return;
		}

		Set<Map.Entry<String, List<CellData>>> entrySet = map.entrySet();
		Iterator<Map.Entry<String, List<CellData>>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<CellData>> entry = iterator.next();

			String key = entry.getKey();
			System.out.println("key---------->" + key);
			System.out.println("value---------->");
			for (CellData data : entry.getValue()) {
				System.out.println(data.getValue() + "\n\r");

			}

		}
	}
}
