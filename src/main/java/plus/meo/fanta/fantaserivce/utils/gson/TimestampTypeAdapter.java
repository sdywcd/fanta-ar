package plus.meo.fanta.fantaserivce.utils.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Created with sdywcd@gmail.com
* Author:sdywcd@gmail.com
* Date:15/7/23
* Time:下午3:41
* Note:Gson日期转换
**/
public class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp>  {
	 private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public Timestamp deserialize(JsonElement json, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		 if (!(json instanceof JsonPrimitive)) {
             throw new JsonParseException("The date should be a string value");
         }
         try {
             Date date = format.parse(json.getAsString());
             return new Timestamp(date.getTime());
         } catch (ParseException e) {
             throw new JsonParseException(e);
         }
	}

	@Override
	public JsonElement serialize(Timestamp src, Type arg1,
			JsonSerializationContext arg2) {
		String dateFormatAsString = format.format(new Date(src.getTime()));
        return new JsonPrimitive(dateFormatAsString);
	}

}
