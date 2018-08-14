Swagger ��һ���淶�������Ŀ�ܣ��������ɡ����������úͿ��ӻ� RESTful ���� Web ��������Ŀ����ʹ�ͻ��˺��ļ�ϵͳ��Ϊ��������ͬ�����ٶ������¡��ļ��ķ�����������ģ�ͽ��ܼ��ɵ��������˵Ĵ��룬����API��ʼ�ձ���ͬ����
��ʼ
1��pom.xml ���������

<!-- swagger RESTful API �ĵ� -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.2.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.2.2</version>
</dependency>

2������ User ʵ��
package com.sam.demo.domain;

/**
 * @author sam
 * @since 2017/7/17
 */
public class User {

    private Long id;
    private String name;
    private int age;

    //getter & setter
}

3���� Application.java ͬ������ Swagger2.java
package com.sam.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sam
 * @since 2017/7/17
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Sam ��Ŀ�ӿ��ĵ�")
                .description("Magical Sam ��Ŀ�Ľӿ��ĵ�������RESTful API��")
                .version("1.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sam.demo.controller")) //��ɨ����ķ�ʽ
                .paths(PathSelectors.any())
                .build();
    }

}

���� .apis(RequestHandlerSelectors.basePackage("com.sam.demo.controller")) ָ������ɨ����ķ�ʽ���У����com.sam.demo.controller���µ�controller��ɨ�赽��

4������ UserController.java

package com.sam.demo.controller;

import com.sam.demo.domain.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sam
 * @since 2017/7/14
 */
@Api(value = "�û�ģ��")
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * ��ȡ�����û�
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "��ȡ�����û�", notes = "����id��ȡ�����û�")
//    @ApiImplicitParam(name = "id", value = "�û�id", required = true, paramType = "path", dataType = "Long") //ע�⣺paramType��Ҫָ��Ϊpath,��Ȼ����������ȡ
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String user(@ApiParam(value = "�û�Id", required = true) @PathVariable Long id) {
        return "user id :" + id;
    }

    /**
     * ��ȡ�û��б�
     *
     * @return
     */
    @ApiOperation(value = "��ȡ�û��б�", notes = "��ȡ�û��б�")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List list() {
        List list = new ArrayList();
        list.add("Sam1");
        list.add("Sam2");
        list.add("Sam3");
        return list;
    }

    /**
     * �½��û�
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "�½��û�", notes = "�½�һ���û�")
//    @ApiImplicitParams({
              //ע�⣺paramType��Ҫָ��Ϊbody
//            @ApiImplicitParam(name = "user", value = "�û�����", required = true, paramType = "body", dataType = "User") 
//    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(@ApiParam(value = "�û�����", required = true) @RequestBody User user) {
        System.out.println("user : " + user.getName() + " " + user.getAge());
        return "success �½�user : " + user.getName() + " " + user.getAge();
    }


    /**
     * ɾ���û�
     *
     * @return
     */
    @ApiOperation(value = "ɾ���û�", notes = "ͨ���û�idɾ���û�")
    @ApiImplicitParam(name = "id", value = "�û�id", required = true, paramType = "path", dataType = "Long")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long id) {
        System.out.println("ɾ���û���" + id);
        return "success ɾ��user" + id;
    }


    /**
     * �����û�
     *
     * @return
     */
    @ApiOperation(value = "�����û�", notes = "�����Ѵ����û�")
    @ApiImplicitParam(name = "user", value = "�û�����", required = true, paramType = "body", dataType = "User")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public String update(@RequestBody User user) {
        System.out.println("�����û���" + user.getId() + " " + user.getName() + " " + user.getAge());
        return "success ����user : " + user.getId() + " " + user.getName() + " " + user.getAge();
    }

}

����Ӧ�ã���������ʣ�http://localhost:8080/swagger-ui.html

���ɿ���API�ĵ�

ע��1��@ApiImplicitParam �� @ApiParam ��ʽ����ָ����������
ע��2��ʹ��@ApiImplicitParam��ʱ����Ҫָ��paramType��

@Api���������ϣ�˵�����������
@ApiOperation�����ڷ����ϣ�˵������������
@ApiImplicitParams�����ڷ����ϰ���һ�����˵��
@ApiImplicitParam������ @ApiImplicitParams ע���У�ָ��һ����������ĸ�������
    paramType�����������ĸ��ط�
        �� header --> ��������Ļ�ȡ��@RequestHeader
        �� query -->��������Ļ�ȡ��@RequestParam
        �� path������restful�ӿڣ�--> ��������Ļ�ȡ��@PathVariable
        �� body�������ã�
        �� form�������ã�
    name��������
    dataType����������
    required�������Ƿ���봫
    value����������˼
    defaultValue��������Ĭ��ֵ
@ApiResponses�����ڱ�ʾһ����Ӧ
@ApiResponse������@ApiResponses�У�һ�����ڱ��һ���������Ӧ��Ϣ
    code�����֣�����400
    message����Ϣ������"�������û���"
    response���׳��쳣����
@ApiModel������һ��Model����Ϣ������һ������post������ʱ��ʹ��@RequestBody�����ĳ�������������޷�ʹ��@ApiImplicitParamע�����������ʱ��
@ApiModelProperty������һ��model������


