/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.scripting.documentation.internal;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.bridge.event.ApplicationReadyEvent;
import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;
import org.xwiki.context.ExecutionContext;
import org.xwiki.context.ExecutionContextException;
import org.xwiki.context.ExecutionContextManager;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.Event;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;

/**
 * Speed up first access to the UI by caching asynchronously standard bindings right when XWiki starts.
 * 
 * @version $Id$
 * @since 1.2
 */
@Component
@Named(ScriptBindingsFinderInitializerListener.NAME)
@Singleton
public class ScriptBindingsFinderInitializerListener implements EventListener
{
    /**
     * The name of the listener.
     */
    public static final String NAME = "ScriptBindingsFinderInitializerListener";

    private static final List<Event> EVENTS = Arrays.<Event>asList(new ApplicationReadyEvent());

    @Inject
    private ScriptBindingsFinder defaultScriptBindingsFinder;

    @Inject
    private Execution execution;

    @Inject
    private ExecutionContextManager executionContextManager;

    @Inject
    private Logger logger;

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public List<Event> getEvents()
    {
        return EVENTS;
    }

    @Override
    public void onEvent(Event event, Object source, Object data)
    {
        // Make bindings initialization asynchronous
        Thread thread = new Thread(() -> {
            try {
                // Initialize a standard execution context
                executionContextManager.initialize(new ExecutionContext());

                // Fill the cache with all the bindings which can be found in the current context (main wiki before
                // any execution of template/page)
                defaultScriptBindingsFinder.find();
            } catch (ExecutionContextException e) {
                logger.error("Failed to initialize ExecutionContext", e);
            } finally {
                // Cleanup context
                execution.removeContext();
            }
        });
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setName("Scriptint Documentation binding initialization");

        thread.start();
    }
}
