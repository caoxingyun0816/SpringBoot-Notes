Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。总体目标是使客户端和文件系统作为服务器以同样的速度来更新。文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。
开始
1、pom.xml 添加依赖：

<!-- swagger RESTful API 文档 -->
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

2、创建 User 实体
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

3、在 Application.java 同级创建 Swagger2.java
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
                .title("Sam 项目接口文档")
                .description("Magical Sam 项目的接口文档，符合RESTful API。")
                .version("1.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sam.demo.controller")) //以扫描包的方式
                .paths(PathSelectors.any())
                .build();
    }

}

其中 .apis(RequestHandlerSelectors.basePackage("com.sam.demo.controller")) 指定了以扫描包的方式进行，会把com.sam.demo.controller包下的controller都扫描到。

4、创建 UserController.java

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
@Api(value = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取单个用户
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取单个用户", notes = "传入id获取单个用户")
//    @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataType = "Long") //注意：paramType需要指定为path,不然不能正常获取
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String user(@ApiParam(value = "用户Id", required = true) @PathVariable Long id) {
        return "user id :" + id;
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List list() {
        List list = new ArrayList();
        list.add("Sam1");
        list.add("Sam2");
        list.add("Sam3");
        return list;
    }

    /**
     * 新建用户
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "新建用户", notes = "新建一个用户")
//    @ApiImplicitParams({
              //注意：paramType需要指定为body
//            @ApiImplicitParam(name = "user", value = "用户数据", required = true, paramType = "body", dataType = "User") 
//    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(@ApiParam(value = "用户数据", required = true) @RequestBody User user) {
        System.out.println("user : " + user.getName() + " " + user.getAge());
        return "success 新建user : " + user.getName() + " " + user.getAge();
    }


    /**
     * 删除用户
     *
     * @return
     */
    @ApiOperation(value = "删除用户", notes = "通过用户id删除用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataType = "Long")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long id) {
        System.out.println("删除用户：" + id);
        return "success 删除user" + id;
    }


    /**
     * 更新用户
     *
     * @return
     */
    @ApiOperation(value = "更新用户", notes = "更新已存在用户")
    @ApiImplicitParam(name = "user", value = "用户数据", required = true, paramType = "body", dataType = "User")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public String update(@RequestBody User user) {
        System.out.println("更新用户：" + user.getId() + " " + user.getName() + " " + user.getAge());
        return "success 更新user : " + user.getId() + " " + user.getName() + " " + user.getAge();
    }

}

启动应用，浏览器访问：http://localhost:8080/swagger-ui.html

即可看到API文档

注意1：@ApiImplicitParam 和 @ApiParam 方式均能指定参数规则。
注意2：使用@ApiImplicitParam的时候，需要指定paramType。

@Api：用在类上，说明该类的作用
@ApiOperation：用在方法上，说明方法的作用
@ApiImplicitParams：用在方法上包含一组参数说明
@ApiImplicitParam：用在 @ApiImplicitParams 注解中，指定一个请求参数的各个方面
    paramType：参数放在哪个地方
        · header --> 请求参数的获取：@RequestHeader
        · query -->请求参数的获取：@RequestParam
        · path（用于restful接口）--> 请求参数的获取：@PathVariable
        · body（不常用）
        · form（不常用）
    name：参数名
    dataType：参数类型
    required：参数是否必须传
    value：参数的意思
    defaultValue：参数的默认值
@ApiResponses：用于表示一组响应
@ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
    code：数字，例如400
    message：信息，例如"请求参数没填好"
    response：抛出异常的类
@ApiModel：描述一个Model的信息（这种一般用在post创建的时候，使用@RequestBody这样的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
@ApiModelProperty：描述一个model的属性


