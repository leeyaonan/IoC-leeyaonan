import com.leeyaonan.config.MyAnnotationConfigApplicationContext;
import com.leeyaonan.factory.ProxyFactory;
import com.leeyaonan.service.TransferService;
import org.junit.Test;

/**
 * @Author leeyaonan
 * @Date 2020/4/14 8:42
 */
public class TestIoC {

    @Test
    public void test() throws Exception {
        MyAnnotationConfigApplicationContext applicationContext = new MyAnnotationConfigApplicationContext("com.leeyaonan");
        ProxyFactory proxyFactory = (ProxyFactory) applicationContext.getBean("proxyFactory");
        TransferService transferService = (TransferService) proxyFactory.getJdkProxy(applicationContext.getBean("transferService"));
        System.out.println(proxyFactory);
        System.out.println(transferService);
    }
}
