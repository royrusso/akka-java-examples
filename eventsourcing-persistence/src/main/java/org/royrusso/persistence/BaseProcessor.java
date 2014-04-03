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

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedEventsourcedProcessor;
import org.royrusso.event.Command;
import org.royrusso.event.Event;

import java.util.UUID;

/**
 * Processor receives an instance of Command. Event is generated from this and persisted. On successful
 * persist, it updates the state of the processor. This allows a complete recovery of state in case of failure
 * by replaying the events that are in the journal and snapshot(s).
 *
 * @author royrusso
 */
public class BaseProcessor extends UntypedEventsourcedProcessor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     * The state of the processor
     */
    private ProcessorState processorState = new ProcessorState();

    /**
     * Called on restart. Loads from Snapshot first, and then replays Journal Events to update state.
     *
     * @param msg
     */
    public void onReceiveRecover(Object msg) {
        log.info("Received Recover: " + msg);
        if (msg instanceof Event) {
            processorState.update((Event) msg);

        } else if (msg instanceof SnapshotOffer) {
            processorState = (ProcessorState) ((SnapshotOffer) msg).snapshot();
        }
    }

    /**
     * Called on Command dispatch
     *
     * @param msg
     */
    public void onReceiveCommand(Object msg) {
        log.info("Received Command: " + msg);
        if (msg instanceof Command) {
            final String data = ((Command) msg).getData();

            // generate an event we will persist after being enriched with a uuid
            final Event event = new Event(data, UUID.randomUUID().toString());

            // persist event and THEN update the state of the processor
            persist(event, new Procedure<Event>() {
                public void apply(Event evt) throws Exception {

                    processorState.update(evt);

                    // broadcast event on eventstream
                    getContext().system().eventStream().publish(evt);
                }
            });
        } else if (msg.equals("snapshot")) {
            saveSnapshot(processorState.copy());
        } else if (msg.equals("printstate")) {
            log.info(processorState.toString());
        }
    }
}
