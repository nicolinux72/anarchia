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

class CheckInCommand
    extends CommandSupport
{

    public static final String COMMAND_NAME = ':check-in'

    CheckInCommand(final Groovysh shell) {
        super(shell, COMMAND_NAME, ':C')
    }

    
    @Override
    Object execute(final List<String> args) {
        assert args != null

        if (args.size() != 2) {
            fail(messages.format('error.unexpected_args', args.join(' ')))
        }

        checkIn(args[0],args[1]);
    }
	 
	 void checkIn(String projectName, String desc ) { 
		 io.out.println "Check in project ${projectName} in anarchia cloud"
		 
		 io.out.println "- create git repository project"
		 try {git(projectName) } catch (RuntimeException e) { io.err.println e.message}
		 
		 io.out.println "- create system unit files"
		 try {writeSystemdServices(projectName, desc, 0) } catch (RuntimeException e) { io.err.println e.message}
				 
		 io.out.println "- create go cd pipeline"
		 try {createGoBuildPipieline(projectName) } catch (RuntimeException e) { io.err.println e.message}
		 
	 }
	 
	 void checkInOld(String projectName, String desc ) {
	 	
		 io.out.println "Check in project ${projectName} in microservices cloud"
		 def conn = DatabaseUtility.openRegistryConnection()
	 
		 def result = conn.firstRow('select count(*) as cont from mservices where name=?', [projectName])
		
		 if (result.cont>0) {
			 fail("A project named ${projectName} is already registered.")
		 }
		 		 
		 git(projectName)
		 //jenkins(projectName)
		 
		 int maxPort = 0
		 conn.eachRow("select max(port)+10 from mservices"){
			 maxPort = it[0]
			 if (maxPort == 0 || maxPort == null)  maxPort = 8100
		 }

		 int maxAdminPort = 8900 + maxPort - 8100
		 
		 io.out.println  "Working port base range: ${maxPort}"
		 io.out.println  "Admin   port base range: ${maxAdminPort}"
		 
		 writeSystemdServices(projectName, desc, maxPort)
		 registerVulcan(projectName)
		 
		 conn.execute('insert into mservices (instances, description, name,  port, admin_port, enabled, context) values (1, ?,?,?,?, \'S\', ?)',
			  [desc, projectName, maxPort, maxAdminPort, projectName ])
		 
	 }
	 
	 
	 void git(projectName) {
		 
		 def gitUrl = ConfigUtility.GIT()
		 
		 if (gitUrl == null) return;
		 
		 def server = ''
		 def path = gitUrl
								 
		 def urlSlices = gitUrl.split(':')
		 
		 if ( urlSlices.size() == 2 ) {
			 server = gitUrl.split(':')[0]
			 path   = gitUrl.split(':')[1]
		 }
		 
		 println "Additing project to git a new git repos ${gitUrl}/${projectName}.git"
		  
		 def pomFile = new File('pom.xml')
		 def gradleFile = new File('build.gradle')
		 
		 def initCmd = "ssh ${server}"
		 if (server=='') initCmd = " "
		 
		 Utility.execute(io, [
			 "${initCmd} git init --bare ${path}/${projectName}.git",
			 "git init"
			 ])
		 
		 if  (pomFile.exists()) { 
			 Utility.execute(io, ['mvn clean',
				 "git add pom.xml src "
				 ])
		 }
		 
		 if  (gradleFile.exists()) {
			 Utility.execute(io, [
				 "git add build.gradle gradle gradlew  gradlew.bat src "
				 ])
		 }
		 
		 Utility.execute(io, [
			 "git commit -m 'initial'",
			 "git remote add origin ${gitUrl}/${projectName}.git",
			 "git push -u origin master"
			 ])
		 
	 
	 }
	 
	 void writeSystemdServices(String name, String desc, int port) {
		 
		  //non è possibile usare groovy keyword ne binding (es- package, class, ecc.)
		  def binding = [ desc:     desc,
							   project:  name]
 
		  def text = TemplateUtility.svnReadTemplate(io, 'check-in/micro.service')
		  //io.out.println text
	 
		  // sostituisco le variabili
		  def engine   = new groovy.text.SimpleTemplateEngine()
		  def template = engine.createTemplate(text).make(binding)
		  
		  // scrivo il file
		  def path = "${ConfigUtility.MSHOME()}/coreos/services/${name}@.service"
		  def file = TemplateUtility.createFile(path)
		  TemplateUtility.write file, template.toString()
		  io.out.println "Write file ${file}"
		 	
		  Utility.execute(io, [
			  "${ConfigUtility.FLEETCTL()} submit $path"  ])
		  
		  	  
		  /*(0..4).each { i ->
			  def cmd = "ln -s ${path} ${ConfigUtility.MSHOME()}/coreos/services/ports/${name}@${port+i}.service" 
			  Utility.execute(io, [cmd])
		  }*/
		
	 }
	 
	 void registerVulcan(String name) {
		 Utility.execute(io, [
			 "${ConfigUtility.VCTL()} backend  upsert -id ${name}",
			 "${ConfigUtility.VCTL()} frontend upsert -id ${name}  -b ${name}  -route 'Path(\"/api/${name}\")'",
		 	 "${ConfigUtility.VCTL()} rewrite  upsert -f ${name} -id ${name}  --regexp=\"/api/${name}(.*)\" --replacement='\$1'"
			 ])
		 
	 }
	 
	 void createGoBuildPipieline(String name) {
		 //non è possibile usare groovy keyword ne binding (es- package, class, ecc.)
		 def binding = [ pipelineName:   "$name-build",
							  gitPath:  "/home/vagrant/${name}.git",
							  projectName: name,
							  jarName: "${name}.jar"]

		 def text = TemplateUtility.svnReadTemplate(io, 'check-in/build-pipeline.json')
		 // io.out.println text
	
		 // sostituisco le variabili
		 def engine   = new groovy.text.SimpleTemplateEngine()
		 def template = engine.createTemplate(text).make(binding)
		 
		 // scrivo il file
		 def path = "/tmp/${binding.pipelineName}.json"
		 def file = TemplateUtility.createFile(path)
		 TemplateUtility.write file, template.toString()
		 io.out.println "Write file ${file}"
		 
		 Utility.execute(io, [
			 "curl -H 'Accept: application/vnd.go.cd.v1+json' -H 'Content-Type: application/json' -X POST -d @$path ${ConfigUtility.GO()}/go/api/admin/pipelines "
		])
		
	 }

}
