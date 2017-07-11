booklib
========

Book Library Application, see [Wiki](https://github.com/springside/booklib/wiki) for details.

Demo is running on [Heroku](http://f5f6lib.herokuapp.com).


P2P 虚拟图书馆项目

11 Jan 2015 | Calvin Category: project
缘起

美团 P2P 图书馆实践： 5天时间1845册图书共享入库

我们也曾做过集中采购集中管理的图书馆，慢慢的就消失了，也许P2P才是正确的路

架构

Anjular.js(Front End) + Spring Boot(Restful Service) + 豆瓣API

新一代全栈工程师的标准架构，可作日后培训的样板工程。

其中展示了俊雄同志在前端开发的各种最佳实践，同时也将展示后台服务的新潮流新趋势，特别是如何用最短的时间最少的代码，开发一个功能完备的业务系统。

部署

PaaS平台： Heroku，免费的512M JVM

应用服务器： SpringBoot内置Tomcat，只需java -jar target/bootlib.war 启动

数据库： 开发时用嵌入式的H2，生产环境用Heroku提供的PostgreSQL，但免费版只能存1万行数据

持续交付： CodeShip自动将代码持续地从github部署到Heroku。但免费版每月只能搬运100次，每天不要提交太多了。

地址

应用地址：f5f6lib.herokuapp.com

源码仓库：github.com/f5f6/booklib

开发团队

Junxiong, Neway, Calvin
