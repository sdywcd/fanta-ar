package plus.meo.fanta.fantaserivce.utils.gson;

import com.google.gson.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
* Created with sdywcd@gmail.com
* Author:sdywcd@gmail.com
* Date:15/7/23
* Time:下午3:40
* Note:Gson和Mysql的日期转换方法
**/
public class GsonJson {
	
	public static String parseDataToJson(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).registerTypeAdapter(java.sql.Date.class, new SQLDateTypeAdapter()).create();
		return gson.toJson(obj);
	}

	public static <T> List<T> parseJsonToData(String json,Class<T> t){
		List<T>list=new ArrayList<T>();
		JsonArray array=new JsonParser().parse(json).getAsJsonArray();
		for(final JsonElement elem:array){
			list.add(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).registerTypeAdapter(java.sql.Date.class, new SQLDateTypeAdapter()).create().fromJson(elem, t));
		}
		return list;
	}

	public static <T> T parsejsonToObject(String jsonData, Class<T> type) {
		Gson gson = new Gson();
		T result = gson.fromJson(jsonData, type);
		return result;
	}

}
