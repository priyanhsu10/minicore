![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![example branch parameter](https://github.com/priyanhsu10/minicore/actions/workflows/maven.yml/badge.svg?branch=master)


minicore parent
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.priyanhsu10/minicore-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.priyanhsu10/minicore-parent/)

minicore- framework
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.priyanhsu10/minicore-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.priyanhsu10/minicore-api/)

minicore -archetype
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.priyanhsu10/minicore-archetype/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.priyanhsu10/minicore-archetype/)

# minicore-archetype
minicore-archetype for client creation


     mvn archetype:generate
      -DarchetypeGroupId=io.github.priyanhsu10 
      -DarchetypeArtifactId=minicore-archetype 
      -DarchetypeVersion=1.0.1


      

# [Minicore Documention](https://priyanhsu10.github.io/minicore-docs/) 
# Minicore Documentation

## Overview

> Minicore is a JVM-based cross-platform, high-performance, open-source framework for building modern, cloud-enabled, Internet-connected apps.

> It is heavily inspired by the Aspnet Core and Express. Currently Minicore framework is in the development phase, Basic The flow is ready for creating API :)

#### Currently implemented Features

- Pipeline Creation for request
- Routing
- Global exception handling
- Filters
    - Global level Filter configuration
    - Controller-level Filter configuration
    - Action level Filter configuration
- Content negociation using Formatters
    - Input Formatters
    - Output Formatters

> Currently I am looking for contributors for the project and need your help to make this project successful

> You can find the Github Project Requirement gathering board Link [here](https://github.com/users/priyanhsu10/projects/2)

> [GitHub Repositoy](https://github.com/priyanhsu10/minicore).

## Minicore Architecture flow

> coming soon ..
> ![The San Juan Mountains are beautiful!](/assets/Arcflow.png "Architecture diagram")

### Program Class


```javascript
public class Program {
    public static void main(String[] args) {
 WebHostBuilder.build(args)
               //configure custom properties
 .ConfigureHost(option->{
                //add your custom property file

//                       option.addPropertyFile("CustomFile.properties");

//------------------------*-Need to be implemented-----*--------------
                           //add your custom JSON file 
//                       option.addJsonFile("CustomjsonFile.json");
                        
                        //add your custom XML file
//                       option.addXmlFile("CustomxmlFile.xml");
//------------------------*-Need to be implemented------*-------------
 })

 .useStartup(AppStartup.class)

 .run();
 }
}
```


> coming soon ..

### AppStartup Class

```javascript
public class AppStartup implements IStartup {

    //Register your service with IOC Container
   
 @Override
    public void configureServices(IServiceCollection service) {
       
        //configuring MVC options
 MvcConfigurer.configureMvc(service,options->{

            // Adding your custom  Global  filters
 options.addGlobalFilter(TestGlobalActionFilter.class);

            //Adding your Custom Exception Filters
 options.addExceptionFilter(TestExceptionFilter.class);
          
            // Adding your custom Global Result filters
            // Global filter will execute for every action before writing the response
 options.addResultFilter(TestResultFilter.class);

 });
        
        //Register your dependencies with the IOC Container like the below
 service.addSingleton(ITestService.class, TestService.class);


 }
//build your pipeline
 @Override
    public void configure(IApplicationBuilder app) {
        
 app.use(TransactionIdMiddleware.class);-->//Custom Middleware added in the pipeline 
       
       //route endPoint selector
 app.useRouting(); 
       
        //add custom middlewares
 app.use(CustomMiddleware.class);

        ...  --> Other custom middlewares
       
 app.useEndpoints();//Endpoint executor
 }

}
```
> coming soon ..

### Setting up Pipeline



> In the startup class, the pipeline can be configured in the configure method.
```javascript
public class AppStartup implements IStartup {

   ...
//build your pipeline
 @Override
    public void configure(IApplicationBuilder app) {


      //create your own pipeline :D
 app.use(TransactionIdMiddleware.class);
      
 app.useRouting(); //route EndPoint selector
      

        //add custom middlewares
 app.use(CustomMiddleware.class);

         ...  --> Other custom middleware


 App.useEndpoints();//Endpoint executor (This should be the last middleware in the pipeline because after this next middleware won't call)
 }

}

```
### Controllers
> You can create the controller by extending the controllerBase class


```javascript

@Route(path = "/hello") --> @Route Annoation setut the base route for Controller
@ActionFilter(filterClass = TestActionFilter2.class) ---> Controller Level Action filter . this is executed On every action execution in the controller
public class HelloController  extends ControllerBase {
    private ITestService testService;

    public HelloController(ITestService testService) { ---> Inject Dependency using Contructor Injection 
        this.testService = testService;
 }

 @Get       //-->  Using @Get attribute can specify Get Method 
 @ActionFilter(filterClass = TestActionFilter.class) --> //apply custom filter for specific action
 @ResultFilter(filterClass = CustomResultFilter.class)  --> //custom Result Filter Which Before Result Execution. at this point, you can change the response.

    public List<Model> get() {
        //accesing transaction id which is added by transactionIdMiddlware
 System.out.println(httpContext.getData().get("trazId"));
        return this.testService.getlist();

 }

 @Get(path = "/:id")   --> //we can specify route templete like this  eg "<route>/:{parameter from route}" this case route will be hello/:id
    public Model get(int id) {
        return this.testService.getById(id);
 }

 @Get(path = "/filter") //value bind from query
    public List<Model> get(@FromQuery String name) {
        return this.testService.getlist().stream().filter(x->x.getName().equals(name)).collect(Collectors.toList());
 }
 @Post
    //Direct Model binding use @FromBody annotation
    public Model post(@FromBody Model model){
        return testService.create(model);
 }
 @Post(path = "/m2")
    //Custom ModelBiding
    public Model post( Model2 model){
        return model.getModel();
 }
 @Put(path = "/update")
    public String put(String name){
        return "this is post "+name;
 }
 @Delete(path = "/delete")
    public String delete(String name){
        return "this is post "+name;
 }

}
```

> comminig soon ..

> comminig soon ..


## Turotials

> comminig soon ..

## Fundametals

> comminig soon ..

### App Startup

> comminig soon ..

### Application configuration

> Minicore can pick the configuration in the following order
1. app.properties
2. app.{profile}.properties
3. custom property configuration provider
4. Environment variables entries
5. Command line Arguments -m_{propertykey}=value

```javascript
public class Program {
    public static void main(String[] args) {
 WebHostBuilder.build(args)
               //configure custom properties
 .ConfigureHost(option->{
                //add your custom property file

//                       option.addPropertyFile("CustomFile.properties");
                           //add your custom JSON file
//                       option.addJsonFile("CustomjsonFile.json");
                        
                        //add your custom XML file
//                       option.addXmlFile("CustomxmlFile.xml");
 options.custom(()->{
 Map<Object,Object> customProperties= new HashMap<>();
                                    //custom keys 
                                    ....
                                    return customProperties;


 }));

 })

 .useStartup(AppStartup.class)

 .run();
 }
}
```
### Dependency Injection

> Micore proved the following dependency type to be registered using
1. Singleton
2. RequestScope
3. Transient

> The IServiceCollection interface provides the functionality to resolve them and register the types to the IOC container.
```javascript

public class AppStartup implements IStartup {

    //Register your service with IOC Container
   
 @Override
    public void configureServices(IServiceCollection service) {
       
        //Register your dependencies with the IOC Container like below
 service.addSingleton(ITestService.class, TestService.class);


 }

.....

Use the dependencies 
public class HelloController  extends ControllerBase {
    private ITestService testService;

    public HelloController(ITestService testService) { ---> Inject Dependency using Contructor Injection 
        this.testService = testService;
 }

```

### Middleware

> coming soon ..
![The San Juan Mountains are beautiful!](/assets/midleware.png "Architecture diagram")

### Routing

> Http Route Annotations
1. @Route
2. @Get
3. @Put
4. @Post
5. @Delete

#### Model Binding

> Model binding can be performed in using the following annotations

1. @FromBody
2. @FromForm
3. @FromRoute
4. @FromQuery

> Custom model biding Model

```javascript
@Post(path = "/m2")
    //Custom ModelBiding
 public Model post( Model2 model){
        return model.getModel();
 }
....

//Custom ModelBiding
public class Model2 {
   
    //value bind from header
 @FromHeader(Key = "Authorization")
    private String token;

   //value bind from Query
 @FromQurey
    private String name;

    //value bind from Route
 @FromRoute
    private String id;

    //value bind from Body
 @FromBody
    private Model model;

    ....
 setter getter

}

```

#### Route Contraints

> coming soon ..

### Filters


> Filters allow to run the code at various stage

> Minicor provides 4 types of filters

1. Authorization filter [need to be implemented]
2. Action Filter
3. Exception Filter
4. Result Filter

#### Authorization filter [need to be implemented]

Coming soon ...

#### Action Filter

Action filters allow to run the code before and after the action execute
Action filter can be defined by implementing the IAcitonFilter Interface

Execution of Pipeline can be short circuited if you set the result field

```javascript
 public class TestActionFilter implements IActionFilter {

    //execute method before action call
 @Override
    public void beforeExecute(HttpContext httpContext) {
        //contains the action parameters that you can change
        // httpContext.ActionContext.MethodParameters
 System.out.println("TestActionFilter :beforeExecute");

          //for short-circuiting pipe set action result value
           //httpContext.ActionContext.ActionResult
        // example httpContext.ActionContext.ActionResult= new NotFoundResult();



 }

 @Override
    public void afterExecute(HttpContext httpContext) {
        //perform any logic after action execution
        // here Action result will be present
 System.out.println("TestActionFilter :afterExecute");
 }
}
...

```

We can configure the Filter on Three-level.

1. Global Level Action Filter

The global filter will execute against every action.
The global filter can be registered in the Startup class ConfigureServices method.

```javascript

public class AppStartup implements IStartup {

    //Register your service with IOC Container
   
 @Override
    public void configureServices(IServiceCollection service) {
       
        //configuring MVC options
 MvcConfigurer.configureMvc(service,options->{

            // Adding your custom  Global  filters
 options.addGlobalFilter(TestGlobalActionFilter.class);
            .....

 });
        
 }
  ...

 }
```

2. Controller Level  Action Filter

Controller level Action filter can be applied using @ActionFilter Annotation on controller class and provide your custom filter
like @ActionFilter(filterClass = TestActionFilter2.class)

The filter will be executed on every action of the controller class

```javascript
@Route(path = "/hello") --> @Route Annotation setup the base route for Controller
@ActionFilter(filterClass = TestActionFilter2.class) ---> Controller Level Action filter . this is exectuted On every action execution in the controller
public class HelloController  extends ControllerBase {
    private ITestService testService;

    public HelloController(ITestService testService) { ---> Inject Dependency using Contructor Injection 
        this.testService = testService;
 }
....
```



3. Action Level Action Filter

Action Level Filter can be used the same as controller level filter. We have to apply the annotation on Action in place of the Controller class.

You can apply multiple filters as well as the order of execution as you apply.

```javascript
@Get       //-->  Using @Get attribute can specify Get Method 
 @ActionFilter(filterClass = TestActionFilter.class) --> //apply custom filter for specific action
   
 public List<Model> get() {
        //accesing transaiton id which is added by transactionIdMiddlware
 System.out.println(httpContext.getData().get("trazId"));
        return this.testService.getlist();

 }
```
#### Exception Filter

Exception Filter allow to handle the the custom exception which will throw from the action method.

We can define our custom ExceptionFilter by implementing the IExceptionFilter interface.

IExceptionFilter filter has 2 Methods
1. boolean support(..)

This method decides the eligibility of ExcptionFilter for a thrown exception.
2. onException(..)
   This method can give the control to you to handle the exception.


 ```
 public class TestExceptionFilter implements IExceptionFilter {
 @Override
 public boolean support(Class<? extends RuntimeException> exceptions) {
 //from the list of exception filter support methods decide to handle the exception
 return exceptions.equals(TestCustomException.class);
 }

 @Override
 public <T extends RuntimeException> void onException(HttpContext context, T e) {
 //handle your custom exception
 context.ActionContext.ActionResult= new BadRequestResult(e.getLocalizedMessage());
 }
}
 ```

> Exception filter can be configured globally in Startup Class in the configureServices method.
Where we can register the Custom Exception filter with MvcConfigurer using options.addExceptionFilter(..).

```javascript
public class AppStartup implements IStartup {

    //Register your service with IOC Container
   
 @Override
    public void configureServices(IServiceCollection service) {
       
        //configuring mvc options
 MvcConfigurer.configureMvc(service,options->{

          
            //Adding your Custom Exception Filters
 options.addExceptionFilter(TestExceptionFilter.class);
          
         

 });
        


 }
    ...
```

#### Result Filter

The result filter can be executed just before writing the response . At this stage, you can change the result fromthe action method at all .

We can define the Result filter by implementing IResultExecutionFilter

```javascript
public class TestResultFilter implements IResultExecutionFilter {
 @Override
    public void beforeResultExecute(HttpContext httpContext) {

 System.out.println("TestResultFilter:executed");
        //writing custom Header for
 httpContext.getResponse().addHeader("Framework-Name","Minicore");
 httpContext.getResponse().addHeader("Developer","Priyanshu Parate");

 }
}
```

We can apply the Result Filter at the global level and Action Level.

1. Global level
   The global result filter will execute for every action
   We can register the Custom Result filter with MvcConfigurer using options.addResultFilter()

```javascript

public class AppStartup implements IStartup {

    //Register your service with IOC Container
 @Override
    public void configureServices(IServiceCollection service) {
        //configuring mvc options
 MvcConfigurer.configureMvc(service, options -> {
           
            // Adding your custom Global Result filters
            // Global filter will execute for every action before writing the response
 options.addResultFilter(TestResultFilter.class);

 });
        ...

 }

```

2. Action level
   You can apply Result fitler for specific action using @ResultFilter

```javascript
 @Get
 @ActionFilter(filterClass = TestActionFilter.class)
    //apply custom filter for specific action
 @ResultFilter(filterClass = CustomResultFilter.class)
 public List<Model> get() {
 Integer value= iConfiguration.getValue(Integer.class,"age");
 System.out.println("value from Conguraton age:"+value);
        //accesing transaiton id which is added by transactionIdMiddlware
 System.out.println(httpContext.getData().get("trazId"));
        return this.testService.getlist();

 }

```


### Formatters


> coming soon ..

#### Input Formatters

> coming soon ..

#### Output Formatters

> coming soon ..

### App Startup

> coming soon ..
