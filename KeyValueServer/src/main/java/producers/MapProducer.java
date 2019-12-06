package producers;

import java.util.concurrent.ConcurrentHashMap;

public class MapProducer {
    private static ConcurrentHashMap<String, String> map;

    public static ConcurrentHashMap<String, String> getMap(){
        if (map == null) {
            try {
                map = new ConcurrentHashMap<>();
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        return map;
    }
}
