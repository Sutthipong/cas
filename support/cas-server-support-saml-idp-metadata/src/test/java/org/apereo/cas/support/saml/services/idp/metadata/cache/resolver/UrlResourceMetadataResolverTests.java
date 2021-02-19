package org.apereo.cas.support.saml.services.idp.metadata.cache.resolver;

import org.apereo.cas.configuration.model.support.saml.idp.SamlIdPProperties;
import org.apereo.cas.services.DefaultRegisteredServiceAccessStrategy;
import org.apereo.cas.support.saml.SamlException;
import org.apereo.cas.support.saml.services.BaseSamlIdPServicesTests;
import org.apereo.cas.support.saml.services.SamlRegisteredService;

import lombok.val;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link UrlResourceMetadataResolverTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@Tag("SAML")
@TestPropertySource(properties = "cas.authn.saml-idp.metadata.file-system.location=file:/tmp")
public class UrlResourceMetadataResolverTests extends BaseSamlIdPServicesTests {
    public static final String METADATA_URL =
        "https://raw.githubusercontent.com/apereo/cas/master/support/cas-server-support-saml-idp/src/test/resources/metadata/testshib-providers.xml";

    public static final String MDQ_URL = "https://mdq.incommon.org/entities/{0}";

    @Test
    public void verifyResolverSupports() throws Exception {
        val props = new SamlIdPProperties();
        props.getMetadata().getFileSystem().setLocation(new FileSystemResource(FileUtils.getTempDirectory()).getFile().getCanonicalPath());
        val resolver = new UrlResourceMetadataResolver(props, openSamlConfigBean);
        val service = new SamlRegisteredService();
        service.setMetadataLocation(METADATA_URL);
        assertTrue(resolver.supports(service));
        service.setMetadataLocation("classpath:sample-sp.xml");
        assertFalse(resolver.supports(service));
        service.setMetadataLocation(MDQ_URL);
        assertFalse(resolver.supports(service));
    }

    @Test
    public void verifyResolverResolves() throws Exception {
        val props = new SamlIdPProperties();
        props.getMetadata().getFileSystem().setLocation(new FileSystemResource(FileUtils.getTempDirectory()).getFile().getCanonicalPath());
        val service = new SamlRegisteredService();
        val resolver = new UrlResourceMetadataResolver(props, openSamlConfigBean);
        service.setName("TestShib");
        service.setId(1000);
        service.setMetadataLocation(METADATA_URL);
        val results = resolver.resolve(service);
        assertFalse(results.isEmpty());
        assertTrue(resolver.isAvailable(service));
        assertFalse(resolver.supports(null));
    }

    @Test
    public void verifyResolverResolvesFailsAccess() throws Exception {
        val props = new SamlIdPProperties();
        props.getMetadata().getFileSystem().setLocation(new FileSystemResource(FileUtils.getTempDirectory()).getFile().getCanonicalPath());
        val service = new SamlRegisteredService();
        service.setAccessStrategy(new DefaultRegisteredServiceAccessStrategy(false, false));
        val resolver = new UrlResourceMetadataResolver(props, openSamlConfigBean);
        service.setName("TestShib");
        service.setId(1000);
        service.setMetadataLocation(METADATA_URL);
        assertThrows(SamlException.class, () -> resolver.resolve(service));
    }

    @Test
    public void verifyResolverUnknownUrl() throws Exception {
        val props = new SamlIdPProperties();
        props.getMetadata().getFileSystem().setLocation(new FileSystemResource(FileUtils.getTempDirectory()).getFile().getCanonicalPath());
        val service = new SamlRegisteredService();
        val resolver = new UrlResourceMetadataResolver(props, openSamlConfigBean);
        service.setName("TestShib");
        service.setId(1000);
        service.setMetadataLocation("https://localhost:9999");
        assertTrue(resolver.resolve(service).isEmpty());
    }

    @Test
    public void verifyResolverWithProtocol() {
        val props = new SamlIdPProperties();
        props.getMetadata().getFileSystem().setLocation("file:/" + FileUtils.getTempDirectory());
        val service = new SamlRegisteredService();
        val resolver = new UrlResourceMetadataResolver(props, openSamlConfigBean);
        service.setName("TestShib");
        service.setId(1000);
        service.setMetadataLocation(METADATA_URL);
        val results = resolver.resolve(service);
        assertFalse(results.isEmpty());
    }
}
