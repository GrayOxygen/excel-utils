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

/**
 * 
 * @author 王辉阳
 * 
 * @date 2016年3月25日 下午4:17:32
 * 
 * @Description 测试excel数据集转换为实体类f
 */
@SuppressWarnings("all")
public class TestExcelEntityExchange extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public TestExcelEntityExchange(String testName) {
		super(testName);
		System.out.println("excel importer test");
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		System.out.println("suite : 同时执行每个测试方法");
		return new TestSuite(TestExcelEntityExchange.class);
	}

	/**
	 * 一个例子看到映射
	 * 
	 * @author 王辉阳
	 * @date 2016年3月25日 下午4:45:33
	 * @throws Exception
	 */
	public void testSimplestXAXisRead() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取横向数据.xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取横向数据(最简单配置).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);
		// 去校验则是默认校验通过的
		Assert.assertTrue(excelData.isPassValid());

		// 采用了默认的转换规则，一般情况下建议使用该接口就可以,更多的需求参考下面的例子自行指定转换策略EntityNum
		ToBeansArgs excelImpoterArgs = ToBeansArgs.simpleArgs(excelData, LoanApply.class);
		List<LoanApply> data = ExcelImporter.toSimpleBeans(excelImpoterArgs);

		Assert.assertNotNull(data);

		for (LoanApply temp : data) {
			System.out.println(temp.toString());
		}
	}

	/**
	 * 纵向读取数据测试（转换的实体数据集的长度指定）
	 * 
	 * @throws Exception
	 */
	public void testEntityNumStrategyYAXisRead() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据.xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("读取纵向数据(实体数据集长度接口指定).xml");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		// 转换实体数据集的长度
		ToBeansArgs maxArgs = ToBeansArgs.simpleArgsWithEntityNum(excelData, Beacon.class, EntityNum.MAX_NUM, null);
		List<Beacon> maxData = ExcelImporter.toSimpleBeans(maxArgs);

		ToBeansArgs minArgs = ToBeansArgs.simpleArgsWithEntityNum(excelData, Beacon.class, EntityNum.MIN_NUM, null);
		List<Beacon> minData = ExcelImporter.toSimpleBeans(minArgs);

		ToBeansArgs appointArgs = ToBeansArgs.simpleArgsWithEntityNum(excelData, Beacon.class, EntityNum.APPOINT_NUM, "priseName");
		List<Beacon> appointData = ExcelImporter.toSimpleBeans(appointArgs);
		// 判断与预期结果是否相等
		Assert.assertSame(4, maxData.size());
		Assert.assertSame(2, minData.size());
		Assert.assertSame(3, appointData.size());
		System.out.println("======MAX_NUM=======");
		for (Beacon temp : maxData) {
			System.out.println(temp.toString());
		}

		System.out.println("======MIN_NUM=======");
		for (Beacon temp : minData) {
			System.out.println(temp.toString());
		}

		System.out.println("======APPOINT_NUM=======");
		for (Beacon temp : appointData) {
			System.out.println(temp.toString());
		}
	}

	/**
	 * 父子表关系，子表只有一个
	 * 
	 * @throws Exception
	 */
	public void testOneParentSonEntity() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("基础数据-国内原油加工量（父子表）.xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("基础数据-国内原油加工量（父子表）.xml");

		List<String> sonProNames = new ArrayList<String>();
		sonProNames.add("date");
		sonProNames.add("oil1");
		sonProNames.add("oil2");
		sonProNames.add("oil3");

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		if (excelData.isPassValid()) {
			ToBeansArgs excelImpoterArgs = ToBeansArgs.parentSonArgs(excelData, OilStatistics.class, new ToBeansSonArgs(OilData.class, "oilDataList"));
			List<OilStatistics> data = ExcelImporter.toParentSonBeans(excelImpoterArgs);
			for (OilStatistics temp : data) {
				System.out.println(temp.toString());
			}
		}

	}

	/**
	 * 父子表关系，子表有多个
	 * 
	 * @throws Exception
	 */
	public void testManyParentSonEntity() throws Exception {
		InputStream excelIs = this.getClass().getClassLoader().getResourceAsStream("基础数据-国内原油加工量（多个子表）.xlsx");
		InputStream xmlIs = this.getClass().getClassLoader().getResourceAsStream("基础数据-国内原油加工量（多个子表）.xml");

		List<String> sonProNames = new ArrayList<String>();

		List<ToBeansSonArgs> sonArgList = new ArrayList<ToBeansSonArgs>();
		ToBeansSonArgs sonArg = new ToBeansSonArgs(OilData.class, "oilDataList");
		sonArgList.add(sonArg);

		ToBeansSonArgs sonArg2 = new ToBeansSonArgs(OilData2.class, "oilDataList2");
		sonArgList.add(sonArg2);

		ExcelData excelData = ExcelImporter.readExcelData(excelIs, xmlIs, 0);

		if (excelData.isPassValid()) {
			ToBeansArgs excelImpoterArgs = ToBeansArgs.parentSonArgs(excelData, OilStatistics.class, sonArgList);
			List<OilStatistics> data = ExcelImporter.toParentSonBeans(excelImpoterArgs);
			for (OilStatistics temp : data) {
				System.out.println(temp.toString());
			}
		}

	}

	/**
	 * 打印方法
	 * 
	 * @author 王辉阳
	 * @date 2016年3月25日 下午4:14:40
	 * @param excelImpoterArgs
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	private void printOilStatics(ToBeansArgs excelImpoterArgs) throws InvalidFormatException, IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
		List<OilStatistics> data = ExcelImporter.toParentSonBeans(excelImpoterArgs);
		for (OilStatistics temp : data) {
			System.out.println(temp.toString());
		}
	}

}
