package com.soagrowers.todo;

import com.soagrowers.todo.aggregates.Todo;
import com.soagrowers.todo.commands.CreateCommand;
import com.soagrowers.todo.commands.MarkDoneCommand;
import com.soagrowers.todo.commands.MarkUndoneCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by ben on 22/09/15.
 */
public class TodoCommandApiTest {


    @AfterClass
    public static void closeDown() throws InterruptedException{
        // Give the message listeners time to consume from the message queues..
        // Otherwise the messages 'back-up'
        TimeUnit.SECONDS.sleep(5l);
    }

    @Test
    public void testGatewayIsntNull(){
        CommandGateway gateway = TodoCommandApi.getGateway();
        assertNotNull(gateway);
    }

    @Test
    public void testRepositoryIsntNull(){
        EventSourcingRepository<Todo> repo = TodoCommandApi.getRepository();
        assertNotNull(repo);
    }

    @Test
    public void createLotsOfTodos(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "TODO [" + id + "]";
            TodoCommandApi.getGateway().sendAndWait(new CreateCommand(id, title));
        }
    }

    @Test
    public void createLotsOfDoneTodos(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "TODO [" + id + "]";
            TodoCommandApi.getGateway().sendAndWait(new CreateCommand(id, title));
            TodoCommandApi.getGateway().sendAndWait(new MarkDoneCommand(id));
        }
    }

    @Test
    public void createLotsOfUndoneTodos(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "TODO [" + id + "]";
            TodoCommandApi.getGateway().sendAndWait(new CreateCommand(id, title));
            TodoCommandApi.getGateway().sendAndWait(new MarkDoneCommand(id));
            TodoCommandApi.getGateway().sendAndWait(new MarkUndoneCommand(id));
        }
    }
}