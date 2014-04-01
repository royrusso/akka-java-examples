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


package org.royrusso.event;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author royrusso
 */
public class SysState implements Serializable {
    private final ArrayList<String> events;

    public SysState() {
        this(new ArrayList<String>());
    }

    public SysState(ArrayList<String> events) {
        this.events = events;
    }

    public SysState copy() {
        return new SysState(new ArrayList<String>(events));
    }

    public void update(Event event) {
        events.add(event.getData());
    }

    public int size() {
        return events.size();
    }

    @Override
    public String toString() {
        return events.toString();
    }
}
