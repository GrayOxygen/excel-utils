
package com.saysth.excel.validtor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

import com.saysth.excel.domain.CellData;
import com.saysth.excel.enumconstants.ValidateError;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年9月17日 上午9:55:11
 * 
 * @Description 校验规则
 * 
 *              注意：验证诸如email url 中文字符串 等等规则时，默认校验了是否为空，xml配置文件中可不必要重复写required
 */
public class ValidatorRule {
	private Boolean required;// 不能为null、空字符串
	private Boolean email;
	private Boolean url;
	// 如"yyyy-MM-dd HH:mm:ss" 其中yyyy表示年 MM表示月 dd表示日 HH表示小时 mm表示分 ss表示秒
	private String dateFormat;
	private Boolean number;// true为数字（整数、小数）
	private Boolean integer;// 整数
	private Boolean decimal;// 小数
	private String enStr;// 英文字符串
	private String cnStr;// 中文字符串
	private String equalTo;// TODO 暂不支持
	private Boolean acceptExtension;// "png" 指定字符串的后缀名
	private Integer maxLength;// "11" 字符最大长度
	private Integer minLength;// "2" 字符最小长度
	private String rangeLength;// "[1,2]" 合法的字符串长度区间
	private String range;// "[2,3]" 合法的数字大小区间
	private Double max;// "123" 最大值
	private Double min;// "1.231" 最小值

	/**
	 * 验证单个单元格数据
	 * 
	 * @author 王辉阳
	 * @date 2015年9月17日 下午1:56:17
	 * @param cellData
	 *            单元格对象（必须包含数据值）
	 * @return 校验信息
	 */
	public List<ValidatorMsg> getValidatorMsg(CellData cellData) {
		List<ValidatorMsg> validatorMsgList = new ArrayList<ValidatorMsg>();
		// 不为空的属性，进行校验
		Field[] fileds = this.getClass().getDeclaredFields();
		for (int i = 0; i < fileds.length; i++) {
			String fieldName = fileds[i].getName();
			try {
				String postName = String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1, fieldName.length());
				Method method = this.getClass().getMethod("get" + postName);
				if (method.invoke(this) != null) {
					Method validateMethod = this.getClass().getMethod("validate" + postName, Object.class);
					ValidatorMsg validatorMsg = (ValidatorMsg) validateMethod.invoke(this, cellData.getValue());
					validatorMsgList.add(validatorMsg);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}

		return validatorMsgList;
	}

	private boolean isEmpty(Object obj) {
		if (obj == null || obj.toString() == null || obj.toString().length() < 1) {
			return true;
		}
		return false;
	}

	public ValidatorMsg validateRequired(Object val) {
		ValidatorMsg validatorMsg = new ValidatorMsg();
		if (isEmpty(val)) {
			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("数据值不能为空");
		}
		return validatorMsg;
	}

	public ValidatorMsg validateEmail(Object obj) {
		return validiteRegex(obj, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的邮件格式！");
	}

	public ValidatorMsg validateUrl(Object obj) {
		return validiteRegex(obj, "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的url格式！");
	}

	public ValidatorMsg validateDateFormat(Object obj) {
		ValidatorMsg validatorMsg = new ValidatorMsg();
		if (isEmpty(obj)) {
			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("非法的日期格式！");
			return validatorMsg;
		}
		SimpleDateFormat df = new SimpleDateFormat(this.dateFormat);
		df.setLenient(false);// 宽松模式：不把2015-13-13 转换为2015-1-1
		try {
			df.parse(obj.toString());
		} catch (Exception e) {
			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("非法的日期格式！");
			return validatorMsg;
		}
		return validatorMsg;
	}

	public ValidatorMsg validateNumber(Object obj) {
		return validiteRegex(obj, "^[0-9]+\\.{0,1}[0-9]*$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的数字！");
	}

	public ValidatorMsg validateEqualTo(Object val) {
		return null;
	}

	public ValidatorMsg validateAcceptExtension(Object obj) {
		return validiteRegex(obj, "^[\\s\\S]*\\." + this.acceptExtension + "*$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的后缀名！");
	}

	public ValidatorMsg validateMaxLength(Object obj) {
		ValidatorMsg validatorMsg = new ValidatorMsg();
		if (isEmpty(obj) || obj.toString().length() > maxLength) {
			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("最大长度不能超过" + maxLength + "位！");
		}
		return validatorMsg;
	}

	public ValidatorMsg validateMinLength(Object obj) {
		ValidatorMsg validatorMsg = new ValidatorMsg();
		if (isEmpty(obj) || obj.toString().length() < minLength) {
			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("最小长度不能少于" + minLength + "位！");
		}
		return validatorMsg;
	}

	public ValidatorMsg validateRangeLength(Object obj) {// "[1,2]"
		ValidatorMsg validatorMsg = new ValidatorMsg();
		try {

			if (isEmpty(obj) || //
					obj.toString().length() < Integer.parseInt(rangeLength.split(",")[0].split("[")[1].trim().toString()) || //
					obj.toString().length() > Integer.parseInt(rangeLength.split(",")[1].split("]")[0].trim().toString())) {
				validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
				validatorMsg.setErrMsg("最小长度不能少于" + //
						Integer.parseInt(rangeLength.split(",")[0].split("[")[1].trim().toString() + //
								"位！" + "最大长度不能超过" + //
								Integer.parseInt(rangeLength.split(",")[1].split("]")[0].trim().toString()) + "位！"));
			}
		} catch (Exception e) {
			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("rangeLength规则不合法");
			return validatorMsg;
		}
		return validatorMsg;
	}

	public ValidatorMsg validateRange(Object obj) {// "[1,2]"
		ValidatorMsg validatorMsg = new ValidatorMsg();
		if (isEmpty(obj) || //
				Double.valueOf(obj.toString()) < Integer.parseInt(range.split(",")[0].split("[")[1].trim().toString()) || //
				Double.valueOf(obj.toString()) > Integer.parseInt(range.split(",")[1].split("]")[0].trim().toString())) {
			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("最小值不能小于" + //
					Integer.parseInt(range.split(",")[0].split("[")[1].trim().toString() + //
							"位！" + "最大值不能大于" + //
							Integer.parseInt(range.split(",")[1].split("]")[0].trim().toString()) + "位！"));
		}
		return null;
	}

	public ValidatorMsg validateMax(Object obj) {
		ValidatorMsg validatorMsg = new ValidatorMsg();
		if (isEmpty(obj) || // 空
				(validateNumber(obj).getCode() != ValidateError.OK.getValue()) || // 非数字
				Double.valueOf(obj.toString()) > this.max/* 大于最大值 */) {

			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("最大值校验不通过！");
		}

		return validatorMsg;
	}

	public ValidatorMsg validateMin(Object obj) {
		ValidatorMsg validatorMsg = new ValidatorMsg();
		if (isEmpty(obj) || // 空
				(validateNumber(obj).getCode() != ValidateError.OK.getValue()) || // 非数字
				Double.valueOf(obj.toString()) < this.min/* 小于最大值 */) {

			validatorMsg.setCode(ValidateError.NOT_PASS_VALID_ERROR.getValue());
			validatorMsg.setErrMsg("最小值校验不通过！");
		}

		return validatorMsg;
	}

	public ValidatorMsg validateInteger(Object obj) {
		return validiteRegex(obj, "^-?\\d+$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的整数！");
	}

	public static void main(String[] args) {
		String text = "1996-0-3 13:23:23";
		Date d = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setLenient(false);// 这个的功能是不把1996-13-3 转换为1997-1-3
		try {
			d = df.parse(text);
		} catch (Exception e) {
			System.out.println("你输入的日期不合法，请重新输入");
		}
		String sdata = df.format(d);
		System.out.println(sdata);

	}

	public ValidatorMsg validateDecimal(Object obj) {
		return validiteRegex(obj, "^-?[0-9]+\\.\\d+$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的小数！");
	}

	public ValidatorMsg validateEnStr(Object obj) {
		return validiteRegex(obj, "^[A-Za-z]+$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的英文字符串！");
	}

	public ValidatorMsg validateCnStr(Object obj) {
		return validiteRegex(obj, "^[\u4E00-\u9FA5]+$", ValidateError.NOT_PASS_VALID_ERROR.getValue(), "非法的中文字符串！");
	}

	/**
	 * 
	 * @param obj
	 *            待校验的数据值
	 * @param code
	 *            校验结果码
	 * @param errmsg
	 *            校验信息
	 * @return 校验结果对象，校验通过返回的是成功的信息，失败的是失败信息
	 */
	private ValidatorMsg validiteRegex(Object obj, String regexStr, int code, String errmsg) {
		ValidatorMsg validatorMsg = new ValidatorMsg();
		Pattern pattern = Pattern.compile(regexStr);
		if (obj == null || !pattern.matcher(obj.toString()).find()) {
			validatorMsg.setCode(code);
			validatorMsg.setErrMsg(errmsg);
		}
		return validatorMsg;
	}

	// TODO 校验是否通过
	public boolean passValidate() {
		//
		return false;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getEmail() {
		return email;
	}

	public void setEmail(Boolean email) {
		this.email = email;
	}

	public Boolean getUrl() {
		return url;
	}

	public void setUrl(Boolean url) {
		this.url = url;
	}

	public Boolean getNumber() {
		return number;
	}

	public void setNumber(Boolean number) {
		this.number = number;
	}

	public String getEqualTo() {
		return equalTo;
	}

	public void setEqualTo(String equalTo) {
		this.equalTo = equalTo;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public String getRangeLength() {
		return rangeLength;
	}

	public void setRangeLength(String rangeLength) {
		this.rangeLength = rangeLength;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Boolean getAcceptExtension() {
		return acceptExtension;
	}

	public void setAcceptExtension(Boolean acceptExtension) {
		this.acceptExtension = acceptExtension;
	}

	public Boolean getInteger() {
		return integer;
	}

	public void setInteger(Boolean integer) {
		this.integer = integer;
	}

	public Boolean getDecimal() {
		return decimal;
	}

	public void setDecimal(Boolean decimal) {
		this.decimal = decimal;
	}

	public String getEnStr() {
		return enStr;
	}

	public void setEnStr(String enStr) {
		this.enStr = enStr;
	}

	public String getCnStr() {
		return cnStr;
	}

	public void setCnStr(String cnStr) {
		this.cnStr = cnStr;
	}

}
