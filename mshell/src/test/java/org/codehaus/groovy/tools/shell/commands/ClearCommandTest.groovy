/*
 * Copyright 2003-2007 the original author or authors.
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

package org.codehaus.groovy.tools.shell.commands

/**
 * Tests for the {@link ClearCommand} class.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class ClearCommandTest
    extends CommandTestSupport
{
    void testClear() {
        shell.execute(ClearCommand.COMMAND_NAME)
    }

    void testClearWithArgs() {
        shell.execute(ClearCommand.COMMAND_NAME + ' foo')
    }
}