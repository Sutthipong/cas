package org.apereo.cas.services.util;

import org.apereo.cas.util.ResourceUtils;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link CasAddonsRegisteredServicesJsonSerializerTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@Tag("RegisteredService")
public class CasAddonsRegisteredServicesJsonSerializerTests {

    @Test
    public void verifySupports() {
        val s = new CasAddonsRegisteredServicesJsonSerializer();
        assertTrue(s.supports(new File("servicesRegistry.conf")));
    }

    @Test
    public void verifyLoad() {
        val s = new CasAddonsRegisteredServicesJsonSerializer();
        val services = s.load(getServiceRegistryResource());
        assertEquals(3, services.size());
    }

    @Test
    public void verifyLoadEmpty() throws Exception {
        val s = new CasAddonsRegisteredServicesJsonSerializer();
        val services = s.load(ResourceUtils.EMPTY_RESOURCE.getInputStream());
        assertEquals(0, services.size());
    }

    @SneakyThrows
    private static InputStream getServiceRegistryResource() {
        val file = new File("servicesRegistry.conf");
        return new ClassPathResource(file.getPath()).getInputStream();
    }
}
