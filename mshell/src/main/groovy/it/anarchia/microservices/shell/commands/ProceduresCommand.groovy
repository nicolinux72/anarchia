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
class ProceduresCommand
    extends CommandSupport
{

    ProceduresCommand(final Groovysh shell) {
        super(shell, ':procedures', ':R')
    }


    Object execute(final List<String> args) {
        assert args != null

        if (args.isEmpty()) {
            fail('Command \'entity\' requires three arguments') // TODO: i18n
        }
		  
		  if (args.size() != 3) {
			  fail(messages.format('error.unexpected_args', args.join(' ')))
		 }
	
		  procedures(args[0],args[1],args[2])	  
	 }
	 
	 void procedures(String schema, String pack, String catalog) {
		 def meta    = DatabaseUtility.openAppConnection().connection.metaData
		 def file    = TemplateUtility.createFile("src/main/java/${pack.replaceAll('\\.','/')}/${JVMUtility.camel(catalog,true)}Proc.groovy")
		 
		 ["package $pack\n\n",
		 
		 "import java.util.Map",
		 "import java.sql.SQLException",
		 "import javax.sql.DataSource",
		 "import org.springframework.beans.factory.annotation.Autowired",
		 "import org.springframework.dao.DataAccessException",
		 "import org.springframework.jdbc.core.JdbcTemplate",
		 "import org.springframework.jdbc.core.namedparam.MapSqlParameterSource",
		 "import org.springframework.jdbc.core.namedparam.SqlParameterSource",
		 "import org.springframework.jdbc.core.simple.SimpleJdbcCall",
		 "import org.springframework.stereotype.Repository\n",
		 
		 "@Repository",
		 "public class ${JVMUtility.camel(catalog,true)}Package {\n",
		 "  private SimpleJdbcCall getDefaultAddressProc",
		  "  private JdbcTemplate jdbcTemplate\n"].each { TemplateUtility.write file, "$it\n"}
		 
		 def fields = []      // definisce  i diversi campi SimpleJdbcCall
		 def simpleCalls = [] // instanzia i diversi campi SimpleJdbcCall
		 
		 def proc = meta.getProcedures(catalog.toUpperCase(), schema.toUpperCase(), null)
		 while (proc.next()) {
			 def procName = proc.getString("PROCEDURE_NAME")
			 //println "\n$procName"
	 
			 def returnType  = "Map<String, Object>"
			 def returnParam = null
			 def firma = [];	def inside = []; def outp = []; def label = 'Proc'
			 def column  = meta.getProcedureColumns(catalog.toUpperCase(), schema.toUpperCase(), procName, null)
			 while (column.next()) {
				 
				 def param = [ name: column.getString("COLUMN_NAME"), type:column.getInt("DATA_TYPE"),
				 columnType: column.getInt("COLUMN_TYPE"), typeName: column.getString("TYPE_NAME")]
				 param.javaType = JVMUtility.getJavaType(param.type)
				 param.javaName = JVMUtility.camel(param.name)
			 
				 if (param.columnType == DatabaseMetaData.functionColumnResult && param.name == null) {
					 param.name = "returnValue"
					 returnType = param.javaType
					 label = 'Func' //è una funzione, non una procedura
				 } else if (param.columnType != DatabaseMetaData.procedureColumnOut)
				 {
					 firma  << "$param.javaType $param.javaName"
					 inside << "    .addValue(\"$param.name\", $param.javaName)"
				 } else if (param.columnType ==DatabaseMetaData.procedureColumnOut) {
					 outp << "// OUT PARAMS: ${param.typeName}  ${param.name} ${param.javaType} "
	 
					 if (returnParam == null) {
						 returnParam = param.name
						 returnType =  param.javaType
					 }
				 }
				 //println "$p.type $p.name $p.javaType $p.javaName"
			 }
				
			 def fieldName = "${JVMUtility.camel(procName)}$label"
			 def execution =  label == 'Proc' ? "execute(inp)" : "executeFunction(${returnType}.class, inp)"
				fields << "  private SimpleJdbcCall $fieldName"
	 
			 ["$fieldName = new SimpleJdbcCall(jdbcTemplate)",
				"  .withSchemaName(\"$schema\")",
				"  .withCatalogName(\"$catalog\")",
				(label == 'Proc' ? "  .withProcedureName(\"$procName\")\n" : "  .withFunctionName(\"$procName\")\n"),
				].each { simpleCalls << "    $it"  }
			 
			 //Inizio con il metodo corrispondente alla procedura
			 TemplateUtility.write file, "  //${procName}\n"
			 TemplateUtility.write file, "  public $returnType ${JVMUtility.camel(procName)} (${firma.join(", ")}) {\n"
			 TemplateUtility.write file, "    SqlParameterSource inp = new MapSqlParameterSource()\n"
			 
			 inside.each { TemplateUtility.write file, "  $it\n"} //converto i parametri da java a pl/sql
			 
			 //ora eseguiamo la chiamata, ritentando se occorre
			 ["\n","def out=[]\n",
			 "try {\n",
			 "  out = $fieldName.$execution\n",
			 "} catch (DataAccessException e) {\n",
			 "  if (!e.contains(SQLException.class) || !(e.getMessage().contains(\"ORA-04068\"))) throw e\n",
			 "  out = $fieldName.$execution\n",
			 "}\n"].each { TemplateUtility.write file, "    $it" }
	 
			 //restituisco il valore corretto
			  TemplateUtility.write file, (returnParam != null ? "    return out['$returnParam']\n" : "    return out\n" )
			 outp.each { TemplateUtility.write file, "  $it\n"} //elenco gli OUT PARAMETER, può tornare utile
			 
			 TemplateUtility.write file, "  }\n\n"
		 }
	 
		 // aggiungiamo i campi per tutte le procedure ed il metodo che le istanzia
		 [fields.join("\n"),
		 "\n  @Autowired",
		  "  public void setDataSource(DataSource dataSource) {",
		  "    jdbcTemplate = new JdbcTemplate(dataSource);",
		  "    jdbcTemplate.setResultsMapCaseInsensitive(true);\n",
		  simpleCalls.join("\n"),
		  "  }",
		 "}"].each { TemplateUtility.write file, "$it\n" }
		 
	 }
}


