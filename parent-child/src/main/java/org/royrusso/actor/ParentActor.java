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
 *
 *
 */


package org.royrusso.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.royrusso.command.Command;
import org.royrusso.event.Event;

import java.util.UUID;

/**
 * ParentActor receives an instance of Command and emits an Event to the ChildActor.
 *
 * @author royrusso
 */
public class ParentActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final ActorRef childActor;

    public ParentActor() {
        childActor = getContext().actorOf(Props.create(ChildActor.class), "child-actor");
    }

    @Override
    public void onReceive(Object msg) throws Exception {

        log.info("Received Command: " + msg);

        if (msg instanceof Command) {
            final String data = ((Command) msg).getData();
            final Event event = new Event(data, UUID.randomUUID().toString());

            childActor.tell(event, getSelf());
        } else if (msg.equals("echo")) {
            log.info("ECHO!");
        }
    }
}
