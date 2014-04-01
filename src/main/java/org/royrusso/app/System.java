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
import akka.actor.DeadLetter;
import akka.actor.Props;
import org.royrusso.actor.DeadLetterActor;
import org.royrusso.event.Command;
import org.royrusso.persistence.BaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class System {


    public static final Logger log = LoggerFactory.getLogger(System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("scale-server");
        Thread.sleep(5000);
        //final ActorRef actorRef = actorSystem.actorOf(Props.create(WorkerActor.class), "scale-processor");
        final ActorRef actorRef = actorSystem.actorOf(Props.create(BaseProcessor.class), "scale-processor");

        final ActorRef actor = actorSystem.actorOf(Props.create(DeadLetterActor.class));
        actorSystem.eventStream().subscribe(actor, DeadLetter.class);

        actorRef.tell(new Command("AAAAAAA"), null);
        actorRef.tell(new Command("BBBBBBB"), null);
        actorRef.tell(new Command("CCCCCCC"), null);
        actorRef.tell(new Command("DDDDDDD"), null);
        actorRef.tell("snap", null);
        actorRef.tell(new Command("EEEEEEE"), null);
        actorRef.tell("print", null);

        Thread.sleep(5000);

        log.debug("Actor System Shutdown Starting...");

        actorSystem.shutdown();
    }
}
