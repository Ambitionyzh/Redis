package multi;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author yongzh
 * @version 1.0
 * @program: DesignPattern
 * @description:
 * @date 2023/8/2 23:40
 */
public class Factory {
    private static Map<String, AbsHandler> strategyMap = new HashMap<>();

    public static AbsHandler getInvokeStrategy(String name){
        return strategyMap.get(name);
    }
    public static void  register(String name,AbsHandler handler){
        if(StringUtils.isEmpty(name) || null == handler){
            return;
        }
        strategyMap.put(name,handler);
    }

}
