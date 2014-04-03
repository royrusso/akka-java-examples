Akka examples in Java
==============

A few examples using Akka with Java. These examples have basic structures in common:

* Coded in Java.
* Clustered Akka setup.
* Logback / SL4J Logger. Log file is under the root path: ``/logs``
* Akka configuration has most logging options turned on. (chatty)

All examples are runnable from within your IDE, by executing the ``Main`` method in the corresponding ``org.royrusso.app.System`` class.

Simple Akka Example
------------------

``/simple`` :: The simplest of Akka examples. Creates an Actor that processes a command.

Akka Parent-Child Actors
------------------

``/parent-child`` :: This example illustrates how you can configure an Akka cluster for hierarchical Actor relationships.
This cluster contains Parent Actors that, given a Command, send an Event to a Child Actor for processing.

Akka Persistence with EventSourcing
------------------

``/eventsourcing-persistence`` :: Usage of the new Akka Persistence module with Event Sourcing. An Akka Processor is responsible for processing non-persistent Commands
that generate Events. The Events are persisted, and then allowed to modify the state of the processor. Additionally, the events are broadcast over the eventstream.

During recovery, the system then loads the persisted Events and replays them to the processor.

On restart, the system will load its last known state from the latest Snapshot (``snapshot`` dir), and replay all Journaled (``journal`` dir) Events after that point.

Notes:

* LevelDB is used for persistence in this example.
* ``/snapshots`` contains the snapshot of the processor states.
* ``/journal`` contains the running journal of events.

Akka Persistent Channels
-----------------------

``persistent-channel`` :: Illustrates how to send/receive payloads between actors listening on a channel. Messages are persisted until there is a valid confirmation
of it being received by the destination Actor, and then deleted. This example also illustrates how the destination Actor may respond with an ack.


Akka Event-Bus
------------------

``/event-bus`` :: Here we show how the Akka EventStream can be used by Actors that are subscribed to listen for certain event types that are emitted by another actor.
