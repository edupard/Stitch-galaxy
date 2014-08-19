package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.constants.Roles;
import com.sg.dto.CanvasDto;
import com.sg.dto.CanvasRefDto;
import com.sg.dto.CanvasUpdateDto;
import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.ValidatorContext;
import com.sg.dto.AccountDto;
import com.sg.dto.CompleteSignupDto;
import com.sg.dto.SignupDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class})
public class JpaServiceTest {

    @Autowired
    private SgService service;

    @Autowired
    private DataSource dataSource;

    public JpaServiceTest() {
    }

    private static final String AIDA_14 = "Aida 14";
    private static final String AIDA_18 = "Aida 18";

    private static final String DMC = "DMC";
    private static final String ANCHOR = "Anchor";

    private static final String USER_LAST_NAME = "Tarasova";
    private static final String USER_FIRST_NAME = "Nadezhda";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_PASSWORD = "secret";

    private static final SignupDto signupDto;

    private static final CompleteSignupDto completeSignupDto;

    private static final ThreadDto dmcThreadDto;
    private static final ThreadDto anchorThreadDto;
    
    private static final CanvasDto aida14CanvasDto;
    private static final CanvasDto aida18CanvasDto;

    static {
        dmcThreadDto = new ThreadDto();
        dmcThreadDto.setCode(DMC);

        anchorThreadDto = new ThreadDto();
        anchorThreadDto.setCode(ANCHOR);
        
        aida14CanvasDto = new CanvasDto();
        aida14CanvasDto.setCode(AIDA_14);
        aida14CanvasDto.setStitchesPerInch(new BigDecimal(14));
        
        aida18CanvasDto = new CanvasDto();
        aida18CanvasDto.setCode(AIDA_18);
        aida18CanvasDto.setStitchesPerInch(new BigDecimal(18));
        

        signupDto = new SignupDto();
        signupDto.setEmail(USER_EMAIL);
        signupDto.setUserBirthDate(USER_BIRTH_DATE);
        signupDto.setUserFirstName(USER_FIRST_NAME);
        signupDto.setUserLastName(USER_LAST_NAME);

        completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(USER_PASSWORD);
    }
    
    @Test
    public void testCanvasCreate()
    {
        service.create(aida14CanvasDto);
        try {
            service.create(aida14CanvasDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }
    
    @Test
    public void testCanvasesList() {
        service.create(aida14CanvasDto);
        service.create(aida18CanvasDto);
        List<CanvasDto> list = new ArrayList<CanvasDto>();
        list.add(aida14CanvasDto);
        list.add(aida18CanvasDto);
        
        List<CanvasDto> result = service.listCanvases();
        Assert.assertEquals(list, result);
    }
    
    @Test
    public void testCanvasUpdate() {
        service.create(aida14CanvasDto);

        CanvasRefDto ref = new CanvasRefDto();
        ref.setCode(AIDA_18);
        CanvasUpdateDto updateDto = new CanvasUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(aida14CanvasDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }

        ref = new CanvasRefDto();
        ref.setCode(AIDA_14);
        updateDto = new CanvasUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(aida18CanvasDto);
        service.update(updateDto);
        List<CanvasDto> list = new ArrayList<CanvasDto>();
        list.add(aida18CanvasDto);
        Assert.assertEquals(list, service.listCanvases());
        service.create(aida14CanvasDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }
    
    @Test
    public void testCanvasDelete() {
        service.create(aida14CanvasDto);

        CanvasRefDto ref = new CanvasRefDto();
        ref.setCode(AIDA_18);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }
        
        ref = new CanvasRefDto();
        ref.setCode(AIDA_14);
        
        service.delete(ref);
        
        Assert.assertEquals(0, service.listCanvases().size());
    }

    @Test
    public void testThreadCreate() {
        service.create(dmcThreadDto);
        try {
            service.create(dmcThreadDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadsList() {
        service.create(dmcThreadDto);
        service.create(anchorThreadDto);
        List<ThreadDto> list = new ArrayList<ThreadDto>();
        list.add(dmcThreadDto);
        list.add(anchorThreadDto);
        Assert.assertEquals(list, service.listThreads());
    }

    @Test
    public void testThreadUpdate() {
        service.create(dmcThreadDto);

        ThreadRefDto ref = new ThreadRefDto();
        ref.setCode(ANCHOR);
        ThreadUpdateDto updateDto = new ThreadUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(dmcThreadDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }

        ref = new ThreadRefDto();
        ref.setCode(DMC);
        updateDto = new ThreadUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(anchorThreadDto);
        service.update(updateDto);
        List<ThreadDto> list = new ArrayList<ThreadDto>();
        list.add(anchorThreadDto);
        Assert.assertEquals(list, service.listThreads());
        service.create(dmcThreadDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadDelete() {
        service.create(dmcThreadDto);

        ThreadRefDto ref = new ThreadRefDto();
        ref.setCode(ANCHOR);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }
        
        ref = new ThreadRefDto();
        ref.setCode(DMC);
        
        service.delete(ref);
        
        Assert.assertEquals(0, service.listThreads().size());
    }

    @Test
    public void testSignup() {
        Long id = service.signup(signupDto, Roles.ROLE_ADMIN, Roles.ROLE_USER);

        AccountDto found = service.getUserByEmail(USER_EMAIL);

        AccountDto expected = new AccountDto();
        expected.setId(id);
        expected.setEmail(USER_EMAIL);
        expected.setEmailVerified(Boolean.FALSE);
        expected.setRoles(Arrays.asList(new String[]{Roles.ROLE_ADMIN, Roles.ROLE_USER}));
        expected.setUserBirthDate(USER_BIRTH_DATE);
        expected.setUserFirstName(USER_FIRST_NAME);
        expected.setUserLastName(USER_LAST_NAME);

        Assert.assertEquals(expected, found);
    }

    @Test
    public void testCompleteSignup() {
        Long accountId = service.signup(signupDto, Roles.ROLE_ADMIN, Roles.ROLE_USER);
        try {
            service.completeSignup(accountId + 1, completeSignupDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }
        service.completeSignup(accountId, completeSignupDto);
        try {
            service.completeSignup(accountId, completeSignupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        } catch (SgSignupAlreadyCompletedException e) {
        }
    }

    @After
    public void cleanupData() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                Statement stmt = connection.createStatement();
                try {
                    stmt.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
                    connection.commit();
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new Exception(e);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}