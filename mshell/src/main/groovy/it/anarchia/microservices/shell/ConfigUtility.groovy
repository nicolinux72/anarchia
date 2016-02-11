package it.anarchia.microservices.shell

import org.codehaus.groovy.tools.shell.util.Preferences
import groovy.sql.Sql
import java.sql.Types
import groovy.json.JsonSlurper

/**
 * Gestsce le configurazioni applicative e di shell
 * 
 * @author nicola
 *
 */
class ConfigUtility {
	
	private static final String _GIT = 'GIT'
	private static final String _MSHOME = 'MSHOME'
	private static final String _VCTL = 'VCTL'
	private static final String _GO = 'GO'
	private static final String _FLEETCTL = 'FLEETCTL'
	
	/**
	 * Imposto alcune preferenze se non lo sono già
	 */
	static setPreferences() {
		def env = System.getenv()
		File mshome = new File(env['MSBIN']).getParentFile()
				
		Preferences.put _GIT,Preferences.get(_GIT, 'viewpoint:/home/vagrant')
		Preferences.put _MSHOME,Preferences.get(_MSHOME, mshome.canonicalPath)
		Preferences.put _VCTL,Preferences.get(_VCTL, '/vagrant/bin/vctl --vulcan http://172.17.8.101:8182' )
		Preferences.put _GO,Preferences.get(_GO, 'http://172.17.8.10:8153')
		Preferences.put _FLEETCTL,Preferences.get(_FLEETCTL, "${MSHOME()}/bin/fleetctl --tunnel 172.17.8.101 ")
		
	}
	
	static value(String name) {
		Preferences.get(name)
	}
	
	static MSHOME() { return value(_MSHOME)}
	static GIT() { return value(_GIT)}
	static VCTL() { return value(_VCTL)}
	static GO() { return value(_GO)}
	static FLEETCTL() { return value(_FLEETCTL)}
	
	
	static webroot() {
		def webroot = new File('webroot')
			  
		 if  (!webroot.exists()) {
			 webroot = new File('src/main/webapp')
			 if  (!webroot.exists()) {
				 return null
			 }
		 }

		 webroot.getAbsolutePath()
		 
	}
	
	static includeJS(newIncludes) {
				
		def appInclude = '<script type="text/javascript" src="js/app.js"></script>'
		newIncludes << appInclude
		
		def newInclude =  newIncludes.join("\n    ")

		TemplateUtility.copyAndReplaceText("${webroot()}/index.html", "${webroot()}/index.html") {
			it.replaceAll(appInclude, newInclude)
		}

	}
	
	static readBowerJson() {
		def bower = new File("bower.json")
		new JsonSlurper().parseText(bower.text)
	}
	
	static readPom() {
		new XmlSlurper().parse("pom.xml")
	}



}
