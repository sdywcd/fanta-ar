package plus.meo.fanta.fantaserivce.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化处理
* @ClassName: DateTools 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author jcchen
* @date 2015年10月31日 上午10:37:04 
*
 */
public class DateTools {
	private static final Logger log = LoggerFactory.getLogger(DateTools.class);

	/**
	 * 日期格式化yy-MM-dd
	 */
	public static final String DF_FF_MM_DD="yy-MM-dd";

	/**
	 * 日期格式化yyyyMMddHHmmss
	 */
	public static final String DF_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	/**
	 * 日期格式化yyyy-MM-dd
	 */
	public static final String DF_YYYY_MM_DD="yyyy-MM-dd";
	/**
	 * 日期格式化yyyy-MM-dd HH:mm:ss
	 */
	public static final String DFYYYYMMDDHHMMSS="yyyy/MM/dd HH:mm:ss";
	/**
	 * 日期格式化yyyyMMdd
	 */
	public static final String DF_YYYYMMDD = "yyyyMMdd";


	/**
	 * 日期格式化yyyy-MM-dd HH:mm:ss
	 */
	public static final String DF_YYYY_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";

	/**
	 * 设置日期格式
	 *
	 * @return
	 */
	public static String tagdate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYYMMDD);
		String time = formatter.format(cal.getTime()).toString();
		return time;
	}


	/**
	 * 获取系统时间 BigInteger
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static BigInteger getSystemTimeBi() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS,Locale.CHINA);
		String dateString = formatter.format(date);
		ParsePosition pos = new ParsePosition(0);
		Date currenttime = formatter.parse(dateString, pos);
		return BigInteger.valueOf(currenttime.getTime());
	}

	/**
	 * 获取系统时间 long
	 *
	 * @return
	 * @throws ParseException
	 */
	public static long getSystemTimeLong() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS,Locale.CHINA);
		String dateString = formatter.format(date);
		ParsePosition pos = new ParsePosition(0);
		Date currenttime = formatter.parse(dateString, pos);
		return currenttime.getTime();
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date getSystemTime() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS,Locale.CHINA);
		String dateString = formatter.format(date);
		ParsePosition pos = new ParsePosition(0);
		Date currenttime = formatter.parse(dateString, pos);
		return currenttime;
	}

	/**
	 * 转换数据库日期格式
	 * 
	 * @param object
	 * @return
	 */
	public static String formateDbDate(Date object) {
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS,Locale.CHINA);
		String dateString = formatter.format(object);
		return dateString;
	}
	
	
	
	public static Date formatString2DateYYMMDD(String dateString) throws ParseException {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat(DF_FF_MM_DD, Locale.CHINA);
		dateFormat.setLenient(false);
		Date timeDate = dateFormat.parse(dateString);
		Date dateTime = new Date(timeDate.getTime());
		return dateTime;
	}
	
	/**
	 * 格式化日期
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date formatString2DateYYYMMDDHHMMSS(String str) throws ParseException{
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS, Locale.CHINA);
		dateFormat.setLenient(false);
		String strs= StringUtils.replaceChars(str, "/", "-");
		Date timeDate = dateFormat.parse(strs);
		Date dateTime = new Date(timeDate.getTime());
		return dateTime;
	}

	/**
	 * 格式化日期年月日时分秒
	 * @param bitime
	 * @return
     */
	public static String formateLongDateToString(long bitime){
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS,Locale.CHINA);
		Long time =new Long(bitime);
		String d = formatter.format(time);
		return d;
	}

	/**
	 * 格式化日期年月日
	 * @param bitime
	 * @return
	 */
	public static String formateLongDateToStringYYYYMMDD(long bitime){
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYY_MM_DD,Locale.CHINA);
		Long time =new Long(bitime);
		String d = formatter.format(time);
		return d;
	}

	/**
	 * 格式化日期年月日
	 * @param bitime
	 * @return
	 */
	public static String formatLongDateToStringYYYYMMDD(long bitime){
		SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYYMMDD,Locale.CHINA);
		Long time =new Long(bitime);
		String d = formatter.format(time);
		return d;
	}

	/**
	 * 返回日期的毫秒
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static long formatStringDate2Long(String str) throws ParseException{
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS, Locale.CHINA);
		dateFormat.setLenient(false);
		String strs= StringUtils.replaceChars(str, "/", "-");
		long mills=dateFormat.parse(strs).getTime();
		return mills;
	}

	/**
	 * 返回日期的毫秒
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static long formatStringDateYYYYMMDD2Long(String str) throws ParseException{
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD, Locale.CHINA);
		dateFormat.setLenient(false);
		String strs= StringUtils.replaceChars(str, "/", "-");
		long mills=dateFormat.parse(strs).getTime();
		return mills;
	}
}
