package org.apereo.cas.ticket.registry;

import org.apereo.cas.config.CouchbaseTicketRegistryConfiguration;
import org.apereo.cas.util.junit.EnabledIfPortOpen;

import lombok.Getter;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is {@link CouchbaseTicketRegistryTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@Tag("Couchbase")
@EnabledIfPortOpen(port = 8091)
@SpringBootTest(classes = {
    CouchbaseTicketRegistryConfiguration.class,
    BaseTicketRegistryTests.SharedTestConfiguration.class
},
    properties = {
        "cas.ticket.registry.couchbase.cluster-password=password",
        "cas.ticket.registry.couchbase.cluster-username=admin",
        "cas.ticket.registry.couchbase.scanConsistency=REQUEST_PLUS",
        "cas.ticket.registry.couchbase.bucket=testbucket"
    })
@Getter
public class CouchbaseTicketRegistryTests extends BaseTicketRegistryTests {
    @Autowired
    @Qualifier("ticketRegistry")
    private TicketRegistry newTicketRegistry;
}
