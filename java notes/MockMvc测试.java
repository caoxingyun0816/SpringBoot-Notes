Mock Mvc 测试
spring MVC 和 spring boot 测试controller差别不大，一个需要加载配置文件，一个自动加载。

1.springboot
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring/applications.xml"}) springmvc 测试
@SpringBootTest
@AutoConfigureMockMvc
//配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
//@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true)
public class MockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mockMvcGO() throws Exception {
       String string =  mockMvc.perform(MockMvcRequestBuilders.get("/girls").contentType(MediaType.APPLICATION_FORM_URLENCODED)
		   // //.get/.post/.delete/.put直接指定请求的方法类型
        .param("age","20")//添加参数
        .param("cupSize","B"))
                .andExpect(MockMvcResultMatchers.status().isOk()) ////返回的状态是200
                //.andDo() //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();//将相应的数据转换为字符串
        System.out.println(string);
    }
}

2.spring MVC测试
RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-*xml"})

//配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

@WebAppConfiguration
public class Test {
    //记得配置log4j.properties ,的命令行输出水平是debug
    protected Log logger= LogFactory.getLog(TestBase.class);

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @org.junit.Test
    public void getAllCategoryTest() throws Exception {
        String responseString = mockMvc.perform(
                get("/categories/getAllCategory")    //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)  //数据的格式
　　　　　　　　　　　　　　 .param("pcode","root")         //添加参数
        ).andExpect(status().isOk())    //返回的状态是200
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("--------返回的json = " + responseString);
    }

}

Spring MVC的测试往往看似比较复杂。其实他的不同在于，他需要一个ServletContext来模拟我们的请求和响应。但是Spring也针对Spring MVC 提供了请求和响应的模拟测试接口，以方便我们的单元测试覆盖面不只是service，dao层

@webappconfiguration是一级注释，用于声明一个ApplicationContext集成测试加载WebApplicationContext。作用是模拟ServletContext

@ContextConfiguration：因为controller，component等都是使用注解，需要注解指定spring的配置文件，扫描相应的配置，将类初始化等。

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
上面两句的作用是，让我们对数据库的操作会事务回滚，如对数据库的添加操作，在方法结束之后，会撤销我们对数据库的操作。

为什么要事务回滚?

测试过程对数据库的操作，会产生脏数据，影响我们数据的正确性
不方便循环测试，即假如这次我们将一个记录删除了，下次就无法再进行这个Junit测试了，因为该记录已经删除，将会报错。
如果不使用事务回滚，我们需要在代码中显式的对我们的增删改数据库操作进行恢复，将多很多和测试无关的代码 
方法解析：

perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
get:声明发送一个get请求的方法。MockHttpServletRequestBuilder get(String urlTemplate, Object... urlVariables)：根据uri模板和uri变量值得到一个GET请求方式的。另外提供了其他的请求的方法，如：post、put、delete等。
param：添加request的参数，如上面发送请求的时候带上了了pcode = root的参数。假如使用需要发送json数据格式的时将不能使用这种方式，可见后面被@ResponseBody注解参数的解决方法
andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确（对返回的数据进行的判断）；
andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台（对返回的数据进行的判断）；
andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理（对返回的数据进行的判断）；

