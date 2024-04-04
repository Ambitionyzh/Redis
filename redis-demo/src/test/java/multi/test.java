package multi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yongzh
 * @version 1.0
 * @program: DesignPattern
 * @description:
 * @date 2023/8/2 23:49
 */
@SpringBootTest(classes = dsmHadler.class)
 class test {
    @Test
    void design(){
        String name = "dsm";
        AbsHandler strategy = Factory.getInvokeStrategy(name);
        System.out.println(strategy.doB(name));
    }
    @Test
    void designA(){
        String name = "wuhu";
        AbsHandler strategy = Factory.getInvokeStrategy(name);
        strategy.doA(name);
        System.out.println(strategy.doB(name));

    }
}
