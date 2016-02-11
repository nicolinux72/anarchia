package it.anarchia.microservices.shell

import org.codehaus.groovy.tools.shell.util.Preferences
import groovy.sql.Sql
import java.sql.Types
import groovy.json.JsonSlurper

/**
 * Alcune comodità per convertiti tipi e nomi
 * @author nicola
 *
 */
class JVMUtility {
	
	/**
	 * Magia: ci mettiamo nella condizione di caricare
	 * ogni classe dell'applicazione
	 * 
	 * @param io
	 * @return
	 */
	static GroovyClassLoader applicationClassLoader(io) {
		
		// Create a new class loader for application classes
		//javaLoader = new URLClassLoader()
		def loader = new GroovyClassLoader(/*javaLoader*/)
	
		def jarsDir    = new File("target/dependency/")
		def srcDir = new File("src/main/java/")
		def classesDir = new File("target/classes/")
	
		if (!jarsDir.exists()) {
			io.out.println "Please run: mvn dependency:copy-dependencies"
			
			if (new File("pom.xml").exists()) {
				Utility.execute(io, ['mvn dependency:copy-dependencies'])
			} else return
		}
	
		if (!classesDir.exists()) {
			io.out.println "Please run: mvn compile"
			if (new File("pom.xml").exists()) {
			  Utility.execute(io, ['mvn compile'])
			} else return
		}
	
	
		// load jars
		jarsDir.eachFileMatch(~/.*\.jar/) {
			 loader.addClasspath(it.absolutePath)
			 //javaLoader.addURL(it.toURI().toURL())
		}
	
		// load classes
		loader.addClasspath(classesDir.absolutePath)
		//loader.addClasspath(srcDir.absolutePath)
		//loader.addURL(new File("./target/classes/").toURI().toURL())
	
		//println loader.getClassPath()
		//println javaLoader.URLs
		return loader
	}
	
	static camel( String text, boolean capitalized = false ) {
		if (!text)  return ""
		text = text.toLowerCase().replaceAll( "(_)([A-Za-z0-9])", { Object[] it -> it[2].toUpperCase() } )
		return capitalized ? text.capitalize() : text
	}
	
	static String getJavaType(sqlType) {
		
		switch (sqlType) {
			
			case Types.BOOLEAN  : return  "bool";
			
			case Types.CHAR:
			case Types.NCHAR : return  "String";
			
			case Types.DATE :
			case Types.TIME :
			case Types.TIMESTAMP : return "java.util.Date";
			
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.FLOAT :
			case Types.NUMERIC : return "Long";
			
			case Types.BIGINT:
			case Types.INTEGER : return "Integer";
			
			case Types.CLOB:
			case Types.NCLOB :
			case Types.NVARCHAR :
			case Types.VARCHAR :
			case Types.LONGNVARCHAR : return "String";
			
			default : return "Object";
			
		}
	}


}
