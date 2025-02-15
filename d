warning: in the working copy of '.idea/workspace.xml', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'pom.xml', LF will be replaced by CRLF the next time Git touches it
[1mdiff --git a/.idea/workspace.xml b/.idea/workspace.xml[m
[1mindex 05779c0..51643f7 100644[m
[1m--- a/.idea/workspace.xml[m
[1m+++ b/.idea/workspace.xml[m
[36m@@ -6,8 +6,7 @@[m
   <component name="ChangeListManager">[m
     <list default="true" id="07d44c36-cacd-435e-afd4-144404fbf40e" name="Changes" comment="">[m
       <change beforePath="$PROJECT_DIR$/.idea/workspace.xml" beforeDir="false" afterPath="$PROJECT_DIR$/.idea/workspace.xml" afterDir="false" />[m
[31m-      <change beforePath="$PROJECT_DIR$/src/main/java/org/Jtech/HealthCheckApplication.java" beforeDir="false" afterPath="$PROJECT_DIR$/src/main/java/org/Jtech/HealthCheckApplication.java" afterDir="false" />[m
[31m-      <change beforePath="$PROJECT_DIR$/src/main/resources/application.properties" beforeDir="false" afterPath="$PROJECT_DIR$/src/main/resources/application.properties" afterDir="false" />[m
[32m+[m[32m      <change beforePath="$PROJECT_DIR$/pom.xml" beforeDir="false" afterPath="$PROJECT_DIR$/pom.xml" afterDir="false" />[m
     </list>[m
     <option name="SHOW_DIALOG" value="false" />[m
     <option name="HIGHLIGHT_CONFLICTS" value="true" />[m
[1mdiff --git a/pom.xml b/pom.xml[m
[1mindex b259be9..b37a793 100644[m
[1m--- a/pom.xml[m
[1m+++ b/pom.xml[m
[36m@@ -55,21 +55,13 @@[m
             <groupId>org.springframework.boot</groupId>[m
             <artifactId>spring-boot-starter-web</artifactId>[m
         </dependency>[m
[31m-        <dependency>[m
[31m-            <groupId>org.bouncycastle</groupId>[m
[31m-            <artifactId>bcpkix-jdk15on</artifactId>[m
[31m-            <version>1.76</version>[m
[31m-        </dependency>[m
[32m+[m
         <dependency>[m
             <groupId>org.springframework.boot</groupId>[m
             <artifactId>spring-boot-starter-test</artifactId>[m
             <scope>test</scope>[m
         </dependency>[m
[31m-        <dependency>[m
[31m-            <groupId>org.Jtech</groupId>[m
[31m-            <artifactId>healthcheck-service</artifactId>[m
[31m-            <version>1.0-SNAPSHOT</version>[m
[31m-        </dependency>[m
[32m+[m
     </dependencies>[m
 [m
 [m
