package plus.meo.fanta.fantaserivce.utils.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
* Created with sdywcd@gmail.com
* Author:sdywcd@gmail.com
* Date:15/7/23
* Time:下午3:42
* Note:Mysql日期转换
**/
public class SQLDateTypeAdapter implements JsonSerializer<Date> {
	  private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public JsonElement serialize(Date src, Type arg1,
			JsonSerializationContext arg2) {
		 String dateFormatAsString = format.format(new Date(src.getTime()));
         return new JsonPrimitive(dateFormatAsString);
	}

}
