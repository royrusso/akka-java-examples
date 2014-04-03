package org.royrusso.actor;

/**
 *
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

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.persistence.Deliver;
import akka.persistence.Persistent;
import akka.persistence.PersistentChannel;
import akka.persistence.PersistentChannelSettings;
import org.royrusso.command.ChannelReply;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


/**
 * The BaseProcessor receives a Command and forwards that Command on to a channel.
 * Once the command is picked up by the Received on the channel, the ChannelReply is printed.
 */
public class BaseProcessor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef receiver;
    private ActorRef channel;

    public BaseProcessor(ActorRef receiver) {
        this.receiver = receiver;
        this.channel = getContext().actorOf(PersistentChannel.props(
                PersistentChannelSettings.create()
                        .withRedeliverInterval(Duration.create(30, TimeUnit.SECONDS))
                        .withPendingConfirmationsMax(10000) // max # of pending confirmations. suspend delivery until <
                        .withPendingConfirmationsMin(2000)  // min # of pending confirmation. suspend delivery until >
                        .withReplyPersistent(true)          // ack
                        .withRedeliverMax(15)), "channel");
    }

    @Override
    public void onReceive(Object msg) {

        if (msg instanceof Persistent) {

            log.info("Send to Channel: " + ((Persistent) msg).payload());

            channel.tell(Deliver.create(((Persistent) msg).withPayload(((Persistent) msg).payload()), receiver.path()), getSelf());
        } else if (msg instanceof ChannelReply) {
            log.info(msg.toString());
        }
    }


}
