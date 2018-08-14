Mock Mvc ����
spring MVC �� spring boot ����controller��𲻴�һ����Ҫ���������ļ���һ���Զ����ء�

1.springboot
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring/applications.xml"}) springmvc ����
@SpringBootTest
@AutoConfigureMockMvc
//��������Ļع�,�����ݿ����ɾ�Ķ���ع�,���ڲ���������ѭ������
//@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true)
public class MockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mockMvcGO() throws Exception {
       String string =  mockMvc.perform(MockMvcRequestBuilders.get("/girls").contentType(MediaType.APPLICATION_FORM_URLENCODED)
		   // //.get/.post/.delete/.putֱ��ָ������ķ�������
        .param("age","20")//��Ӳ���
        .param("cupSize","B"))
                .andExpect(MockMvcResultMatchers.status().isOk()) ////���ص�״̬��200
                //.andDo() //��ӡ���������Ӧ������
                .andReturn().getResponse().getContentAsString();//����Ӧ������ת��Ϊ�ַ���
        System.out.println(string);
    }
}

2.spring MVC����
RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-*xml"})

//��������Ļع�,�����ݿ����ɾ�Ķ���ع�,���ڲ���������ѭ������
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

@WebAppConfiguration
public class Test {
    //�ǵ�����log4j.properties ,�����������ˮƽ��debug
    protected Log logger= LogFactory.getLog(TestBase.class);

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before()  //���������ÿ������ִ��֮ǰ����ִ��һ��
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //��ʼ��MockMvc����
    }

    @org.junit.Test
    public void getAllCategoryTest() throws Exception {
        String responseString = mockMvc.perform(
                get("/categories/getAllCategory")    //�����url,����ķ�����get
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)  //���ݵĸ�ʽ
���������������������������� .param("pcode","root")         //��Ӳ���
        ).andExpect(status().isOk())    //���ص�״̬��200
                .andDo(print())         //��ӡ���������Ӧ������
                .andReturn().getResponse().getContentAsString();   //����Ӧ������ת��Ϊ�ַ���
        System.out.println("--------���ص�json = " + responseString);
    }

}

Spring MVC�Ĳ����������ƱȽϸ��ӡ���ʵ���Ĳ�ͬ���ڣ�����Ҫһ��ServletContext��ģ�����ǵ��������Ӧ������SpringҲ���Spring MVC �ṩ���������Ӧ��ģ����Խӿڣ��Է������ǵĵ�Ԫ���Ը����治ֻ��service��dao��

@webappconfiguration��һ��ע�ͣ���������һ��ApplicationContext���ɲ��Լ���WebApplicationContext��������ģ��ServletContext

@ContextConfiguration����Ϊcontroller��component�ȶ���ʹ��ע�⣬��Ҫע��ָ��spring�������ļ���ɨ����Ӧ�����ã������ʼ���ȡ�

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
��������������ǣ������Ƕ����ݿ�Ĳ���������ع���������ݿ����Ӳ������ڷ�������֮�󣬻᳷�����Ƕ����ݿ�Ĳ�����

ΪʲôҪ����ع�?

���Թ��̶����ݿ�Ĳ���������������ݣ�Ӱ���������ݵ���ȷ��
������ѭ�����ԣ�������������ǽ�һ����¼ɾ���ˣ��´ξ��޷��ٽ������Junit�����ˣ���Ϊ�ü�¼�Ѿ�ɾ�������ᱨ��
�����ʹ������ع���������Ҫ�ڴ�������ʽ�Ķ����ǵ���ɾ�����ݿ�������лָ�������ܶ�Ͳ����޹صĴ��� 
����������

perform��ִ��һ��RequestBuilder���󣬻��Զ�ִ��SpringMVC�����̲�ӳ�䵽��Ӧ�Ŀ�����ִ�д���
get:��������һ��get����ķ�����MockHttpServletRequestBuilder get(String urlTemplate, Object... urlVariables)������uriģ���uri����ֵ�õ�һ��GET����ʽ�ġ������ṩ������������ķ������磺post��put��delete�ȡ�
param�����request�Ĳ����������淢�������ʱ���������pcode = root�Ĳ���������ʹ����Ҫ����json���ݸ�ʽ��ʱ������ʹ�����ַ�ʽ���ɼ����汻@ResponseBodyע������Ľ������
andExpect�����ResultMatcher��֤������֤������ִ����ɺ����Ƿ���ȷ���Է��ص����ݽ��е��жϣ���
andDo�����ResultHandler������������������ʱ��ӡ���������̨���Է��ص����ݽ��е��жϣ���
andReturn����󷵻���Ӧ��MvcResult��Ȼ������Զ�����֤/������һ�����첽�����Է��ص����ݽ��е��жϣ���

