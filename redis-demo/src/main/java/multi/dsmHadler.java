package multi;

import org.springframework.stereotype.Component;

/**
 * @author yongzh
 * @version 1.0
 * @program: DesignPattern
 * @description:
 * @date 2023/8/2 23:46
 */
@Component
public class dsmHadler extends AbsHandler{
    @Override
    public String doB(String name){
        return "dsm 起飞";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Factory.register("dsm",this);
    }
}
