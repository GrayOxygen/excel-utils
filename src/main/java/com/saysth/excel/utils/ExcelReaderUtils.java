package com.saysth.excel.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import com.saysth.excel.domain.CellData;
import com.saysth.excel.domain.ExcelData;
import com.saysth.excel.domain.ExcelXMLConfig;
import com.saysth.excel.domain.PropertyConfig;
import com.saysth.excel.domain.ToBeansSonArgs;
import com.saysth.excel.enumconstants.ReadExcelDirection;
import com.saysth.excel.enumconstants.ReadExcelRange;
import com.saysth.excel.enumconstants.ValidateError;
import com.saysth.excel.validtor.ValidatorMsg;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月16日 下午1:57:37
 * 
 * @Description excel读取工具类
 */
public class ExcelReaderUtils {
	// 自定义的结束符
	// private String endFlag;

	/**
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午5:19:24
	 * @param excelInputStream
	 *            excel文件的文件流
	 * @param xmlInputStream
	 *            xml模板配置文件的文件流
	 * @return 读取的数据集
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static ExcelData readExcel(InputStream excelInputStream, InputStream xmlInputStream, Integer sheetIndex) throws EncryptedDocumentException, InvalidFormatException, IOException {
		ExcelData excelData = new ExcelData();
		Map<String, List<CellData>> map = new HashMap<String, List<CellData>>();

		Workbook workbook = WorkbookFactory.create(excelInputStream);
		Sheet sheet = workbook.getSheetAt(sheetIndex);

		ExcelXMLConfig excelXMLConfig = XMLParserUtil.parseXML(xmlInputStream);
		// 获取数据： 遍历每个属性：根据读取方向，获取到原始数据集
		map = readDatas(excelXMLConfig, excelData, sheet);

		excelData.setMap(map);

		excelInputStream.close();
		xmlInputStream.close();

		return excelData;
	}

	/**
	 * 解析excel数据
	 * 
	 * @author 王辉阳
	 * @date 2016年3月25日 下午2:50:53
	 * @param excelXMLConfig
	 * @param excelData
	 * @param sheet
	 * @return
	 */
	private static Map<String, List<CellData>> readDatas(ExcelXMLConfig excelXMLConfig, ExcelData excelData, Sheet sheet) {
		List<PropertyConfig> proConfigList = excelXMLConfig.getPropertyConfigList();
		Map<String, List<CellData>> map = new HashMap<String, List<CellData>>();

		for (int i = 0; i < proConfigList.size(); i++) {
			// 校验失败，停止读取
			if (!excelData.isPassValid()) {
				map = null;
				break;
			}

			PropertyConfig proConfig = proConfigList.get(i);
			// 横向
			if (proConfig.getDirection().equals(ReadExcelDirection.READ_XAXIS)) {
				map.put(proConfig.getPropertyName(), getXAxisProValue(sheet, proConfigList.get(i), excelData, excelXMLConfig));
			}
			
			// 纵向
			if (proConfig.getDirection().equals(ReadExcelDirection.READ_YAXIS)) {// CellReference获取行为真正的行，而sheet根据行号获取行时，获取的是一行行的（合并的被拆开看待了）
				map.put(proConfig.getPropertyName(), getYAxisProValue(sheet, proConfigList.get(i), excelData, excelXMLConfig));
			}
		}
		return map;
	}

	/**
	 * 数据校验：校验后的信息设置到cellData中
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:10:02
	 * @param map
	 * @param excelXMLConfig
	 */
	private static void setValidateMsg(Map<String, List<CellData>> map, ExcelXMLConfig excelXMLConfig) {
		Set<Map.Entry<String, List<CellData>>> set = map.entrySet();
		Iterator<Map.Entry<String, List<CellData>>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, List<CellData>> entry = iterator.next();
			String cellRefName = entry.getKey();
			List<CellData> cellDataList = entry.getValue();
			for (CellData data : cellDataList) {
				data.setValidatorMsgList(ValidateUtil.getValidatorMsg(data, excelXMLConfig.getPropertyConfig(data.getProConfigCellRefName()).getCellValidator()));
			}
		}
	}

	/**
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午8:52:16
	 * @param cell
	 *            单元格
	 * @param excelRangeFlag
	 *            结束符标识
	 * @return
	 */
	private static boolean isStop(Cell cell, ReadExcelRange excelRangeFlag, ExcelXMLConfig excelXMLConfig) {
		if (ReadExcelRange.EMPTY_CELL_END.equals(excelRangeFlag)) {
			if (cell == null || getValueFromCell(cell) == null || "".equals(getValueFromCell(cell).toString())) {
				return true;
			}
		}

		if (ReadExcelRange.USER_DEFINED_END.equals(excelRangeFlag)) {
			if (getValueFromCell(cell) != null && getValueFromCell(cell).equals(excelXMLConfig.getEndFlag())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取横向数据
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:07:45
	 * @param sheet
	 * @param proConfig
	 * @return
	 */
	public static List<CellData> getXAxisProValue(Sheet sheet, PropertyConfig proConfig, ExcelData excelData, ExcelXMLConfig excelXMLConfig) {
		List<CellData> proDataList = new ArrayList<CellData>();
		CellReference curRef = new CellReference(proConfig.getCellRefName());
		Row row = sheet.getRow(curRef.getRow());
		if (row != null) {

			int columnIndex = getCellLastColumnNum(sheet, row.getCell(curRef.getCol()));

			int end = getReadEndIndex(proConfig);

			for (int j = proConfig.getReadEndStrategy().getStart(); j <= end; j++) {
				Cell cell = row.getCell(columnIndex + j);

				if (null == cell) {// 单元格不存在(任何内容都没有被POI认为是不存在)
					proDataList.add(new CellData());
					continue;
				}

				if (proConfig.getReadEndStrategy().getReadExcelRange() != null) {// 配置了策略范围

					if (isStop(cell, proConfig.getReadEndStrategy().getReadExcelRange(), excelXMLConfig)) {
						break;
					}
				}
				// TODO 没有配置策略时，是否需要判断空数据列为停止
				CellData cellData = new CellData();

				CellReference ref = new CellReference(cell.getRowIndex(), cell.getColumnIndex());
				cellData.setProConfigCellRefName(ref.formatAsString());// 单元格位置
				cellData.setPropertyName(proConfig.getPropertyName());
				cellData.setValue(getValueFromCell(cell));
				cellData.setValidatorMsgList(ValidateUtil.getValidatorMsg(cellData, proConfig.getCellValidator()));
				if (!cellData.isAllValid()) {
					for (ValidatorMsg msg : cellData.getValidatorMsgList()) {
						if (msg.getCode() == ValidateError.NOT_PASS_VALID_ERROR.getValue()) {
							cellData.setValidatorMsgList(Arrays.asList(msg));
							break;
						}
					}
					excelData.setErrorData(cellData);
					excelData.setPassValid(false);
					return null;
				}
				proDataList.add(cellData);
			}
		}
		return proDataList;
	}

	/**
	 * 获取纵向数据
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:07:34
	 * @param sheet
	 * @param proConfig
	 * @return
	 */
	public static List<CellData> getYAxisProValue(Sheet sheet, PropertyConfig proConfig, ExcelData excelData, ExcelXMLConfig excelXMLConfig) {
		CellReference curRef = new CellReference(proConfig.getCellRefName());
		Row curRow = sheet.getRow(curRef.getRow());
		List<CellData> proDataList = new ArrayList<CellData>();
		if (curRow != null) {
			int rowIndex = getCellLastRowNum(sheet, curRow.getCell(curRef.getCol()));
			int end = getReadEndIndex(proConfig);

			for (int j = proConfig.getReadEndStrategy().getStart(); j <= end; j++) {
				Row tempRow = sheet.getRow(rowIndex + j);
				if (tempRow != null) {

					Cell cell = tempRow.getCell(curRef.getCol());

					if (proConfig.getReadEndStrategy().getReadExcelRange() != null) {// 配置了策略范围

						if (isStop(cell, proConfig.getReadEndStrategy().getReadExcelRange(), excelXMLConfig)) {
							break;
						}
					}

					CellData cellData = new CellData();

					CellReference ref = new CellReference(cell.getRowIndex(), cell.getColumnIndex());
					cellData.setProConfigCellRefName(ref.formatAsString());// 单元格位置
					cellData.setPropertyName(proConfig.getPropertyName());
					cellData.setValue(getValueFromCell(cell));
					cellData.setValidatorMsgList(ValidateUtil.getValidatorMsg(cellData, proConfig.getCellValidator()));
					if (!cellData.isAllValid()) {
						for (ValidatorMsg msg : cellData.getValidatorMsgList()) {
							if (msg.getCode() == ValidateError.NOT_PASS_VALID_ERROR.getValue()) {
								cellData.setValidatorMsgList(Arrays.asList(msg));
								break;
							}
						}
						excelData.setErrorData(cellData);
						excelData.setPassValid(false);
						return null;
					}
					proDataList.add(cellData);
				}

				// 默认读取到空行前最后一行就停止
				if (tempRow == null || (tempRow != null && tempRow.getRowNum() == sheet.getLastRowNum())) {
					break;
				}

			}
		}
		return proDataList;
	}

	/**
	 * 设置读取最终序列号
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:18:03
	 * @param proConfig
	 * @return
	 */
	private static int getReadEndIndex(PropertyConfig proConfig) {
		if (proConfig.getReadEndStrategy().getReadExcelRange() != null && (ReadExcelRange.EMPTY_CELL_END.equals(proConfig.getReadEndStrategy().getReadExcelRange()))) {
			return Integer.MAX_VALUE;
		}

		if (proConfig.getReadEndStrategy().getReadExcelRange() != null && (ReadExcelRange.USER_DEFINED_END.equals(proConfig.getReadEndStrategy().getReadExcelRange()))) {
			return Integer.MAX_VALUE;
		}
		return proConfig.getReadEndStrategy().getEnd();
	}

	/**
	 * 获取sheet中所有合并单元格
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:06:17
	 * @param sheet
	 * @return
	 */
	public static List<CellRangeAddress> getCombineCell(Sheet sheet) {
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();

		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>(sheetmergerCount);

		// 遍历合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格加入list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			list.add(ca);
		}
		return list;
	}

	/**
	 * 获取合并单元格的最后的列数（按0开始计数）
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:06:26
	 * @param sheet
	 * @param cell
	 * @return
	 */
	public static int getCellLastColumnNum(Sheet sheet, Cell cell) {
		List<CellRangeAddress> listCombineCell = getCombineCell(sheet);
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		boolean flag = false;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					flag = true;
					break;
				}
			}
		}
		return (flag == true) ? lastC : cell.getColumnIndex();
	}

	/**
	 * 获取合并单元格的最后的行数（按0开始计数）
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:07:01
	 * @param sheet
	 * @param cell
	 * @return
	 */
	public static int getCellLastRowNum(Sheet sheet, Cell cell) {
		List<CellRangeAddress> listCombineCell = getCombineCell(sheet);
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		boolean flag = false;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					flag = true;
					break;
				}
			}
		}
		return (flag == true) ? lastR : cell.getRowIndex();
	}

	/**
	 * 是否为合并单元格
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:07:15
	 * @param listCombineCell
	 * @param cell
	 * @return
	 */
	public static Boolean isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell) {
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取单元格数据值
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午8:52:39
	 * @param cell
	 *            单元格对象
	 * @return
	 */
	public static Object getValueFromCell(Cell cell) {
		Object value = null;
		if (cell != null) {
			switch (cell.getCellType()) {

			case Cell.CELL_TYPE_FORMULA:
				FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
				if (evaluator.evaluate(cell).getCellType() == Cell.CELL_TYPE_NUMERIC) {
					value = evaluator.evaluate(cell).getNumberValue();
				}
				if (evaluator.evaluate(cell).getCellType() == Cell.CELL_TYPE_BOOLEAN) {
					value = evaluator.evaluate(cell).getNumberValue();
				}
				if (evaluator.evaluate(cell).getCellType() == Cell.CELL_TYPE_STRING) {
					value = evaluator.evaluate(cell).getNumberValue();
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_ERROR:// TODO 错误信息?
				value = cell.getErrorCellValue();
				break;
			case Cell.CELL_TYPE_BLANK: // 没有值返回null
				value = null;
				break;
			case Cell.CELL_TYPE_BOOLEAN: // boolean型
				value = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			default:// 判断日期
				try {
					value = cell.getDateCellValue();
				} catch (Exception e) {
					value = cell.getStringCellValue();
				}
			}

		}
		return value;
	}

}
