package it.anarchia.microservices.shell.commands

/**
 * Comando per la creazione di un nuovo progetto
 * 
 * @author nicola
 */
import it.anarchia.microservices.shell.*
import jline.console.completer.Completer
import org.codehaus.groovy.tools.shell.util.SimpleCompletor

import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Command
import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.completion.CommandNameCompleter
import org.codehaus.groovy.tools.shell.util.Preferences

class ProjectCommand
    extends CommandSupport
{

    public static final String COMMAND_NAME = ':project'

    ProjectCommand(final Groovysh shell) {
        super(shell, COMMAND_NAME, ':P')
    }

    protected List<Completer> createCompleters() {
        
		 def loader = {
			 TemplateUtility.templates.toList()
		 }
		 
		return [
            new SimpleCompletor(loader),
            null
      ]
    }

    @Override
    Object execute(final List<String> args) {
        assert args != null

        if (args.size() != 2) {
            fail(messages.format('error.unexpected_args', args.join(' ')))
        }

        create(args[0],args[1]);
    }

	 private void create(String template,String projectName ) {
		 
		 
		 def mshome = ConfigUtility.MSHOME()
		 
		 io.out.println "Creating project $projectName from template $template existing in mshome $mshome"
		 //Utility.execute(io, ["svn export  ${svn}/microservices/trunk/templates/${template} . --force "])
	    
		 //Utility.execute(io, ["cp -rv  ${mshome}/templates/${template}/* . "])
		 
		 new AntBuilder().copy(verbose:"true", todir: ".") {
			 fileset(dir : "${mshome}/templates/${template}") {
				  include(name:"**/*")
				  exclude(name:"**/.*/**")
				  exclude(name:"**/coverage/**")
				  exclude(name:"**/bower_components/**")
				  exclude(name:"**/node_modules/**")
				  exclude(name:"**/js/lib/**")
				  exclude(name:"**/node/**")
				  exclude(name:"**/target/**")
				  exclude(name:"**/deploy/**")
				  exclude(name:"*.iml")
			 }
		}
		 		 
		 def webroot =  ConfigUtility.webroot()


		 [ "build.gradle",
			"bower.json",
		   "pom.xml",
			"src/main/resources/bootstrap.yml",
		   "${webroot}/js/language/language.controller.js",
		   "${webroot}/js/language/language.service.js",
			"${webroot}/js/home/home.controller.js",
			"${webroot}/js/home/home.js",
		   "${webroot}/js/app.js",
		   "${webroot}/index.html",
			"test/app.spec.js",
		   "test/app.mocha.js"].each { fileName ->
		   	TemplateUtility.copyAndReplaceText(fileName, fileName) { fileContent ->
					fileContent.replaceAll("nrk-project-name", projectName)
			 
		 	}
		 }

		 
	 }
	 
}

