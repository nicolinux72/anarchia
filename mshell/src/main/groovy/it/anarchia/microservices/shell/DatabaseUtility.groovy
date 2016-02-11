package it.anarchia.microservices.shell

import org.codehaus.groovy.tools.shell.util.Preferences
import groovy.sql.Sql
import java.sql.Types
import groovy.json.JsonSlurper

class DatabaseUtility {
	
	static def appConnection
	static def registryConnection
	
	
	/**
	 * Apro la connessione al db così come
	 * configurata nell'applicazione
	 */
	static openAppConnection() {
		
		if (appConnection != null) return appConnection
		
		def pp = new Properties()
		new File("application.properties").withInputStream { pp.load(it) }

		appConnection = Sql.newInstance( pp['spring.datasource.url'], pp['spring.datasource.username'],
		pp['spring.datasource.password'], pp['spring.datasource.driverClassName'])
	}
	
	static openRegistryConnection() {
		
		if (registryConnection != null) return registryConnection
			
		def pp = new Properties()
		new File("${ConfigUtility.MSHOME()}/bin/application.properties").withInputStream { pp.load(it) }

		appConnection = Sql.newInstance( pp['spring.datasource.url'], pp['spring.datasource.username'],
		pp['spring.datasource.password'], pp['spring.datasource.driverClassName'])
	}


}
