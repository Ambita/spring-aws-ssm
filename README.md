AWS SSM / Parameter Store for Spring Boot
=========================================

This project adds support to Spring Boot projects to fetch configuration from
 AWS's Parameter Store. (Aka AWS SSM)




How to use
----------

1. Add dependency
```
    
```
2. Add initializer to Application
3. Add initializer to integration tests:
```
        @ContextConfiguration(initializers = [ParamStoreContextConfiguration::class]) to
```
4. Add property source to your project
5. Put configuration into AWS SSM


Integration tests
-----------------

The integration tests require properties in AWS.

### Terraform

* Install TF
* init, plan and apply



We have several reasons for using AWS Parameter Store (SSM):
* Move secrets out of the Appliction's source code.
* Reduce the dependency on Hiera and Puppet
* Prepare for Docket / Kubernetes


How it works
------------

1. Put the files in SSM, with Terraform.
    * Terraform repos must be secured more tightly.
2. Modify the application to use the new properties.


Path in SSM
-----------

We put files in AWS's SSM under the path:

    /components/$sysgroup/$component/$miljoe

For Spring Boot applications this is the files:

    /components/$sysgroup/$component/$miljoe/springboot-role.yaml
    /components/$sysgroup/$component/$miljoe/springboot-miljoe.yaml


Terraform
---------

An example of the configuration is in the proto-project.

1. Copy the files from proto to your project:
    * paramstore-role.tf
    * paramstore-miljoe.tf
    * conf/dev/springboot-miljoe.yaml
    * paramstore-springboot.tf
2. Add variables to your project:
    * sysgroup
    * system
    * role

If play, then you need to call the files play instead of springboot :-)

Usage in Spring Boot
---------------------

To run an application server with SSM configuration:

1. Add ambita-client-aws-spring
2. To SpringApplications, do as in 

        com.ambita.client.aws.spring.paramstore.ParamStoreApplicationTest.main

3. To springBoot tests add @ContextConfiguration as in

        com.ambita.client.aws.spring.paramstore.ParamStorePropertySourceTest

4. Add @PropertySource as in 

        com.ambita.client.aws.spring.paramstore.TestContext    


