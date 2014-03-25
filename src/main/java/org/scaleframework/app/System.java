package org.scaleframework.app;
/*
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
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.scaleframework.event.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class System {


    public static final Logger log = LoggerFactory.getLogger(System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("scale-server");

        final ActorRef actorRef = actorSystem.actorOf(Props.create(GenericActor.class), "scale-processor");

        actorRef.tell(new Command("1"), null);
        actorRef.tell(new Command("2"), null);
        actorRef.tell(new Command("3"), null);
        actorRef.tell(new Command("4"), null);

        Thread.sleep(1000);

        log.debug("Actor System Shutdown Starting...");

        actorSystem.shutdown();
    }

    public static void startUp(String[] config) {

    }
}
