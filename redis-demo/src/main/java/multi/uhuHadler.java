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
public class uhuHadler extends AbsHandler{
    @Override
    public void doA(String name){
        System.out.println("wuhu");;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Factory.register("wuhu",this);
    }
}
