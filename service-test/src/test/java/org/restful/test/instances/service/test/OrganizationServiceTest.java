package org.restful.test.instances.service.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.restful.test.instances.model.detail.OrganizationDetail;
import org.restful.test.instances.model.entity.Organization;
import org.restful.test.instances.repository.OrganizationRepository;
import org.restful.test.instances.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @project restful-test-instances
 * @created 2020-04-25 14:09
 * <p>
 * @author Alexander A. Kropotin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@Slf4j
@NoArgsConstructor
@FieldDefaults(
        level = AccessLevel.PRIVATE
)
public class OrganizationServiceTest {

    @MockBean
    OrganizationRepository organizationRepository;

    @InjectMocks
    @Autowired
    OrganizationService organizationService;

    @Before
    public void beforeEachTest() {
        when(organizationRepository.save(any(Organization.class)))
                .thenReturn(Organization.builder()
                        .uid(Long.valueOf(1))
                        .build()
                );
        when(organizationRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(Organization.builder()
                        .uid(Long.valueOf(1))
                        .build())
                );
        when(organizationRepository.findById(2L))
                .thenReturn(null);
    }

    @Test
    public void create_positive_whenNameNotNull_thenSuccessfulCreated() throws JsonMappingException {
        OrganizationDetail createOrganizationRequest = OrganizationDetail.builder()
                .name(Optional.of("WCorp"))
                .build();

        log.info("Создали ДТО `Организация` - {}", createOrganizationRequest);

        OrganizationDetail organizationResponse = this.organizationService.create(createOrganizationRequest);

        verify(organizationRepository).save(any(Organization.class));
        assertNotNull(organizationResponse, "Что-то пошло не так");
        assertNotNull(organizationResponse.getUid().orElse(null), "Иденьтификатор null");
        assertTrue(
                organizationResponse.getUid().orElse(null).equals(Long.valueOf(1)),
                "Иденьтификаторы разные"
        );
    }

    @Test
    public void update_positive_whenNameNotNull_thenSuccessfulUpdated() throws JsonMappingException {
        Long uidOrganization = 1L;
        OrganizationDetail updateOrganizationRequest = OrganizationDetail.builder()
                .name(Optional.of("WCorp"))
                .build();

        log.info("Создали ДТО `Организация` - {}", updateOrganizationRequest);

        OrganizationDetail organizationResponse = this.organizationService.update(
                uidOrganization,
                updateOrganizationRequest
        );

        verify(organizationRepository).findById(uidOrganization);
        verify(organizationRepository).save(any(Organization.class));
        assertNotNull(organizationResponse, "Что-то пошло не так");
        assertNotNull(organizationResponse.getUid().orElse(null), "Иденьтификатор null");
        assertTrue(
                organizationResponse.getUid().orElse(null).equals(Long.valueOf(1)),
                "Иденьтификаторы разные"
        );
    }
}
