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

import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author royrusso
 */
public class DeadLetterActor extends UntypedActor {

    public static final Logger log = LoggerFactory.getLogger(DeadLetterActor.class);

    @Override
    public void onReceive(Object message) {
        if (message instanceof DeadLetter) {
            log.debug("Received Dead Letter: " + message.toString());
        }
    }

}
