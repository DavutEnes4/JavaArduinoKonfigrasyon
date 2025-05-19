package tr.com.my_app.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJSONValueProcessor implements JsonValueProcessor{

    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
    private DateFormat dateFormat;

    public DateJSONValueProcessor() {
        this(DEFAULT_DATE_PATTERN);
    }

    public DateJSONValueProcessor(String datePattern) {
        try {
            dateFormat = new SimpleDateFormat(datePattern);
        } catch (Exception ex) {
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        }
    }

    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        if(value != null) {
            if(value instanceof Timestamp)
                return dateFormat.format((Timestamp) value);
            else
                return dateFormat.format((Date) value);
        } else {
            return null;
        }
    }

}
