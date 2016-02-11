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
class EntityCommand
    extends CommandSupport
{

    /**
     * pattern used to validate the arguments to the import command,
     * which proxies the Groovy import statement
     * chars, digits, underscore, dot, star
     */
    private static final Pattern IMPORTED_ITEM_PATTERN = ~'[a-zA-Z0-9_. *]+;?$'

    EntityCommand(final Groovysh shell) {
        super(shell, ':entity', ':E')
    }


    Object execute(final List<String> args) {
        assert args != null

        if (args.isEmpty()) {
            fail('Command \'entity\' requires three arguments') // TODO: i18n
        }
		  
		  if (args.size() != 3) {
			  fail(messages.format('error.unexpected_args', args.join(' ')))
		 }
	
		  entity(args[0],args[1],args[2])	  
	 }
	 
	 void entity(String schema, String pack, String table) {
			 def meta = DatabaseUtility.openAppConnection().connection.metaData
			 def col  = meta.getColumns(null,schema.toUpperCase(),table.toUpperCase(), null)
			 
			 // variabili da sostituire nel template		 
			 def properties = []
			 while (col.next()) {
				 def property = [:]
						 property.field  = JVMUtility.camel(col.getString('COLUMN_NAME'))
						 property.column = col.getString('COLUMN_NAME')
						 property.type   = JVMUtility.getJavaType(col.getInt('DATA_TYPE'))
						 
						 properties << property
			 }
			 
			 //non è possibile usare groovy keyword nel binding (es- package, class, ecc.)
			 def binding = [ pack:pack, 
				              table: table.toUpperCase(), 
								  clazz: JVMUtility.camel(table,true), 
								  properties:properties ]
			 
			 TemplateUtility.svnWriteTemplate(io, 'entity/entity.groovy',
				 binding,"${pack.replaceAll('\\.','/')}/${binding['clazz']}.groovy")
			 
			 	 
		
	 }
	 
	
}


