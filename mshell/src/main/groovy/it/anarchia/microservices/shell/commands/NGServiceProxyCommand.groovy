/*
 * Copyright 2003-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.anarchia.microservices.shell.commands

import groovy.transform.CompileStatic
import jline.console.completer.AggregateCompleter
import jline.console.completer.ArgumentCompleter
import jline.console.completer.Completer
import jline.console.completer.StringsCompleter

import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.ResolveVisitor
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Evaluator
import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.Interpreter
import org.codehaus.groovy.tools.shell.commands.ImportCommand ;
import org.codehaus.groovy.tools.shell.commands.ImportCompleter;
import org.codehaus.groovy.tools.shell.completion.ReflectionCompletionCandidate
import org.codehaus.groovy.tools.shell.completion.ReflectionCompletor
import org.codehaus.groovy.tools.shell.util.Logger
import org.codehaus.groovy.tools.shell.util.PackageHelper
import org.codehaus.groovy.tools.shell.util.PackageHelperImpl

import java.util.regex.Pattern

import it.anarchia.microservices.shell.*

import java.util.List

import groovy.sql.Sql

import java.sql.Types
import java.sql.DatabaseMetaData

import groovy.io.FileType

import java.text.DecimalFormat

import static groovy.io.FileType.FILES

/**
 * The 'import' command.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class NGServiceProxyCommand
    extends CommandSupport
{

    /**
     * pattern used to validate the arguments to the import command,
     * which proxies the Groovy import statement
     * chars, digits, underscore, dot, star
     */
    private static final Pattern IMPORTED_ITEM_PATTERN = ~'[a-zA-Z0-9_. *]+;?$'

    NGServiceProxyCommand(final Groovysh shell) {
        super(shell, ':ng-service-proxy', ':ng-sp')
    }

	 def loader 

	 @Override
	 Completer getCompleter() {
		 loader = JVMUtility.applicationClassLoader(io)
		 Completer impCompleter = new StringsCompleter(name, shortcut)
		  PackageHelper packageHelper = new PackageHelperImpl(loader)
		  Interpreter interp = shell.interp
		  
		  Collection<Completer> argCompleters = [
			  (Completer) new ArgumentCompleter([
						 impCompleter,
						 new ImportCompleter(packageHelper, interp, false),
						 null])]
		  
		  return new AggregateCompleter(argCompleters)
		  	
	 }

	 

    Object execute(final List<String> args) {
        assert args != null

        if (args.size()!=2) {
            fail('Command \'proxy service\' requires two arguments') // TODO: i18n
        }
		  	
		  ngServiceProxy(args[1],args[0])	  
	 }
	 
	 void ngServiceProxy(String name, String clazz) {
			 
	 	 def bowerJson = ConfigUtility.readBowerJson()
	 	 def webroot   = ConfigUtility.webroot()

		 if  (webroot == null) {
			fail "Webroot direcotry doesn't exists"
		 }
		 
		 //non è possibile usare groovy keyword ne binding (es- package, class, ecc.)
		 def binding = [proxyName: name,
			 				 endpoints: endPoints(io, clazz),
			             appName: bowerJson.name]
 
		 TemplateUtility.svnWriteTemplate(io, 'ng-service-proxy/service.js',binding,"${webroot}/js/api/${name}.js")
		 ConfigUtility.includeJS(["<script type=\"text/javascript\" src=\"js/api/${name}.js\"></script>"])
    }
	 
	 def endPoints(io, String clazz) {
		 
		 def bobo =  loader.loadClass(clazz)
		 def requestMapping = loader.loadClass( 'org.springframework.web.bind.annotation.RequestMapping')
		 
		 def gb = bobo.getAnnotation(requestMapping)
		 def prefix = gb?.value().find{true} ?: ''
			
		 
		 def endPoints = []
		 bobo.methods.each { method ->
			  def anno = method.getAnnotation(requestMapping)
	  
			  if (anno != null) {
				  def endPoint = [:]
		
				  endPoint.name = method.name
				  endPoint.httpMethod =   anno.method()*.name()*.toLowerCase().find{true} ?: 'get'
				  endPoint.url = prefix + anno.value()[0]
	 
				  io.out.println " ${endPoint.httpMethod} ${endPoint.url} ${method.name}"
				  endPoints << endPoint
			 }
		}
		 
		return endPoints
	 }
	 
}


