									CytoVisProject Tutorial
This tutorial is prepared to introduce how to install “CytoVisProject” app. CytoVisProject is created for visualizing and helping to analyze of PROV-O formatted .xml 
files. CytoVisProject can visualize PROV-O formatted .xml files with using a XSLT Engine named Saxon. Requirements for transforming it is Saxon and a VisMap file.
You can download Saxon from the below link.

http://saxon.sourceforge.net/

Tutorial is divided into 3 part:

-	Creating a Jar File 
-	Installing Jar File To The Cytoscape

To start using of this app you need to create a jar file from the files of CytoVisProject with using a Java IDE an installing it to the Cytoscape.

Creating A Jar File
To create a jar file follow the below steps.

1.	Open the CytoVisProject folder with a Java IDE.

2.	Add Maven dependencies to the Project which is given in the dependencies file of the CytoVisProject.
	-	If you do not have Maven you can download and install by following the instructions here: https://maven.apache.org/install.html
	-	For adding a dependency to Maven run this command at console sceen with filling the inside of quotation marks.

	mvn install:install-file -Dfile=”Jar File’s Path” -DgroupId=”GroupID” -DartifactId=”ArtifactId” -Dversion=”version” -Dpackaging=jar
 	

	-	After installing .jar files which is in dependencies files, add below code to pom.xml file.

	<dependency>
      		<groupId> groupId e</groupId>
      		<artifactId> artifactId </artifactId>
      		<version> version </version>
	 </dependency>

3.	After adding Maven dependencies, now you can create a jar file. For doing this in IntelliJ idea in the top menü follow “File – Project Structure” path.
4.	Choose the Artifacts under the Project Settings and click "+" plus icon.
5.	Under the Add tab follow the "JAR - From modules with dependencies" path.
6.	Click Ok and after choosing Output Directory (where JAR file will be create) click Ok buton again.

After following the steps above Jar file will be created in the directory that you choosed.

Installing Jar File To The Cytoscape
Now you will learn how to install the jar file that you create with the applying above instructions to the Cytoscape.

1.	In the Cytoscape interface click the “Apps” part which is located in top menu and then choose “Apps Manager” tab.
2.	Under the “Install Apps” tab click “Install From File” button and choose the jar file that you created.
3.	After the installation you should see "Installed" status under the “Currently Installed” tab.

If you see any other thing than the "Installed" message,  open the JAR file (you can open with a program like Winrar) and change the MANIFEST.MF files under the 
META-INF folder with the MANIFEST.MF file that is in the APP folder which is again in the META-INF folder of the CytoVisProject’s documents.