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
class NGStateCommand
    extends CommandSupport
{

    /**
     * pattern used to validate the arguments to the import command,
     * which proxies the Groovy import statement
     * chars, digits, underscore, dot, star
     */
    private static final Pattern IMPORTED_ITEM_PATTERN = ~'[a-zA-Z0-9_. *]+;?$'

    NGStateCommand(final Groovysh shell) {
        super(shell, ':ng-state', ':ng-s')
    }


    Object execute(final List<String> args) {
        assert args != null

        if (args.isEmpty()) {
            fail('Command \'entity\' requires one argument') // TODO: i18n
        }
		  
		  if (args.size() != 1) {
			  fail(messages.format('error.unexpected_args', args.join(' ')))
		 }
	
		  ngState(args[0])	  
	 }
	 
	 void ngState(String state) {
			 
		 	 def bowerJson = ConfigUtility.readBowerJson();
		 	 def webroot =  ConfigUtility.webroot()

			 if  (webroot == null) {
					 fail "Webroot direcotry doesn't exists"
			 }
		 
			 //non è possibile usare groovy keyword ne binding (es- package, class, ecc.)
			 def binding = [ state:   state, 
				             State:   state.capitalize(), 
						     appName: bowerJson.name]
	 
			 TemplateUtility.svnWriteTemplate(io, 'ng-state/state.js',binding,"${webroot}/js/${state}/${state}.js")
			 TemplateUtility.svnWriteTemplate(io, 'ng-state/state.controller.js',binding,"${webroot}/js/${state}/${state}.controller.js")
			 TemplateUtility.svnWriteTemplate(io, 'ng-state/state.html',binding,"${webroot}/js/${state}/${state}.html")
			
			 TemplateUtility.svnWriteTemplate(io, 'ng-state/state-en.json',binding,"${webroot}/i18n/${state}-en.json")
	 		 TemplateUtility.svnWriteTemplate(io, 'ng-state/state-it.json',binding,"${webroot}/i18n/${state}-it.json")
	 		 

	 		 def appInclude = '<script type="text/javascript" src="js/app.js"></script>'
	 		 def newInclude =  [ appInclude,
	 		                     "<script type=\"text/javascript\" src=\"js/${state}/${state}.js\"></script>",
	 		                     "<script type=\"text/javascript\" src=\"js/${state}/${state}.controller.js\"></script>"
	 		                   ].join("\n    ")


	 		 TemplateUtility.copyAndReplaceText("${webroot}/index.html", "${webroot}/index.html") {
	 		 	it.replaceAll(appInclude, newInclude)
		 	 }


	 }		 
	 
}


