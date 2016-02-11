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
import org.codehaus.groovy.tools.shell.completion.ReflectionCompletionCandidate
import org.codehaus.groovy.tools.shell.completion.ReflectionCompletor
import org.codehaus.groovy.tools.shell.util.Logger
import org.codehaus.groovy.tools.shell.util.PackageHelper
import org.codehaus.groovy.tools.shell.commands.ImportCompleter
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
class RestControllerCommand
    extends CommandSupport
{

    /**
     * pattern used to validate the arguments to the import command,
     * which proxies the Groovy import statement
     * chars, digits, underscore, dot, star
     */
    private static final Pattern IMPORTED_ITEM_PATTERN = ~'[a-zA-Z0-9_. *]+;?$'

    RestControllerCommand(final Groovysh shell) {
        super(shell, ':rest-controller', ':RC')
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

        if (args.isEmpty()) {
            fail('Command \'rest controller\' requires two arguments') // TODO: i18n
        }
		  
		  if (args.size() != 2) {
			  fail(messages.format('error.unexpected_args', args.join(' ')))
		  }
	
		  restController(args[0],args[1])	  
	 }
	 
	 void restController(String modelFullPath, String controllerName)
	 {
		def pom = ConfigUtility.readPom()
		 				
		//non è possibile usare groovy keyword nel binding 
		//(es- package, class, ecc.)
		def binding = [ Controller: controllerName,
			             pack: "${pom.groupId}.manager", 
			             model: modelFullPath.split('\\.').last().toLowerCase(),
							 Model: modelFullPath.split('\\.').last(),
							 modelPath: modelFullPath]

		def controllerPath = binding.pack.replaceAll('\\.','/')
		
		TemplateUtility.svnWriteTemplate(io, 'rest-controller/controller.java',
			binding,"src/main/java/$controllerPath/${controllerName}.groovy")
		

	 }
	 
	
}


