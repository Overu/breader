-- Maven settings配置 --
需要在settings文件里配置仓库管理器镜像代理所有仓库

-- Eclipse配置文件   --
 因Eclipse的配置文件因人而异, 故配置文件不做版本管理. 
现已在SVN上忽略.gwt .settings target .classpath .project. 
配置文件在导入Maven项目时由Eclipse Maven Plugin根据pom自动生成.

-- Code style --
we uses the same code style rules as GWT
http://code.google.com/webtoolkit/makinggwtbetter.html#codestyle
配合下列Workspace Mechanic for Eclipse插件, 可在代码保存时自动格式化为统一的样式.

-- Eclipse配置集中管理  --
推荐安装下列Workspace Mechanic for Eclipse插件, 
http://code.google.com/a/eclipselabs.org/p/workspacemechanic/

-- Option A: Import your project into Eclipse (recommended) --

Configure Eclipse following the instructions at 
http://code.google.com/p/google-web-toolkit/wiki/WorkingWithMaven#Using_Maven_with_Google_Plugin_for_Eclipse

In Eclipse, go to the File menu and choose:

  File -> Import... -> Existing Maven Projects into Workspace

  Select the directory containing this file.

  Click Finish.

You can now browse the project in Eclipse.

To launch your web app in GWT development mode

  Go to the Run menu item and select Run -> Run as -> Web Application.

  If prompted for which directory to run from, simply select the directory
  that Eclipse defaults to.

  You can now use the built-in debugger to debug your web app in development mode.

GWT developers (those who build GWT from source) may add their
gwt-user and gwt-dev projects to this project's class path in order to
use the built-from-source version of GWT instead of the version
specified in the POM.

  Select the project in the Project explorer and select File > Properties

  Select Java Build Path and click the Projects tab

  Click Add..., select gwt-user and gwt-dev, and click OK

  Still in the Java Build Path dialog, click the Order and Export tab

  Move gwt-dev and gwt-user above Maven Dependencies
  
GWT developers can also use tools/scripts/maven_script.sh to push their
own GWT jars into their local maven repo.

-- Option B: Build from the command line with Maven --

If you prefer to work from the command line, you can use Maven to
build your project (http://maven.apache.org/). You will also need Java
1.6 JDK. Maven uses the supplied 'pom.xml' file which describes
exactly how to build your project. This file has been tested to work
against Maven 2.2.1. The following assumes 'mvn' is on your command
line path.

To run development mode use the Maven GWT Plugin.

  mvn gwt:run

To compile your project for deployment, just type 'mvn package'.

For a full listing of other goals, visit:
http://mojo.codehaus.org/gwt-maven-plugin/plugin-info.html
