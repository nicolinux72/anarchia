package it.anarchia.microservices.shell.commands

/**
 * Comando per la creazione di un nuovo progetto
 * 
 * @author nicola
 */
import it.anarchia.microservices.shell.Utility;
import jline.console.completer.Completer
import org.codehaus.groovy.tools.shell.util.SimpleCompletor

import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Command
import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.completion.CommandNameCompleter
import org.codehaus.groovy.tools.shell.util.Preferences

class ExecuteCommand
    extends CommandSupport
{

    public static final String COMMAND_NAME = ':execute'

    ExecuteCommand(final Groovysh shell) {
        super(shell, COMMAND_NAME, ':X')
    }

  
    @Override
    Object execute(final List<String> args) {
        assert args != null

        if (args.size() == 0) {
            fail(messages.format('error.unexpected_args', args.join(' ')))
        }

        Utility.execute(io, [args.join(' ')])
    }
	 
}

