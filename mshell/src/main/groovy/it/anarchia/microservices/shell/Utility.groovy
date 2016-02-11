package it.anarchia.microservices.shell

import org.codehaus.groovy.tools.shell.util.Preferences
import groovy.sql.Sql
import java.sql.Types
import groovy.json.JsonSlurper

/**
 * Classe generica di utilità.
 * 
 * @author nicola
 *
 */
class Utility {
	
	/**
	 * Chiamata all'avvio della shell, ci consente
	 * di precaricare, inizializzare e quant'altro
	 * 
	 */
	static setup() {
		ConfigUtility.setPreferences()
		TemplateUtility.listTemplates()
	}
	

	//bello vero?
	static banner(io) {
		[ "           _                                    _",
		  " _ __ ___ (_) ___ _ __ ___  ___  ___ _ ____   _(_) ___ ___  ___",
		  "| '_ ` _ \\| |/ __| '__/ _ \\/ __|/ _ \\ '__\\ \\ / / |/ __/ _ \\/ __|",
		  "| | | | | | | (__| | | (_) \\__ \\  __/ |   \\ V /| | (_|  __/\\__ \\",
		  "|_| |_| |_|_|\\___|_|  \\___/|___/\\___|_|    \\_/ |_|\\___\\___||___/"
		].each {io.out.println it}
	}
	
	/**
	 * Eseguo un comando sul sistema operativo ospite
	 * @param io per scrivere qualcosa sullo schermo
	 * @param cmds l'array di comandi
	 * @return
	 */
	static execute(io, List<String> cmds) {
		cmds.each { cmd ->
			println cmd
			def process = cmd.execute()
			io.out.println "${process.text}" 
			process.waitFor()
			def result = process.exitValue()
			if (result!=0) {
			  io.out.println process.err.text
			  throw new RuntimeException("Errore eseguendo il comando: ${cmd}")
			}
		}
	}

		
	
	
			

	
	
}
