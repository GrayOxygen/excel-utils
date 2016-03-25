package com.saysth.excel.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.saysth.excel.domain.CellData;
import com.saysth.excel.domain.ExcelData;
import com.saysth.excel.domain.ToBeansArgs;
import com.saysth.excel.domain.ToBeansSonArgs;
import com.saysth.excel.utils.ExcelReaderUtils;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月16日 下午1:57:37
 * 
 * @Description 对外开放的接口：excel读取接口
 */
@SuppressWarnings("all")
public class ExcelImporter {

	/**
	 * 开放接口：读取excel
	 * 
	 * @author 王辉阳
	 * @date 2015年9月16日 下午5:19:24
	 * @param excelInputStream
	 *            excel文件的输入流，支持reset mark方法的流
	 * @param xmlInputStream
	 *            xml模板配置文件的输入流，支持reset mark方法的流
	 * @param sheetIndex
	 *            从0开始计数，指定excel欲读取的sheet
	 * @return 读取的数据集
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static ExcelData readExcelData(InputStream excelInputStream, InputStream xmlInputStream, Integer sheetIndex) throws EncryptedDocumentException, InvalidFormatException, IOException {
		return ExcelReaderUtils.readExcel(excelInputStream, xmlInputStream, sheetIndex);
	}

	public static List toSimpleBeans(ToBeansArgs toBeansArgs) throws EncryptedDocumentException, InvalidFormatException, IOException, IllegalAccessException, InstantiationException,
			InvocationTargetException {

		// 得到最终转换的条数，list实例
		toBeansArgs.initEntityNum();
		if (toBeansArgs.getEntityNum() == null) {
			return null;
		}

		int num = toBeansArgs.finalEntityNum(toBeansArgs.getExcelData().getMap());

		List destList = new ArrayList(num);// 仅仅是数据
		for (int i = 0; i < num; i++) {
			destList.add(toBeansArgs.getClazz().newInstance());
		}
		// 根据读取数据顺序往list中塞数据
		Iterator<Map.Entry<String, List<CellData>>> it = toBeansArgs.getExcelData().getMap().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<CellData>> entry = it.next();
			String propertyName = entry.getKey();
			List<CellData> cellDataList = entry.getValue();
			for (int i = 0; i < num; i++) {
				// 个数策略
				Object value = null;
				if (i <= cellDataList.size() - 1) {
					value = cellDataList.get(i).getValue();
				}
				BeanUtils.setProperty(destList.get(i), propertyName, value);
			}
		}

		return (num < 1) ? null : destList;
	}

	/**
	 * 
	 * @param toBeansArgs
	 * @return
	 */
	public static List toParentSonBeans(ToBeansArgs toBeansArgs) throws EncryptedDocumentException, InvalidFormatException, IOException, IllegalAccessException, InstantiationException,
			InvocationTargetException {

		// 得到最终转换的条数，list实例
		toBeansArgs.initEntityNum();
		if (toBeansArgs.getEntityNum() == null) {
			return null;
		}

		int num = toBeansArgs.finalEntityNum(toBeansArgs.getExcelData().getMap());

		List destList = new ArrayList(num);// 仅仅是数据
		for (int i = 0; i < num; i++) {
			destList.add(toBeansArgs.getClazz().newInstance());
		}
		// 设置子参数对象
		Iterator<Map.Entry<String, List<CellData>>> tempIt = toBeansArgs.getExcelData().getMap().entrySet().iterator();
		List<String> selfPropertyNames = new ArrayList<String>();// xx.yy.zz的zz
		List<String> tempKeys = new ArrayList<String>();// xx.yy
		Set<String> tempUniqueKeys = new HashSet<String>();// xx
		Map<String, List<CellData>> sonMap = new HashMap<String, List<CellData>>();// 子属性
		while (tempIt.hasNext()) {
			Entry<String, List<CellData>> entry = tempIt.next();
			if (entry.getKey().contains(".")) {
				tempUniqueKeys.add(entry.getKey().split("\\.")[0]);
				tempKeys.add(entry.getKey());
				sonMap.put(entry.getKey(), entry.getValue());
			}
		}

		for (String tempUniqueKey : tempUniqueKeys) {

			for (int j = 0; j < tempKeys.size(); j++) {
				if (tempUniqueKey.equals(tempKeys.get(j).split("\\.")[0])) {
					selfPropertyNames.add(tempKeys.get(j).split("\\.")[1]);
				}
			}
			// 设置到子类参数中
			for (ToBeansSonArgs tempSonArg : toBeansArgs.getSonArgs()) {
				if (tempUniqueKey.equals(tempSonArg.getPropertyName())) {
					tempSonArg.setSelfPropertyNames(selfPropertyNames);
				}
			}
			selfPropertyNames = new ArrayList<String>();
		}
		// TODO toBeansArgs.getSonArgs()必须配置clazz propertyName 可选配置：策略
		// 读取子实例属性：跟“父表”一样，也考虑个数策略
		for (ToBeansSonArgs tempSonArg : toBeansArgs.getSonArgs()) {
			// 初始化实体转换个数策略
			tempSonArg.initEntityNum();
			if (tempSonArg.getEntityNum() == null) {
				break;
			}
			// 获取属性某个子属性的map数据集
			Map sonProDataMap = new HashMap();
			Iterator<Map.Entry<String, List<CellData>>> sonIt = sonMap.entrySet().iterator();
			while (sonIt.hasNext()) {
				Map.Entry<String, List<CellData>> entry = sonIt.next();
				String key = entry.getKey();
				List<CellData> value = entry.getValue();
				if (tempSonArg.getSelfPropertyNames().contains(key.split("\\.")[1])) {
					sonProDataMap.put(key, value);
				}
			}
			// 最终转换个数
			int sonNum = tempSonArg.finalEntityNum(sonProDataMap);

			List sonDestList = new ArrayList(sonNum);
			for (int i = 0; i < sonNum; i++) {
				sonDestList.add(tempSonArg.getClazz().newInstance());
			}
			Iterator<Map.Entry<String, List<CellData>>> it2 = toBeansArgs.getExcelData().getMap().entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry<String, List<CellData>> entry = it2.next();
				String propertyName = entry.getKey();
				if (propertyName.contains(".") && tempSonArg.getSelfPropertyNames().contains(propertyName.split("\\.")[1]) && tempSonArg.getPropertyName().equals(propertyName.split("\\.")[0])) {

					List<CellData> cellDataList = entry.getValue();
					for (int i = 0; i < sonNum; i++) {
						// 读取指定个数的数据
						Object value = null;
						if (i <= cellDataList.size() - 1) {
							value = cellDataList.get(i).getValue();
						}
						BeanUtils.setProperty(sonDestList.get(i), propertyName.split("\\.")[1], value);
					}
					it2.remove();
				}
			}

			for (Object temp : destList) {
				BeanUtils.setProperty(temp, tempSonArg.getPropertyName(), sonDestList);
			}
		}

		// 整合父数据
		Iterator<Map.Entry<String, List<CellData>>> it = toBeansArgs.getExcelData().getMap().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<CellData>> entry = it.next();
			String propertyName = entry.getKey();
			if (!toBeansArgs.existNameInSonArgs(propertyName)) {

				List<CellData> cellDataList = entry.getValue();
				for (int i = 0; i < num; i++) {
					// 个数策略
					Object value = null;
					if (i <= cellDataList.size() - 1) {
						value = cellDataList.get(i).getValue();
					}
					BeanUtils.setProperty(destList.get(i), propertyName, value);
				}
				it.remove();
			}
		}

		return (num < 1) ? null : destList;
	}

	/**
	 * 参数是否合法
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午5:04:47
	 * @param excelInputStream
	 * @param xmlInputStream
	 * @param sheetIndex
	 * @return
	 */
	private static boolean checkArgLegal(InputStream excelInputStream, InputStream xmlInputStream, Integer sheetIndex) {
		if (excelInputStream == null || xmlInputStream == null || sheetIndex == null || sheetIndex < 0) {
			return false;
		}
		return true;
	}

}
