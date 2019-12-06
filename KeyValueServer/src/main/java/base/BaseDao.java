package base;

import producers.MapProducer;

import java.util.concurrent.ConcurrentHashMap;

public class BaseDao {
    ConcurrentHashMap<String, String> map = MapProducer.getMap();

    public void putValue(String key, String value){
        map.put(key, value);
    }

    public String getValue(String key){
        String value = map.get(key);

        if(value == null)
            value = "Элемента с таким ключом не существует";

        return value;
    }

    public void deleteValue(String key){
        map.remove(key);
    }
}