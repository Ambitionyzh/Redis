package multi;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author yongzh
 * @version 1.0
 * @program: DesignPattern
 * @description:
 * @date 2023/8/2 23:41
 */
public abstract  class AbsHandler implements InitializingBean {
    public void doA(String name){
        throw  new UnsupportedOperationException();
    }
    public String doB(String name){
        throw  new UnsupportedOperationException();
    }
}
