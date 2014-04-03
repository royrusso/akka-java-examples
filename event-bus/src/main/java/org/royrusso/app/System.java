/**
  * (C) Copyright 2014 Roy Russo
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *
  */

package org.royrusso.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.royrusso.actor.Emitter;
import org.royrusso.actor.Handler;
import org.royrusso.command.Command;
import org.royrusso.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main runtime class.
 * <p/>
 * Create the Emitter Actor that will handle the initial Commands. The Emitter will publish events, of type Event, on the Akka EventBus.
 * We configure a Handler that will listen for instances of Event on the event stream.
 */
public class System {

    public static final Logger log = LoggerFactory.getLogger(System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("event-system");

        Thread.sleep(5000);

        final ActorRef emitter = actorSystem.actorOf(Props.create(Emitter.class));
        final ActorRef handler = actorSystem.actorOf(Props.create(Handler.class));
        actorSystem.eventStream().subscribe(handler, Event.class);


        for (int i = 0; i < 10; i++) {
            emitter.tell(new Command("CMD " + i), null);
        }

        Thread.sleep(5000);

        log.debug("Actor System Shutdown Starting...");

        actorSystem.shutdown();
    }
}
