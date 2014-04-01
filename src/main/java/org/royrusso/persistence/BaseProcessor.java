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

package org.royrusso.persistence;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedEventsourcedProcessor;
import org.royrusso.actor.WorkerActor;
import org.royrusso.event.Command;
import org.royrusso.event.Event;
import org.royrusso.event.SysState;

/**
 * @author royrusso
 */
public class BaseProcessor extends UntypedEventsourcedProcessor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private SysState sysState = new SysState();

    private final ActorRef workerActor;

    public BaseProcessor() {
        workerActor = getContext().actorOf(Props.create(WorkerActor.class), "worker");
    }

    public int getNumEvents() {
        return sysState.size();
    }

    public void onReceiveRecover(Object msg) {
        log.info("Received Recover: " + msg);
        if (msg instanceof Event) {
            sysState.update((Event) msg);
        } else if (msg instanceof SnapshotOffer) {
            sysState = (SysState) ((SnapshotOffer) msg).snapshot();
        }
    }

    public void onReceiveCommand(Object msg) {
        log.info("Received Command: " + msg);
        if (msg instanceof Command) {
            final String data = ((Command) msg).getData();
            final Event event = new Event(data);
            persist(event, new Procedure<Event>() {
                public void apply(Event evt) throws Exception {
                    sysState.update(evt);
                    //getContext().system().eventStream().publish(evt);
                    workerActor.tell("foo", getSelf());
                }
            });
/*
            final Event evt1 = new Event(data + "-" + getNumEvents());
            final Event evt2 = new Event(data + "-" + (getNumEvents() + 1));
            persist(asList(evt1, evt2), new Procedure<Event>() {
                public void apply(Event evt) throws Exception {
                    sysState.update(evt);
                    if (evt.equals(evt2)) {
                        getContext().system().eventStream().publish(evt);
                    }
                }
            });
*/
        } else if (msg.equals("snap")) {
            // IMPORTANT: create a copy of snapshot
            // because ExampleState is mutable !!!
            saveSnapshot(sysState.copy());
        } else if (msg.equals("print")) {
            System.out.println(sysState);
        }
    }
}
