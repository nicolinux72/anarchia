package it.anarchia.microservices.shell

import groovy.lang.Closure
import org.codehaus.groovy.tools.shell.util.Preferences
import groovy.sql.Sql
import java.sql.Types
import groovy.json.JsonSlurper

/**
 * Metodi di supporto per gestire file e template
 * @author nicola
 *
 */
class TemplateUtility {
	
	static copyAndReplaceText(source, dest, Closure replaceText){
		def sourceFile = new File(source)
		if (!sourceFile.exists()) return
		
		def destFile = new File(dest)
		destFile.write(replaceText(sourceFile.text))
  }

	public static templates = []
	static listTemplates() {
		//def process = "svn list ${Preferences.get(SVN, SVN)}/microservices/trunk/templates/".execute()
		//templates = process.text.split("/\n")
		
		new File("${ConfigUtility.MSHOME()}/templates").eachFile() { file->
			if (file.getName() != 'fragments' ) templates << file.getName()
		}
		
		return templates
		
	}
	
	static svnWriteTemplate(io, templatePath, binding, destPath) {
		def text = svnReadTemplate(io, templatePath)
		//io.out.println text
  
		// sostituisco le variabili
		def engine   = new groovy.text.SimpleTemplateEngine()
		def template = engine.createTemplate(text).make(binding)
		
		// scrivo il file
		def file = createFile(destPath)
		write file, template.toString()
		io.out.println "Write file ${file}"
	}
	
	static svnReadTemplate(io, path) {
		/*def cmd = "svn cat ${Preferences.get(SVN, SVN)}/microservices/trunk/templates/fragments/${path}"
		def cmd = "cat ${Preferences.get(MSHOME)}/templates/fragments/${path}"
	
		//io.out.println cmd
		def process = cmd.execute()
		process.text*/
		new File("${ConfigUtility.MSHOME()}/templates/fragments/${path}").text
	}
	
	static createFile(filePath) {
		def file    = new File(filePath)
		def dir     = file.parentFile
		if (!dir.exists()) dir.mkdirs()
	
		
		if (file.exists()) {
		  println "Il file già esiste, non creo nulla: ${file.path}"
		  file = null
		}
	
		file
	}
	
	static write(file, string) {
		if (file) file << string
			
	}

}
